package i.server.modules.client.service.impl

import i.server.modules.client.model.ClientGroupResultView
import i.server.modules.client.model.ClientGroupTable
import i.server.modules.client.model.ClientGroupView
import i.server.modules.client.model.ClientLinkGroupTable
import i.server.modules.client.model.ClientTable
import i.server.modules.client.model.MiniClientResultView
import i.server.modules.client.service.IClientGroupService
import i.server.modules.user.model.MiniRoleResultView
import i.server.modules.user.model.RolesTable
import i.server.utils.autoRollback
import i.server.utils.template.SimpleView
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service

@Service
class ClientGroupServiceImpl :
    IClientGroupService,
    CRUDServiceImpl<ClientGroupView, ClientGroupResultView, Int, ClientGroupTable> {
    override val table = ClientGroupTable

    override fun ClientGroupTable.tableToOutput(it: ResultRow): ClientGroupResultView {
        val clientList =
            ClientLinkGroupTable.leftJoin(ClientTable).select { ClientLinkGroupTable.group eq it[id].value }
                .map { item -> MiniClientResultView(item[ClientTable.id].value, item[ClientTable.name]) }
        val miniRole = RolesTable.select { RolesTable.id eq it[table.role].value }.first()
            .let { item -> MiniRoleResultView(item[RolesTable.id].value, item[RolesTable.name]) }
        return ClientGroupResultView(
            id = it[id].value,
            description = it[description],
            role = miniRole,
            clients = clientList,
            createTime = it[createTime],
            updateTime = it[updateTime]
        )
    }

    override fun ClientGroupTable.inputToTable(it: UpdateBuilder<Int>, input: ClientGroupView) {
        it[description] = input.description
        it[role] = input.roleId
    }

    override fun ResultRow.insertAfterHook(id: Int, input: ClientGroupView) {
        ClientLinkGroupTable.deleteWhere {
            ClientLinkGroupTable.group eq id
        }
        ClientLinkGroupTable.batchInsert(input.clients) { data ->
            this[ClientLinkGroupTable.client] = data
            this[ClientLinkGroupTable.group] = id
        }
    }

    override fun ResultRow.updateAfterHook(id: Int, input: ClientGroupView) {
        if (input.clients.isNotEmpty()) {
            insertAfterHook(id, input)
        }
    }

    override fun delete(id: Int): SimpleView<Boolean> = autoRollback {
        ClientLinkGroupTable.deleteWhere {
            ClientLinkGroupTable.group eq id
        }
        super.delete(id)
    }
}
