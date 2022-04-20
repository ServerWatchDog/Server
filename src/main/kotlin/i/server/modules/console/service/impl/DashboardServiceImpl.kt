package i.server.modules.console.service.impl

import i.server.modules.console.model.DashBoardResultView
import i.server.modules.console.service.IDashboardService
import i.server.modules.user.model.UserSession
import i.server.utils.template.PageView
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DashboardServiceImpl : IDashboardService {
    override fun getClientInfo(session: ISessionContext, page: Pageable): PageView<DashBoardResultView> {
        val userSession = UserSession(session)
        TODO()
    }
}
