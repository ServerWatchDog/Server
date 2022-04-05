package i.server.modules.console.model.view

data class LoginView(
    val account: String,
    val password: String,
)

data class LoginResultView(
    val code: String,
)
