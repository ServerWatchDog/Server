package i.server.modules.user.controller

import i.server.handler.inject.security.Permission
import i.server.modules.user.UserAuthority
import i.server.modules.user.model.RoleResultView
import i.server.modules.user.model.RoleView
import i.server.modules.user.service.IRoleService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [UserAuthority.USER_ADMIN])
@RestController
@RequestMapping("/api/admin/roles")
class RoleController(service: IRoleService) :
    CRUDController<RoleView, RoleResultView, Int>(
        service
    )
