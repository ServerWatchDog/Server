package i.server.modules.console.model

import i.server.modules.client.model.ClientTable
import org.jetbrains.exposed.dao.id.IntIdTable

object DashBoardClientStatusTable : IntIdTable("t_dash_client_details") {
    val client = reference("client", ClientTable)
    val name = varchar("name", 64)
    val expression = varchar("expression", 255)
}
