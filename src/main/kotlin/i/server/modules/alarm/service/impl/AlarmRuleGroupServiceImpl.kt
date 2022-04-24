package i.server.modules.alarm.service.impl

import i.server.modules.alarm.model.AlarmGroupResultView
import i.server.modules.alarm.model.AlarmGroupView
import i.server.modules.alarm.model.AlarmRoleGroupLinkTable
import i.server.modules.alarm.model.AlarmRuleGroupTable
import i.server.modules.alarm.model.AlarmRuleTable
import i.server.modules.alarm.model.MiniAlarmRuleResultView
import i.server.modules.alarm.service.IAlarmRuleGroupService
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service

@Service
class AlarmRuleGroupServiceImpl :
    IAlarmRuleGroupService,
    CRUDServiceImpl<AlarmGroupView, AlarmGroupResultView, Int, AlarmRuleGroupTable> {
    override val table = AlarmRuleGroupTable

    override fun AlarmRuleGroupTable.inputToTable(it: UpdateBuilder<Int>, input: AlarmGroupView) {
        it[name] = input.name
        it[description] = input.description
    }

    override fun AlarmRuleGroupTable.tableToOutput(it: ResultRow): AlarmGroupResultView {
        val rules =
            AlarmRoleGroupLinkTable.leftJoin(AlarmRuleTable).select { AlarmRoleGroupLinkTable.group eq it[id].value }
                .map { MiniAlarmRuleResultView(it[AlarmRuleTable.id].value, it[AlarmRuleTable.name]) }
        return AlarmGroupResultView(it[id].value, it[name], it[description], rules)
    }

    override fun ResultRow.insertAfterHook(id: Int, input: AlarmGroupView) {
        if (input.rules.isNotEmpty()) {
            AlarmRoleGroupLinkTable.batchInsert(input.rules) {
                this[AlarmRoleGroupLinkTable.group] = id
                this[AlarmRoleGroupLinkTable.alarm] = it
            }
        }
    }

    override fun ResultRow.updateAfterHook(id: Int, input: AlarmGroupView) {
        if (input.rules.isNotEmpty()) {
            AlarmRoleGroupLinkTable.deleteWhere { AlarmRoleGroupLinkTable.group eq id }
            AlarmRoleGroupLinkTable.batchInsert(input.rules) {
                this[AlarmRoleGroupLinkTable.group] = id
                this[AlarmRoleGroupLinkTable.alarm] = it
            }
        }
    }
}
