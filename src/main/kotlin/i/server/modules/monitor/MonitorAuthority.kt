package i.server.modules.monitor

import i.server.modules.user.service.impl.PermissionsServiceImpl
import i.server.modules.user.service.impl.PermissionsServiceImpl.UserAuthorityType

object MonitorAuthority : PermissionsServiceImpl.UserAuthority {
    @UserAuthorityType("监控指标管理权限")
    const val MONITOR_TYPE_ADMIN = "MONITOR_TYPE_ADMIN"
}
