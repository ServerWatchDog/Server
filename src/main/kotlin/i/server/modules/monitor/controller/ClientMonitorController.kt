package i.server.modules.monitor.controller

import i.server.handler.inject.security.Permission
import i.server.modules.client.ClientAuthority
import i.server.modules.monitor.model.ClientMonitorGroupView
import i.server.modules.monitor.model.ClientMonitorTypeResultView
import i.server.modules.monitor.model.ClientMonitorTypeView
import i.server.modules.monitor.service.IClientMonitorTypeService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [ClientAuthority.CLIENT_ADMIN])
@RestController
@RequestMapping("/api/admin/client/monitors")
class ClientMonitorController(private val service: IClientMonitorTypeService) {
    @PostMapping("/{id}/updateType")
    @Permission("user", [ClientAuthority.CLIENT_ADMIN])
    fun updateType(
        @PathVariable("id") id: Int,
        @RequestBody typesId: List<String>,
    ): ClientMonitorTypeResultView {
        return service.updateByType(ClientMonitorTypeView(id, typesId))
    }

    @PostMapping("/{id}/updateGroup")
    @Permission("user", [ClientAuthority.CLIENT_ADMIN])
    fun updateGroup(
        @PathVariable("id") id: Int,
        @RequestBody typesId: List<Int>,
    ): ClientMonitorTypeResultView {
        return service.updateByGroup(ClientMonitorGroupView(id, typesId))
    }
}
