package i.server.modules.console.service

import i.server.modules.console.model.DashBoardResultView
import i.server.utils.template.PageView
import org.d7z.light.db.modules.session.api.ISessionContext
import org.springframework.data.domain.Pageable

interface IDashboardService {
    fun getClientInfo(session: ISessionContext, page: Pageable): PageView<DashBoardResultView>
}
