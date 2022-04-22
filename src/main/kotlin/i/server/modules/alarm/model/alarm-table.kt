package i.server.modules.alarm.model

import i.server.modules.client.model.ClientTable
import i.server.modules.monitor.model.MonitorTypeTable
import i.server.utils.comment
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object AlarmRuleGroupTable : IntIdTable("t_alarm_rule_group") {
    val name = varchar("name", 128).uniqueIndex()
    val description = varchar("description", 255)
}

object AlarmRoleGroupLinkTable : Table("t_alarm_rule_group_link") {
    val alarm = reference("alarm", AlarmRuleTable)
    val group = reference("group", AlarmRuleGroupTable)
    override val primaryKey = PrimaryKey(alarm, group)
}

object AlarmRuleTable : IntIdTable("t_alarm_rule") {
    val name = varchar("name", 255).uniqueIndex().comment("规则名称")
    val expression = varchar("expression", 512).comment("规则表达式")
}

object AlarmRuleDependsTable : Table("t_alarm_rule_linked_monitor") {
    val alarm = reference("alarm", AlarmRuleTable)
    val monitor = reference("monitor_type", MonitorTypeTable)
    override val primaryKey = PrimaryKey(alarm, monitor)
}

object ClientAlarmRuleTable : Table("t_alarm_rule_link_client") {
    val alarm = reference("alarm", AlarmRuleTable)
    val client = reference("client", ClientTable)
    override val primaryKey = PrimaryKey(alarm, client)
}
