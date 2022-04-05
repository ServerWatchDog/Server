package i.server.modules.user.model.table

import i.server.utils.template.crud.TimeTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : IntIdTable("t_users"), TimeTable {
    val name = varchar("name", 128)
    val phone = varchar("phone", 128)
    val email = varchar("mail", 128)
    val twoFactor = varchar("twoFactor", 128)
    val password = varchar("password", 128)
    val lastLoginTime = datetime("last_login_time").defaultExpression(CurrentDateTime())
    override val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable) {
        var name = UsersTable.name
        var phone = UsersTable.phone
        var email = UsersTable.email
        var twoFactor = UsersTable.twoFactor
        var passowrd = UsersTable.password
        val createTime = UsersTable.createTime
        val lastLoginTime = UsersTable.lastLoginTime
        var updateTime = UsersTable.updateTime
    }
}

object UserLinkRoleTable : Table("t_user_link_role") {
    val user = reference("user", UsersTable)
    val role = reference("role", RolesTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(user, role, name = "pk_user_role")
}

class UserLinkRoleEntity(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, UserLinkRoleEntity>(UsersTable)

    var users by UserEntity via UsersTable
    var roles by RoleEntity via RolesTable
    var createTime by UserLinkRoleTable.createTime
}
