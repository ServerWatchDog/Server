package i.server.modules.alarm.service

import i.server.modules.alarm.model.AlarmGroupResultView
import i.server.modules.alarm.model.AlarmGroupView
import i.server.utils.template.crud.CRUDService

interface IAlarmRuleGroupService : CRUDService<AlarmGroupView, AlarmGroupResultView, Int>
