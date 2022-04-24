package i.server.modules.alarm.service

import i.server.modules.alarm.model.AlarmRuleResultView
import i.server.modules.alarm.model.AlarmRuleView
import i.server.modules.alarm.model.SupportVarsResultView
import i.server.utils.template.SimpleView
import i.server.utils.template.crud.CRUDService

interface IAlarmRuleService : CRUDService<AlarmRuleView, AlarmRuleResultView, Int> {
    fun supportVars(): List<SupportVarsResultView>
    fun checkRule(data: SimpleView<String>): SimpleView<String>
}
