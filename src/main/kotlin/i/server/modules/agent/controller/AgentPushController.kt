package i.server.modules.agent.controller

import i.server.handler.inject.security.Permission
import i.server.handler.inject.security.Session
import i.server.modules.agent.model.AgentPushView
import i.server.modules.agent.service.IAgentPushService
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/agent/push")
class AgentPushController(private val service: IAgentPushService) {
    @Permission("client")
    @PostMapping("")
    fun push(@Session session: ISessionContext, @RequestBody push: AgentPushView) = service.push(session, push)
}
