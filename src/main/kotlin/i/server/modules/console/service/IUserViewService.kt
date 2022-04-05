package i.server.modules.console.service

import i.server.modules.console.model.view.LoginResultView
import i.server.modules.console.model.view.LoginView

interface IUserViewService {
    fun login(loginView: LoginView): LoginResultView
}
