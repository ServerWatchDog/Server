package i.server.modules.user.model.table

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object UserLinkRoleTable : Table("t_user_link_role") {
    val user = reference("user", UsersTable)
    val role = reference("role", RolesTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(user, role, name = "pk_user_role")
}

class UserLinkRoleEntity(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, UserLinkRoleEntity>(UsersTable)

    var users by UserEntity via UsersTable
    var roles by RoleEntity via RolesTable
    var createTime by UserLinkRoleTable.createTime
}
