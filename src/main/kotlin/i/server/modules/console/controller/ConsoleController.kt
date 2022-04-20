package i.server.modules.console.controller

import i.server.handler.inject.security.Permission
import i.server.handler.inject.security.Session
import i.server.modules.console.ConsoleAuthority.CONSOLE_LOGIN
import i.server.modules.console.service.IConsoleService
import i.server.modules.user.model.UserSession
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/view/user")
@Permission("user", [CONSOLE_LOGIN])
class ConsoleController(
    private val consoleService: IConsoleService,
) {
    @Permission("user", [CONSOLE_LOGIN])
    @GetMapping("/info")
    fun info(@Session session: ISessionContext) = consoleService.getUserInfo(UserSession(session))
}
