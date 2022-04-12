package i.server.modules.console

import i.server.modules.user.service.impl.PermissionsServiceImpl
import i.server.modules.user.service.impl.PermissionsServiceImpl.UserAuthorityType

object ConsoleAuthority : PermissionsServiceImpl.UserAuthority {
    @UserAuthorityType("控制台登陆权限")
    const val CONSOLE_LOGIN = "CONSOLE_LOGIN"
}
