package i.server.modules.message.model

import org.jetbrains.exposed.dao.id.IntIdTable

object MessageSendTargetTable : IntIdTable("t_message_target") {
    val name = varchar("name", 64)
    val address = varchar("address", 255)
    val type = varchar("type", 32)
}
