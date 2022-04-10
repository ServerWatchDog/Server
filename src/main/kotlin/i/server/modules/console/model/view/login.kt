package i.server.modules.console.model.view

data class LoginView(
    val account: String,
    val password: String,
    val twoFactor: String = "",
)

data class LoginResultView(
    val code: Int,
    val token: String,
)

data class LogoutResultView(
    val code: Int,
)
