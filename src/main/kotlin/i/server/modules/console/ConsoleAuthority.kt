package i.server.modules.console

import i.server.modules.user.service.impl.PermissionsServiceImpl

object ConsoleAuthority : PermissionsServiceImpl.Authority {
    const val CONSOLE_LOGIN = "CONSOLE_LOGIN"
}
