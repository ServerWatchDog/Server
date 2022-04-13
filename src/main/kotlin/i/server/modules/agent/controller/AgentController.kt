package i.server.modules.agent.controller

import i.server.handler.inject.security.Permission
import i.server.handler.inject.security.Session
import i.server.modules.agent.service.IAgentService
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/agent")
class AgentController(private val agentService: IAgentService) {
    @Permission("client")
    @GetMapping("/info")
    fun getInfo(@Session session: ISessionContext) = agentService.getInfo(session)
}
