package i.server.modules.user.model.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PermissionsTable : IdTable<String>("t_permissions") {
    val key = varchar("key", 32).index()
    val description = varchar("description", 60)
    override val primaryKey: PrimaryKey = PrimaryKey(key)
    override val id: Column<EntityID<String>> = key.entityId()
}

object PermissionsLinkRoleTable : Table("t_permissions_link_role") {
    val permissions = reference("permissions", PermissionsTable)
    val role = reference("role", RolesTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(permissions, role, name = "pk_permissions_role")
}
