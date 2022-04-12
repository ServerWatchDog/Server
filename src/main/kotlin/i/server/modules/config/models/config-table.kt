package i.server.modules.config.models

import org.jetbrains.exposed.sql.Table

object SoftConfigTable : Table("t_config") {
    val key = varchar("key", 255).index()
    val value = varchar("value", 4096).index()
    override val primaryKey = PrimaryKey(key)
}
