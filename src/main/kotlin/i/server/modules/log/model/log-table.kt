package i.server.modules.log.model

import i.server.modules.client.model.ClientTable
import i.server.modules.monitor.model.MonitorTypeTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ClientLogTable : LongIdTable("t_log_client_digital") {
    val client = reference("client", ClientTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
}

/**
 * 客户端上报日志
 */
object ClientLogItemTable : LongIdTable("t_log_client_digital_item") {
    val clientLog = reference("log", ClientLogTable)
    val type = reference("client_digital_item_type", MonitorTypeTable)
    val value = varchar("value", 128)
}

/**
 * 客户端报警日志
 */
object ClientAlarmLogTable : LongIdTable("t_log_client_alarm") {
    val client = reference("client", ClientTable).index()
    val type = varchar("type", 64).index()
    val message = varchar("message", 256)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
}

object MessageSendLogTable : IntIdTable("t_log_send_message") {
    val address = varchar("address", 64)
    val title = varchar("title", 120)
    val messsage = varchar("message", 2048)
    val status = bool("status")
    val sendTime = datetime("send_time").defaultExpression(CurrentDateTime())
}

/**
 * 操作日志
 */
object OperationLogTable : LongIdTable("t_log_operation") {
    val name = varchar("name", 64)
    val obj = varchar("object", 64)
    val nessage = varchar("message", 256)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
}
