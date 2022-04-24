package i.server.modules.config

import i.server.modules.user.service.impl.PermissionsServiceImpl
import i.server.modules.user.service.impl.PermissionsServiceImpl.UserAuthorityType

object ConfigAuthority : PermissionsServiceImpl.UserAuthority {
    @UserAuthorityType("全局配置文件编辑权限")
    const val CONFIG_ADMIN = "CONFIG_ADMIN"
}
