package i.server.modules.user.service.impl

import i.server.handler.inject.security.IAuthorityService
import i.server.modules.user.model.UserSession
import i.server.modules.user.service.IUserService
import i.server.utils.ForbiddenException
import org.d7z.light.db.modules.session.LightSession
import org.d7z.logger4k.core.utils.getLogger
import org.springframework.stereotype.Service

@Service("user")
class UserAuthorityService(
    private val userService: IUserService,
    private val lightSession: LightSession,
) : IAuthorityService {

    private val logger = getLogger()
    override fun verify(sessionId: String, permissions: Array<String>): Boolean {
        return lightSession.findSessionContext(sessionId).orElseThrow {
            logger.debug("Token {} 未找到.", sessionId)
            throw ForbiddenException("Token 已过期.")
        }.let {
            userService.getPermissionsById(UserSession(it).userId).containsAll(permissions.asList())
        }
    }
}
