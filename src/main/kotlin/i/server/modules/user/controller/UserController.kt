package i.server.modules.user.controller

import i.server.handler.inject.security.Permission
import i.server.modules.user.UserAuthority
import i.server.modules.user.model.UserResultView
import i.server.modules.user.model.UserView
import i.server.modules.user.service.IUserService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [UserAuthority.USER_ADMIN])
@RestController
@RequestMapping("/api/admin/users")
class UserController(service: IUserService) :
    CRUDController<UserView, UserResultView, Int>(
        service
    )
