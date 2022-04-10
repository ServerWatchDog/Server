package i.server.modules.user.model

import org.d7z.light.db.modules.session.api.BaseSessionData
import org.d7z.light.db.modules.session.api.ISessionContext

class UserSession(sessionContext: ISessionContext) : BaseSessionData(sessionContext) {
    var userId: Int by sessionContext.config(Int::class)
}
