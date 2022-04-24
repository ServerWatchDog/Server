package i.server.modules.alarm.model

import i.server.utils.interpreter.RuleDataType
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

data class MiniAlarmRuleResultView(
    val id: Int,
    val name: String
)

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
    val type: RuleDataType,
    val description: String
)

data class AlarmGroupView(
    val name: String,
    val description: String,
    val rules: List<Int>
)

data class AlarmGroupResultView(
    override val id: Int,
    val name: String,
    val description: String,
    val rules: List<MiniAlarmRuleResultView>
) : CRUDResultView<Int>

data class ClientAlarmResultView(
    val id: Int,
    val name: String,
    val rules: List<MiniAlarmRuleResultView>
)
