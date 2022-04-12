package i.server.modules.user.controller

import i.server.handler.inject.security.Permission
import i.server.modules.user.UserAuthority
import i.server.modules.user.model.PermissionsResultView
import i.server.modules.user.service.IPermissionsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/permissions")
class PermissionsController(private val permission: IPermissionsService) {
    @Permission("user", [UserAuthority.USER_ADMIN])
    @GetMapping("")
    fun getPermissions(): List<PermissionsResultView> = permission.getPermissions()
}
