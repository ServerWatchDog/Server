package i.server.modules.client.service.impl

import i.server.modules.agent.model.ClientSessionData
import i.server.modules.client.model.ClientGroupTable
import i.server.modules.client.model.ClientLinkGroupTable
import i.server.modules.client.model.ClientResultView
import i.server.modules.client.model.ClientTable
import i.server.modules.client.model.ClientView
import i.server.modules.client.model.MinClientGroupResultView
import i.server.modules.client.service.ClientSessionService
import i.server.modules.client.service.IClientService
import i.server.modules.user.model.RolesTable
import i.server.utils.autoRollback
import i.server.utils.template.crud.CRUDServiceImpl
import org.d7z.light.db.modules.session.LightSession
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

@Service("client")
class ClientServiceImpl(
    private val lightSession: LightSession,
) : IClientService, ClientSessionService, CRUDServiceImpl<ClientView, ClientResultView, Int, ClientTable> {
    override val table = ClientTable

    override fun ClientTable.tableToOutput(it: ResultRow): ClientResultView {
        val clientId = it[id].value
        return ClientResultView(
            id = clientId,
            name = it[name], token = it[token], enable = it[enable],
            createTime = it[createTime], updateTime = it[updateTime],
            description = it[description],
            groups = ClientLinkGroupTable
                .leftJoin(ClientGroupTable)
                .leftJoin(RolesTable).select { ClientLinkGroupTable.client eq clientId }.groupBy(
                    RolesTable.id
                ).map {
                    MinClientGroupResultView(it[RolesTable.name], it[ClientGroupTable.description])
                }
        )
    }

    override fun insert(input: ClientView) = autoRollback {
        table.insertAndGetId {
            inputToTable(it, input)
            it[this.createTime] = LocalDateTime.now()
            it[this.updateTime] = LocalDateTime.now()
            it[token] = generateToken() // 强制刷新
        }.value.let { id ->
            table.select { table.id eq id }.first().let {
                it.insertAfterHook(id, input)
                table.tableToOutput(it)
            }
        }
    }

    override fun ClientTable.inputToTable(it: UpdateBuilder<Int>, input: ClientView) {
        it[name] = input.name
        if (input.refreshToken) {
            it[token] = generateToken()
        }
        it[description] = input.description
        it[enable] = input.enable
    }

    override fun getClientByToken(sessionId: String) = autoRollback {
        table.select { table.token eq sessionId }.firstOrNull()?.let {
            Optional.of(table.tableToOutput(it))
        } ?: Optional.empty()
    }

    override fun generateToken(): String {
        return lightSession.sessionGenerate.generate("client")
    }

    override fun verify(sessionId: String, permissions: Array<String>): Boolean {
        return lightSession.findSessionContext(sessionId).or {
            getClientByToken(sessionId).filter { it.enable }.map {
                val newSession = lightSession.getSessionGroupContext("client").newSession(sessionId)
                ClientSessionData(
                    newSession
                        .apply { refresh() }
                ).apply {
                    clientId = it.id
                }
                newSession
            }
        }.isPresent
    }
}
