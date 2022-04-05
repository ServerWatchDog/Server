package i.server.modules.user.model.table

import i.server.utils.template.crud.TimeTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object RolesTable : IntIdTable("t_roles"), TimeTable {
    val name = varchar("name", 32)
    val description = varchar("description", 60)
    val defaultRole = bool("default_role")
    override val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}
