package i.server.modules.config.models

import i.server.utils.interpreter.RuleDataType
import org.jetbrains.exposed.dao.id.IdTable

object SoftConfigTable : IdTable<String>("t_config") {
    override val id = varchar("key", 255).entityId()
    val value = varchar("value", 4096).index()
    val description = varchar("description", 2048).default("")
    val defaultValue = varchar("default_value", 4096).default("")
    val internalData = bool("internal_data").default(true)
    val type = enumerationByName("type", 16, RuleDataType::class).default(RuleDataType.TEXT)
    override val primaryKey = PrimaryKey(id)
}
