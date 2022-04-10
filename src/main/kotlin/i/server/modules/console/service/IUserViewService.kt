package i.server.modules.console.service

import i.server.modules.console.model.view.LoginResultView
import i.server.modules.console.model.view.LoginView
import i.server.modules.console.model.view.LogoutResultView
import org.d7z.light.db.modules.session.api.ISessionContext

interface IUserViewService {
    fun login(loginView: LoginView): LoginResultView
    fun logout(userSession: ISessionContext): LogoutResultView
}
