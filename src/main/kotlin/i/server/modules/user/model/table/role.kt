package i.server.modules.user.model.table

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object RolesTable : UUIDTable("t_roles") {
    val name = varchar("name", 32)
    val description = varchar("description", 60)
    val defaultRole = bool("default_role")
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

class RoleEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RoleEntity>(RolesTable)

    var name by RolesTable.name
    var description by RolesTable.description
    var defaultRole by RolesTable.defaultRole
    val createTime by RolesTable.createTime
    var updateTime by RolesTable.updateTime
}
