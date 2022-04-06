package i.server.modules.user

import i.server.modules.user.service.impl.PermissionsServiceImpl

object UserAuthority : PermissionsServiceImpl.Authority {
    const val ADMIN_WRITE = "用户模块写"
    const val ADMIN_READ = "用户模块读"
}
