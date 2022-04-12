package i.server.modules.message.model

import org.jetbrains.exposed.dao.id.IntIdTable

object MessageSendTargetTable : IntIdTable("t_message_target") {
    val name = varchar("name", 64)
    val address = varchar("address", 255)
    val title = varchar("title", 120)
    val template = varchar("template", 2048)
}
