package i.server.modules.console.controller

import i.server.handler.inject.security.Permission
import i.server.handler.inject.security.Session
import i.server.modules.console.ConsoleAuthority.CONSOLE_LOGIN
import i.server.modules.console.model.LoginView
import i.server.modules.console.service.IUserViewService
import jakarta.validation.Valid
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/view/users")
class LoginController(
    private val userView: IUserViewService,
) {
    @PostMapping("login")
    fun login(@Valid @RequestBody loginView: LoginView) = userView.login(loginView)

    @Permission("user", [CONSOLE_LOGIN])
    @DeleteMapping("logout")
    fun logout(@Session userSession: ISessionContext) = userView.logout(userSession)
}
