package i.server.modules.console.service

import i.server.modules.console.model.UserInfoResultView
import i.server.modules.user.model.UserSession

interface IConsoleService {
    fun getUserInfo(userSession: UserSession): UserInfoResultView
}
