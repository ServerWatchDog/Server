package i.server.handler.inject.security

import org.d7z.light.db.modules.session.api.ISessionContext

interface IAuthorityService {
    fun verify(session: ISessionContext, permissions: Array<String>): Boolean
}
