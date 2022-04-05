package i.server.modules.user.model.table

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object UsersTable : UUIDTable("t_users") {
    val name = varchar("name", 128)
    val phone = varchar("phone", 128)
    val email = varchar("mail", 128)
    val password = varchar("password", 128)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable) {
        var name = UsersTable.name
        var phone = UsersTable.phone
        var email = UsersTable.email
        var passowrd = UsersTable.password
        val createTime = UsersTable.createTime
        var updateTime = UsersTable.updateTime
    }
}
