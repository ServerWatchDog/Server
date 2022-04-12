package i.server.modules.console.service.impl

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil
import i.server.modules.console.ConsoleAuthority
import i.server.modules.console.model.LoginResultView
import i.server.modules.console.model.LoginView
import i.server.modules.console.model.LogoutResultView
import i.server.modules.console.service.IUserViewService
import i.server.modules.user.model.UserSession
import i.server.modules.user.model.UsersTable
import i.server.modules.user.service.IUserService
import i.server.utils.PasswordUtils
import org.d7z.light.db.modules.session.LightSession
import org.d7z.light.db.modules.session.api.ISessionContext
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class UserViewImpl(
    private val userService: IUserService,
    lightSession: LightSession,
) : IUserViewService {
    private val userSessionGroup = lightSession.getSessionGroupContext("user")
    override fun login(loginView: LoginView): LoginResultView {
        val resultRow = UsersTable.select {
            (UsersTable.phone eq loginView.account and UsersTable.phone.neq("")) or
                (UsersTable.email eq loginView.account and UsersTable.email.neq("")) or
                (UsersTable.name eq loginView.account and UsersTable.name.neq(""))
        }.firstOrNull() ?: return LoginResultView(4001, "用户不存在.")
        val userId = resultRow[UsersTable.id].value
        val password = resultRow[UsersTable.password]
        val twoFactor = resultRow[UsersTable.twoFactor]
        if (password.isEmpty()) {
            return LoginResultView(4002, "用户异常，无法登陆.")
        }
        if (twoFactor.isNotEmpty() && loginView.twoFactor.isEmpty()) {
            return LoginResultView(4003, "此账户需要双重验证（2FA）.")
        }
        if (PasswordUtils.hash(loginView.password) != password) {
            return LoginResultView(4004, "用户名或密码错误 .")
        }
        if (twoFactor.isNotEmpty() &&
            TimeBasedOneTimePasswordUtil.generateCurrentNumberString(twoFactor) != loginView.twoFactor
        ) {
            return LoginResultView(4004, "双重验证（2FA）错误.")
        }
        if (userService.hasPermissions(userId, ConsoleAuthority::CONSOLE_LOGIN).not()) {
            return LoginResultView(4005, "此用户不允许登陆.")
        }
        val newSession = userSessionGroup.newSession()
        val userSession = UserSession(newSession)
        userSession.userId = userId
        UsersTable.update {
            it[lastLoginTime] = LocalDateTime.now()
        }
        return LoginResultView(0, newSession.sessionToken)
    }

    override fun logout(userSession: ISessionContext): LogoutResultView {
        userSessionGroup.destroy(userSession.sessionToken)
        return LogoutResultView(0)
    }
}
