package i.server.modules.alarm.controller

import i.server.handler.inject.security.Permission
import i.server.modules.alarm.AlarmAuthority
import i.server.modules.alarm.model.AlarmGroupResultView
import i.server.modules.alarm.model.AlarmGroupView
import i.server.modules.alarm.service.IAlarmRuleGroupService
import i.server.utils.template.crud.CRUDController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user", [AlarmAuthority.ALARM_ADMIN])
@RestController
@RequestMapping("/api/admin/alarm/rule-groups")
class AlarmRoleGroupController(private val service: IAlarmRuleGroupService) :
    CRUDController<AlarmGroupView, AlarmGroupResultView, Int>(service)
