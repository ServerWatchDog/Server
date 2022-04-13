package i.server.modules.monitor.controller

import i.server.handler.inject.security.Permission
import i.server.modules.monitor.MonitorAuthority.MONITOR_TYPE_ADMIN
import i.server.modules.monitor.model.MonitorTypeGroupResultView
import i.server.modules.monitor.model.MonitorTypeGroupView
import i.server.modules.monitor.service.IMonitorTypeGroupService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [MONITOR_TYPE_ADMIN])
@RestController
@RequestMapping("/api/admin/monitor/groups")
class MonitorTypeGroupController(service: IMonitorTypeGroupService) :
    CRUDController<MonitorTypeGroupView, MonitorTypeGroupResultView, Int>(service)
