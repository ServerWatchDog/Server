package i.server.modules.client.model

import i.server.modules.user.model.RolesTable
import i.server.utils.template.crud.TimeTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ClientTable : IntIdTable("t_client"), TimeTable {
    val name = varchar("name", 255).uniqueIndex()
    val token = varchar("token", 255).uniqueIndex()
    val enable = bool("enable")
    val description = varchar("description", 60).default("")
    override val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

/**
 * client group
 */
object ClientGroupTable : IntIdTable("t_client_group"), TimeTable {
    val role = reference("user_role", RolesTable) // 链接的角色
    val description = varchar("description", 60).default("")
    override val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    override val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

object ClientLinkGroupTable : Table("t_client_link_client_group") {
    val client = reference("client", ClientTable)
    val group = reference("group", ClientGroupTable)
    override val primaryKey = PrimaryKey(client, group, name = "pk_client_group")
}
