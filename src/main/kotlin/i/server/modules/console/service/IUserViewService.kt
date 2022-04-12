package i.server.modules.console.service

import i.server.modules.console.model.LoginResultView
import i.server.modules.console.model.LoginView
import i.server.modules.console.model.LogoutResultView
import org.d7z.light.db.modules.session.api.ISessionContext

interface IUserViewService {
    fun login(loginView: LoginView): LoginResultView
    fun logout(userSession: ISessionContext): LogoutResultView
}
