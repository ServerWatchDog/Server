package i.server.modules.user.service.impl

import i.server.modules.abac.api.IAccessResource.Attribute
import i.server.modules.abac.api.ObjectAccessResource
import i.server.modules.user.service.impl.UserAccessResourceImpl.UserAccept
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.reflect.KClass

@Service
class UserAccessResourceImpl : ObjectAccessResource<UserAccept, String> {
    override val name = "USER"
    override val resourceIdType = String::class
    override val attributeType = UserResource::class
    override val accept = UserAccept::class

    override fun <T : Any> getAttribute(id: String, path: String, type: KClass<T>): Optional<T> {
        TODO("Not yet implemented")
    }

    enum class UserAccept {
        SELECT,
        CREATE,
        UPDATE,
        DELETE
    }

    interface UserResource : Attribute {
        /**
         *  当前用户代号
         */
        val id: String

        /**
         * 当前用户角色组 （id）
         */
        val roles: Set<String>
    }
}
