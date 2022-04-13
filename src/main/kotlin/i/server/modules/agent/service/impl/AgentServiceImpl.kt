package i.server.modules.agent.service.impl

import i.server.modules.agent.model.AgentInfoResultView
import i.server.modules.agent.model.ClientSessionData
import i.server.modules.agent.service.IAgentService
import i.server.modules.config.services.SoftInfoService
import i.server.modules.monitor.service.IClientMonitorTypeService
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.stereotype.Service

@Service
class AgentServiceImpl(
    private val softInfoService: SoftInfoService,
    private val clientMonitorType: IClientMonitorTypeService,
) : IAgentService {
    override fun getInfo(session: ISessionContext): AgentInfoResultView {
        val clientSessionData = ClientSessionData(session)
        return AgentInfoResultView(
            softInfoService.agentRefreshDate,
            clientMonitorType.getClientMonitor(clientSessionData.clientId)
        )
    }
}
