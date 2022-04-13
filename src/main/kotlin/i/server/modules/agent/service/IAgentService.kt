package i.server.modules.agent.service

import i.server.modules.agent.model.AgentInfoResultView
import org.d7z.light.db.modules.session.api.ISessionContext

interface IAgentService {
    fun getInfo(session: ISessionContext): AgentInfoResultView
}
