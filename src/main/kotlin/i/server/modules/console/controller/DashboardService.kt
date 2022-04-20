package i.server.modules.console.controller

import i.server.handler.inject.page.RestPage
import i.server.handler.inject.security.Permission
import i.server.handler.inject.security.Session
import i.server.modules.console.ConsoleAuthority
import i.server.modules.console.service.IDashboardService
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/view/dashboard")
@Permission("user", [ConsoleAuthority.CONSOLE_LOGIN])
class DashboardService(
    private val dashboard: IDashboardService,
) {
    @GetMapping("/client")
    fun getClientInfo(@Session session: ISessionContext, @RestPage page: Pageable) =
        dashboard.getClientInfo(session, page)
}
