package i.server.modules.client.controller

import i.server.handler.inject.security.Permission
import i.server.modules.client.ClientAuthority
import i.server.modules.client.model.ClientResultView
import i.server.modules.client.model.ClientView
import i.server.modules.client.service.IClientService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [ClientAuthority.CLIENT_ADMIN])
@RequestMapping("/api/admin/clients")
@RestController
class ClientController(service: IClientService) :
    CRUDController<ClientView, ClientResultView, Int>(service)
