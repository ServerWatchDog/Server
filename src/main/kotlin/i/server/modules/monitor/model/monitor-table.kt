package i.server.modules.monitor.model

import i.server.modules.client.model.ClientTable
import i.server.utils.interpreter.RuleDataType
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
    val type = enumeration("type", RuleDataType::class)
    override val primaryKey = PrimaryKey(id)
}

// enum class MonitorType(val description: String, val check: TypeCheck, val alias: RuleDataType) {
//    TEXT(
//        "文本",
//        object : TypeCheck {
//            override fun check(data: String) = true
//        },
//        RuleDataType.TEXT
//    ), // 文本
//    NUMBER(
//        "数字",
//        object : TypeCheck {
//            override fun check(data: String): Boolean {
//                data.toLongOrNull() ?: data.toDoubleOrNull() ?: return false
//                return true
//            }
//        },
//        RuleDataType.NUMBER
//    ),
//    BOOL(
//        "布尔",
//        object : TypeCheck {
//            override fun check(data: String): Boolean {
//                data.toBooleanStrictOrNull() ?: return false
//                return true
//            }
//        },
//        RuleDataType.BOOL
//    ), // 布尔类型
//    TIME(
//        "时间",
//        object : TypeCheck {
//            private val dataFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//            override fun check(data: String) = try {
//                LocalDateTime.parse(data, dataFormat)
//                true
//            } catch (e: Exception) {
//                false
//            }
//        },
//        RuleDataType.TIME
//    ), // 时间类型
// }

interface TypeCheck {
    fun check(data: String): Boolean
}

/**
 * client 支持的监控指标
 */
object ClientMonitorTypeTable : Table("t_client_monitor_type") {
    val client = reference("client", ClientTable).index()
    val monitorType = reference("monitor_type", MonitorTypeTable)
    override val primaryKey = PrimaryKey(client, monitorType)
}
