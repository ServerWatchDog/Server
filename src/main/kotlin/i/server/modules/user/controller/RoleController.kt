package i.server.modules.user.controller

import i.server.modules.user.model.view.RoleResultView
import i.server.modules.user.model.view.RoleView
import i.server.modules.user.service.IRoleService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/roles")
class RoleController(service: IRoleService) :
    CRUDController<RoleView, RoleResultView, Int>(
        service
    )
