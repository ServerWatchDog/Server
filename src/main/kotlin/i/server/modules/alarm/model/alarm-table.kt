package i.server.modules.alarm.model

import i.server.modules.client.model.ClientTable
import i.server.modules.message.model.MessageSendTargetTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object AlarmRoleTable : IntIdTable("t_alarm_role") {
    /**
     * 规则名称
     */
    val name = varchar("name", 64).uniqueIndex()

    /**
     * 规则
     */
    val role = varchar("role", 2048)

    /**
     * 持续时间 (秒)
     */
    val duration = long("duration_time")

    /**
     * 条件触发器目标
     */
    val notificationTarget = reference("notification_target", MessageSendTargetTable)
}

object AlarmRoleBindTable : IntIdTable("t_alarm_role_bind_client") {
    val alarmRole = reference("alarm_role", AlarmRoleTable)
    val client = reference("client", ClientTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
}
