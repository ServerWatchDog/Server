package i.server.modules.console.service.impl

import i.server.modules.console.model.view.LoginResultView
import i.server.modules.console.model.view.LoginView
import i.server.modules.console.service.IUserViewService
import org.springframework.stereotype.Service

@Service
class UserViewImpl : IUserViewService {
    override fun login(loginView: LoginView): LoginResultView {
        TODO("Not yet implemented")
    }
}
