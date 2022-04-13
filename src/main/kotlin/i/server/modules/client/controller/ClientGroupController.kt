package i.server.modules.client.controller

import i.server.handler.inject.security.Permission
import i.server.modules.client.ClientAuthority
import i.server.modules.client.model.ClientGroupResultView
import i.server.modules.client.model.ClientGroupView
import i.server.modules.client.service.IClientGroupService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [ClientAuthority.CLIENT_GROUP_ADMIN])
@RequestMapping("/api/admin/client/groups")
@RestController
class ClientGroupController(service: IClientGroupService) : CRUDController<ClientGroupView, ClientGroupResultView, Int>(
    service
)
