package i.server.modules.agent.model

import i.server.modules.monitor.model.MonitorTypeTable
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table

object CustomMonitorScriptTable : IdTable<String>("t_agent_custom_script") {
    override val id = reference("id", MonitorTypeTable)
    val name = varchar("name", 128).uniqueIndex()
    val type = enumerationByName("type", 16, CustomScriptType::class)
    val script = text("script")
    override val primaryKey = PrimaryKey(id, type, name)
}

enum class CustomScriptType {
    BASH_SHELL, POWER_SHELL, CMD_LINE, PYTHON, JAVA
}

object CustomMonitorScriptLabelTable : Table("t_monitor_custom_script_label") {
    val label = varchar("label", 64).uniqueIndex()
    val script = reference("script", CustomMonitorScriptTable)
    override val primaryKey = PrimaryKey(label, script)
}
