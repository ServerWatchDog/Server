package i.server.modules.user.model.table

import i.server.utils.template.crud.TimeTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : IntIdTable("t_users"), TimeTable {
    val name = varchar("name", 128).uniqueIndex()
    val phone = varchar("phone", 128).uniqueIndex()
    val email = varchar("mail", 128).uniqueIndex()
    val twoFactor = varchar("twoFactor", 128).default("")
    val password = varchar("password", 128)
    val lastLoginTime = datetime("last_login_time").defaultExpression(CurrentDateTime())
    override val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

object UserLinkRoleTable : Table("t_user_link_role") {
    val user = reference("user", UsersTable)
    val role = reference("role", RolesTable)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(user, role, name = "pk_user_role")
}
