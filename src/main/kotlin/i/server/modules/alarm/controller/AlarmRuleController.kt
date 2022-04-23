package i.server.modules.alarm.controller

import i.server.handler.inject.security.Permission
import i.server.modules.alarm.AlarmAuthority
import i.server.modules.alarm.model.AlarmRuleResultView
import i.server.modules.alarm.model.AlarmRuleView
import i.server.modules.alarm.service.IAlarmRuleService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [AlarmAuthority.ALARM_ADMIN])
@RestController
@RequestMapping("/api/admin/alarm/rules")
class AlarmRuleController(private val service: IAlarmRuleService) :
    CRUDController<AlarmRuleView, AlarmRuleResultView, Int>(
        service
    ) {
    @GetMapping("/vars")
    fun supportVars() = service.supportVars()
}
