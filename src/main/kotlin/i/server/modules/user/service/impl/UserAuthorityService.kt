package i.server.modules.user.service.impl

import i.server.handler.inject.security.IAuthorityService
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.stereotype.Service

@Service("user")
class UserAuthorityService : IAuthorityService {
    override fun verify(session: ISessionContext, permissions: Array<String>): Boolean {
        TODO("Not yet implemented")
    }
}
