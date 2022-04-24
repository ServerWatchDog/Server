package i.server.modules.agent.model

import i.server.utils.interpreter.RuleDataType

data class AgentInfoResultView(
    val refreshTime: Long,
    val pushType: Map<String, RuleDataType>,
)

data class AgentPushView(
    val data: Map<String, String>,
)

data class AgentPushResultView(
    val status: Int,
)
