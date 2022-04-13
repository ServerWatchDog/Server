package i.server.modules.agent.service.impl

import i.server.modules.agent.model.AgentPushResultView
import i.server.modules.agent.model.AgentPushView
import i.server.modules.agent.model.ClientSessionData
import i.server.modules.agent.service.IAgentPushService
import i.server.modules.monitor.service.IClientMonitorTypeService
import org.d7z.light.db.api.LightDB
import org.d7z.light.db.modules.session.api.ISessionContext
import org.d7z.objects.format.api.IDataCovert
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AgentPushServiceImpl(
    lightDB: LightDB,
    private val dataCovert: IDataCovert,
    private val clientMonitorService: IClientMonitorTypeService,
) : IAgentPushService {
    private val clients = lightDB.withMap("client-monitor")
    override fun push(session: ISessionContext, push: AgentPushView): AgentPushResultView {
        val sessionData = ClientSessionData(session)
        var refresh = false
        clients.getOrCreate("client:${sessionData.clientId}", String::class, String::class) {
            "id" to sessionData.clientId.toString()
        }.apply {
            val supportTypes = this["monitor.types"].orElseGet {
                refresh = true
                val joinToString = clientMonitorService.getClientMonitor(sessionData.clientId).joinToString(";")
                this.put("monitor.types", joinToString)
                joinToString
            }.split(";")
            push.data.filter { supportTypes.contains(it.key) }.forEach { (t, u) ->
                this.put("monitor.$t", u)
            }
            this.put("push_time", dataCovert.format(LocalDateTime.now())) // 标记推送时间
        }
        return if (refresh) {
            AgentPushResultView(-1)
        } else {
            AgentPushResultView(0)
        }
    }
}
