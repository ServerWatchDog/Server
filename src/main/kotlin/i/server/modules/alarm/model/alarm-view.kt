package i.server.modules.alarm.model

import i.server.modules.monitor.model.MonitorType
import i.server.utils.template.crud.CRUDResultView

data class AlarmRuleView(
    val name: String,
    val expression: String
)

data class AlarmRuleResultView(
    override val id: Int,
    val name: String,
    val expression: String,
) : CRUDResultView<Int>

data class MiniRuleVariablesResultView(
    val key: String,
    val type: String
)

data class SupportVarsResultView(
    val id: String,
    val name: String,
    val type: String,
    val description: String
)

data class InternalSupportVarsResultView(
    val id: String,
    val name: String,
    val type: MonitorType,
    val description: String
)
