package i.server.modules.alarm.service.impl

import i.server.modules.alarm.model.AlarmRoleGroupLinkTable
import i.server.modules.alarm.model.AlarmRuleTable
import i.server.modules.alarm.model.ClientAlarmResultView
import i.server.modules.alarm.model.ClientAlarmRuleTable
import i.server.modules.alarm.model.MiniAlarmRuleResultView
import i.server.modules.alarm.service.IAlarmService
import i.server.modules.client.model.ClientTable
import i.server.utils.autoRollback
import i.server.utils.template.PageView
import i.server.utils.template.SimpleView
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AlarmServiceImpl : IAlarmService {
    override fun getClient(page: Pageable): PageView<ClientAlarmResultView> = autoRollback {
        ClientTable
            .selectAll().limit(page.pageSize, page.offset)
            .map {
                val data = ClientAlarmRuleTable.leftJoin(AlarmRuleTable)
                    .select { ClientAlarmRuleTable.client eq it[ClientTable.id].value }
                    .map { item -> MiniAlarmRuleResultView(item[AlarmRuleTable.id].value, item[AlarmRuleTable.name]) }
                ClientAlarmResultView(it[ClientTable.id].value, it[ClientTable.name], data)
            }.let {
                PageView(it, page.pageNumber, page.pageSize, ClientTable.selectAll().count())
            }
    }

    override fun updateClientOnes(id: Int, data: List<Int>): SimpleView<Boolean> = autoRollback {

        ClientAlarmRuleTable.deleteWhere { ClientAlarmRuleTable.client eq id }
        val rules = AlarmRuleTable
            .select { AlarmRuleTable.id inList data }.map { it[AlarmRuleTable.id].value }
        ClientAlarmRuleTable.batchInsert(
            rules
        ) {
            this[ClientAlarmRuleTable.alarm] = it
            this[ClientAlarmRuleTable.client] = id
        }
        SimpleView(true)
    }

    override fun updateClientGroup(id: Int, data: List<Int>): SimpleView<Boolean> = autoRollback {
        updateClientOnes(
            id,
            AlarmRoleGroupLinkTable.select { AlarmRoleGroupLinkTable.group inList data }
                .map { it[AlarmRoleGroupLinkTable.alarm].value }
        )
    }
}
