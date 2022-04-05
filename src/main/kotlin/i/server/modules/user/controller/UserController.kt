package i.server.modules.user.controller

import i.server.modules.user.model.view.UserResultView
import i.server.modules.user.model.view.UserView
import i.server.modules.user.service.IUserService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/user")
class UserController(service: IUserService) :
    CRUDController<UserView, UserResultView, Int>(
        service
    )
