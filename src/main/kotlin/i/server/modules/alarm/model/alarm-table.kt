package i.server.modules.alarm.model

import i.server.modules.client.model.ClientTable
import i.server.modules.message.model.MessageSendTargetTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * 异常报警通知规则
 */
object AlarmRoleTable : IdTable<String>("t_alarm_role") {
    override val id: Column<EntityID<String>> = varchar("id", 64).uniqueIndex().entityId()
    val description = varchar("description", 128)

    override val primaryKey = PrimaryKey(id)
}

/**
 * 异常指标规则
 */
object AlarmRoleMonitorTable : IntIdTable("t_alarm_monitor") {
    val duration = AlarmRoleTable.long("duration_time")
}

object AlarmRoleBindTable : IntIdTable("t_alarm_role_bind_client") {
    val alarmRole = reference("alarm_role", AlarmRoleTable)
    val client = reference("client", ClientTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    val notificationTarget = reference("notification_target", MessageSendTargetTable)
}
