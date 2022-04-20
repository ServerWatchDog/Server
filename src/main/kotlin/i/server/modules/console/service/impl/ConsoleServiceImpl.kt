package i.server.modules.console.service.impl

import i.server.modules.console.model.UserInfoResultView
import i.server.modules.console.service.IConsoleService
import i.server.modules.user.model.RolesTable
import i.server.modules.user.model.UserLinkRoleTable
import i.server.modules.user.model.UserSession
import i.server.modules.user.model.UsersTable
import i.server.utils.EmailImageUtils
import i.server.utils.autoRollback
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Service

@Service
class ConsoleServiceImpl : IConsoleService {
    override fun getUserInfo(userSession: UserSession): UserInfoResultView = autoRollback {
        val first = UsersTable.select { UsersTable.id eq userSession.userId }.first()
        UserInfoResultView(
            first[UsersTable.name],
            first[UsersTable.email],
            EmailImageUtils.getImageUrl(first[UsersTable.email]),
            UserLinkRoleTable.leftJoin(RolesTable).select { UserLinkRoleTable.user eq userSession.userId }
                .joinToString { it[RolesTable.name] }
        )
    }
}
