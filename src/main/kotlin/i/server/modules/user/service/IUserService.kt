package i.server.modules.user.service

import i.server.modules.user.model.UserResultView
import i.server.modules.user.model.UserView
import i.server.utils.template.crud.CRUDService
import kotlin.reflect.KProperty0

interface IUserService : CRUDService<UserView, UserResultView, Int> {
    fun getPermissionsById(userId: Int): Set<String>
    fun hasPermissions(userId: Int, kProperty0: KProperty0<String>): Boolean
}
