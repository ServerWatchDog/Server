package i.server.modules.console.controller

import i.server.modules.console.model.view.LoginView
import i.server.modules.console.service.IUserViewService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/view/public/login")
class LoginController(
    private val userView: IUserViewService,
) {
    @PostMapping("")
    fun login(@Valid @RequestBody loginView: LoginView) = userView.login(loginView)
}
