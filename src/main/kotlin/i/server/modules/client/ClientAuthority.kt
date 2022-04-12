package i.server.modules.client

import i.server.modules.user.service.impl.PermissionsServiceImpl

object ClientAuthority : PermissionsServiceImpl.UserAuthority {
    @PermissionsServiceImpl.UserAuthorityType("客户端管理")
    const val CLIENT_ADMIN = "CLIENT_ADMIN"

    @PermissionsServiceImpl.UserAuthorityType("客户端分组管理")
    const val CLIENT_GROUP_ADMIN = "CLIENT_GROUP_ADMIN"
}
