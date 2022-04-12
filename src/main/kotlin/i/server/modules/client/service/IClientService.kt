package i.server.modules.client.service

import i.server.modules.client.model.ClientResultView
import i.server.modules.client.model.ClientView
import i.server.utils.template.crud.CRUDService
import java.util.Optional

interface IClientService : CRUDService<ClientView, ClientResultView, Int> {
    fun getClientByToken(sessionId: String): Optional<ClientResultView>
}
