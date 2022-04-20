package i.server.modules.monitor.service.impl

import i.server.modules.client.model.ClientTable
import i.server.modules.client.model.MiniClientResultView
import i.server.modules.monitor.model.ClientMonitorGroupView
import i.server.modules.monitor.model.ClientMonitorTypeResultView
import i.server.modules.monitor.model.ClientMonitorTypeTable
import i.server.modules.monitor.model.ClientMonitorTypeView
import i.server.modules.monitor.model.MiniMonitorTypeResultView
import i.server.modules.monitor.model.MonitorTypeTable
import i.server.modules.monitor.service.IClientMonitorTypeService
import i.server.utils.autoRollback
import i.server.utils.template.PageView
import org.d7z.light.db.api.LightDB
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClientMonitorTypeServiceImpl(
    lightDB: LightDB,
) : IClientMonitorTypeService {
    private val clients = lightDB.withMap("client-monitor")
    override fun updateByGroup(clientMonitor: ClientMonitorGroupView): ClientMonitorTypeResultView = autoRollback {
        ClientMonitorTypeTable.deleteWhere { ClientMonitorTypeTable.client eq clientMonitor.clientId }
        ClientMonitorTypeTable.batchInsert(
            MonitorTypeTable.select { MonitorTypeTable.group.inList(clientMonitor.groupsId) }
                .map { it[MonitorTypeTable.id].value }
        ) {
            this[ClientMonitorTypeTable.client] = clientMonitor.clientId
            this[ClientMonitorTypeTable.monitorType] = it
        }
        getClientMonitorTypeResult(clientMonitor.clientId)
    }

    @Transactional
    override fun updateByType(clientMonitor: ClientMonitorTypeView): ClientMonitorTypeResultView = autoRollback {
        ClientMonitorTypeTable.deleteWhere { ClientMonitorTypeTable.client eq clientMonitor.clientId }
        ClientMonitorTypeTable.batchInsert(clientMonitor.typesId) {
            this[ClientMonitorTypeTable.client] = clientMonitor.clientId
            this[ClientMonitorTypeTable.monitorType] = it
        }
        getClientMonitorTypeResult(clientMonitor.clientId)
    }

    private fun getClientMonitorTypeResult(clientId: Int): ClientMonitorTypeResultView {
        val client = ClientTable.select { ClientTable.id eq clientId }.first().let {
            MiniClientResultView(it[ClientTable.id].value, it[ClientTable.name])
        }
        val monitors = ClientMonitorTypeTable.leftJoin(MonitorTypeTable).select {
            ClientMonitorTypeTable.client eq clientId
        }.map {
            MiniMonitorTypeResultView(it[MonitorTypeTable.id].value, it[MonitorTypeTable.name])
        }
        clients.get("client:$clientId", String::class, String::class).ifPresent {
            it.removeKey("monitor.types")
        }
        return ClientMonitorTypeResultView(client, monitors)
    }

    override fun getClientMonitor(clientId: Int): List<String> = autoRollback {
        ClientMonitorTypeTable.leftJoin(MonitorTypeTable)
            .select { ClientMonitorTypeTable.client eq clientId }.map {
                it[MonitorTypeTable.id].value
            }
    }

    override fun getClientMonitors(page: Pageable): PageView<ClientMonitorTypeResultView> = autoRollback {
        ClientTable.selectAll().limit(page.pageSize, page.offset).map {
            val data = ClientMonitorTypeTable.leftJoin(MonitorTypeTable).select {
                ClientMonitorTypeTable.client eq it[ClientTable.id].value
            }.map { data ->
                MiniMonitorTypeResultView(data[MonitorTypeTable.id].value, data[MonitorTypeTable.name])
            }
            ClientMonitorTypeResultView(
                MiniClientResultView(it[ClientTable.id].value, it[ClientTable.name]),
                data
            )
        }.let { data -> PageView(data, page.offset.toInt(), page.pageSize, ClientTable.selectAll().count()) }
    }
}
