package i.server.modules.console.model

import i.server.modules.monitor.model.MonitorType
import java.time.LocalDateTime

data class DashBoardResultView(
    val name: String,
    val lastPushDate: LocalDateTime,
    val types: Map<String, MiniMonitorTypeDataResult>,
)

data class MiniMonitorTypeDataResult(
    val name: String,
    val type: MonitorType,
    val value: String,
)
