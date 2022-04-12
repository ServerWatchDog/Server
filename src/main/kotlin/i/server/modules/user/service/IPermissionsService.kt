package i.server.modules.user.service

import i.server.modules.user.model.PermissionsResultView

interface IPermissionsService {
    fun getPermissions(): List<PermissionsResultView>
}
