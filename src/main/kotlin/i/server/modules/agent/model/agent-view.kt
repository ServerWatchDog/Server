package i.server.modules.agent.model

data class AgentInfoResultView(
    val refreshTime: Long,
    val pushType: List<String>,
)

data class AgentPushView(
    val data: Map<String, String>,
)

data class AgentPushResultView(
    val status: Int,
)
