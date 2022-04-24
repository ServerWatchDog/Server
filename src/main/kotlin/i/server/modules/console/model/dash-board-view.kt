package i.server.modules.console.model

import i.server.utils.interpreter.RuleDataType
import java.time.LocalDateTime

data class DashBoardResultView(
    val name: String,
    val lastPushDate: LocalDateTime,
    val types: Map<String, MiniMonitorTypeDataResult>,
)

data class MiniMonitorTypeDataResult(
    val name: String,
    val type: RuleDataType,
    val value: String,
)
