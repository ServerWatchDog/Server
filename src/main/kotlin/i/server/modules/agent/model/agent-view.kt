package i.server.modules.agent.model

import i.server.modules.monitor.model.MonitorType

data class AgentInfoResultView(
    val refreshTime: Long,
    val pushType: Map<String, MonitorType>,
)

data class AgentPushView(
    val data: Map<String, String>,
)

data class AgentPushResultView(
    val status: Int,
)
