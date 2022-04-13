package i.server.modules.agent.model

import org.d7z.light.db.modules.session.api.BaseSessionData
import org.d7z.light.db.modules.session.api.ISessionContext

class ClientSessionData(sessionContext: ISessionContext) : BaseSessionData(sessionContext) {
    var clientId: Int by sessionContext.config(Int::class, "client.id")
}
