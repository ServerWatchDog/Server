package i.server.modules.alarm

import i.server.modules.user.service.impl.PermissionsServiceImpl.UserAuthority
import i.server.modules.user.service.impl.PermissionsServiceImpl.UserAuthorityType

object AlarmAuthority : UserAuthority {
    @UserAuthorityType("报警规则配置权限")
    const val ALARM_ADMIN = "ALARM_ADMIN"
}
