package i.server.modules.alarm.service

import i.server.modules.alarm.model.InternalSupportVarsResultView

interface IAlarmRuntimeService {
    fun supportVars(): List<InternalSupportVarsResultView>
}
