package i.server.modules.monitor.model

import i.server.modules.client.model.ClientTable
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

/**
 * 监控的指标归组
 */
object MonitorTypeGroupTable : IntIdTable("t_monitor_type_group") {
    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 60).default("")
}

object MonitorTypeTable : IdTable<String>("t_monitor_type") {
    override val id = varchar("id", 64).entityId().uniqueIndex()
    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 60).default("")
    val group = reference("group", MonitorTypeGroupTable)
    val type = enumeration("type", MonitorType::class)
    override val primaryKey = PrimaryKey(id)
}

enum class MonitorType(val description: String) {
    TEXT("文本"), // 文本
    NUMBER("数字"), // 数值
    PERCENTAGE("百分比"), // 百分比
    BOOL("布尔"), // 布尔类型
    TIME("时间"), // 时间类型 （时间戳）
}

/**
 * client 支持的监控指标
 */
object ClientMonitorTypeTable : Table("t_client_monitor_type") {
    val client = reference("client", ClientTable).index()
    val monitorType = reference("monitor_type", MonitorTypeTable)
    override val primaryKey = PrimaryKey(client, monitorType)
}
