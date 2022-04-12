package i.server.modules.user

import i.server.modules.user.service.impl.PermissionsServiceImpl
import i.server.modules.user.service.impl.PermissionsServiceImpl.UserAuthorityType

object UserAuthority : PermissionsServiceImpl.UserAuthority {
    @UserAuthorityType("用户、角色、权限管理")
    const val USER_ADMIN = "USER_ADMIN"
}
