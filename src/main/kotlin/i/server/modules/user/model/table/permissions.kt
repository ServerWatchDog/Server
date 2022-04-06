package i.server.modules.user.model.table

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PermissionsTable : IdTable<String>("t_permissions") {
    override val id = varchar("key", 32).entityId()
    val description = varchar("description", 60).default("")
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}

object PermissionsLinkRoleTable : Table("t_permissions_link_role") {
    val permissions = reference("permissions", PermissionsTable)
    val role = reference("role", RolesTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(permissions, role, name = "pk_permissions_role")
}
