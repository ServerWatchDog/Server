package i.server.modules.agent.service

import i.server.modules.agent.model.AgentPushResultView
import i.server.modules.agent.model.AgentPushView
import org.d7z.light.db.modules.session.api.ISessionContext

interface IAgentPushService {
    fun push(session: ISessionContext, push: AgentPushView): AgentPushResultView
}
