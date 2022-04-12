package i.server.modules.monitor.model

import i.server.modules.client.model.ClientTable
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * 监控的指标类型
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

enum class MonitorType {
    TEXT,
    NUMBER,
    PERCENTAGE,
    BOOL
}

/**
 * client 支持的监控指标
 */
object ClientMonitorTypeTable : IntIdTable("t_client_monitor_type") {
    val client = reference("client", ClientTable).index()
    val monitorType = reference("monitor_type", MonitorTypeTable)
}
