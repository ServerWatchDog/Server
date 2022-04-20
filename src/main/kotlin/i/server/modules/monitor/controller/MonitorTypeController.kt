package i.server.modules.monitor.controller

import i.server.handler.inject.security.Permission
import i.server.modules.monitor.MonitorAuthority
import i.server.modules.monitor.model.MonitorTypeResultView
import i.server.modules.monitor.model.MonitorTypeView
import i.server.modules.monitor.service.IMonitorTypeService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [MonitorAuthority.MONITOR_TYPE_ADMIN])
@RestController
@RequestMapping("/api/admin/monitor/types")
class MonitorTypeController(private val service: IMonitorTypeService) :
    CRUDController<MonitorTypeView, MonitorTypeResultView, String>(
        service
    ) {
    @GetMapping("/types")
    fun getTypeList() = service.getTypeList()
}
