package i.server.modules.agent.service.impl

import i.server.modules.agent.model.AgentPushResultView
import i.server.modules.agent.model.AgentPushView
import i.server.modules.agent.model.ClientSessionData
import i.server.modules.agent.service.IAgentPushService
import i.server.modules.monitor.service.IClientMonitorTypeService
import i.server.utils.BadRequestException
import i.server.utils.interpreter.RuleDataType
import org.d7z.light.db.api.LightDB
import org.d7z.light.db.modules.session.api.ISessionContext
import org.d7z.objects.format.api.IDataCovert
import org.d7z.objects.format.utils.reduce
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
                val typesCache = dataCovert.format(clientMonitorService.getClientMonitor(sessionData.clientId))
                this.put("monitor.types", typesCache)
                typesCache
            }.let { dataCovert.reduce<Map<String, RuleDataType>>(it) }
            push.data.filter { supportTypes.containsKey(it.key) }.map {
                it.key to (it.value to supportTypes[it.key]!!)
            }.forEach { (t, u) ->
                if (u.second.check(u.first)) {
                    this.put("monitor.$t", u.first)
                    this.put("monitor.$t.date", dataCovert.format(LocalDateTime.now(), LocalDateTime::class))
                } else {
                    throw BadRequestException("消息推送的格式不规范，数据 '${u.first}' 不是 '${u.second} 可用的格式.'")
                }
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
