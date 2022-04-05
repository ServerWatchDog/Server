package i.server.modules.user.model.table

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.UUID

object PermissionsTable : UUIDTable("t_permissions") {
    val name = varchar("name", 32)
    val description = varchar("description", 60)
}

class PermissionsEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PermissionsEntity>(PermissionsTable)

    var name by PermissionsTable.name
    var description by PermissionsTable.description
}

object PermissionsLinkRoleTable : Table("t_permissions_link_role") {
    val permissions = reference("permissions", PermissionsTable)
    val role = reference("role", RolesTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(permissions, role, name = "pk_permissions_role")
}

class PermissionsLinkRoleEntity(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, PermissionsLinkRoleEntity>(PermissionsTable)

    var permissions by PermissionsEntity via PermissionsTable
    var roles by RoleEntity via RolesTable
    var createTime by PermissionsLinkRoleTable.createTime
}
