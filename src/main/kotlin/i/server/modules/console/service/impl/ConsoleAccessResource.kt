package i.server.modules.console.service.impl

import i.server.modules.abac.api.IAccessResource.Attribute
import i.server.modules.abac.api.ObjectAccessResource
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.reflect.KClass

@Service
class ConsoleAccessResource : ObjectAccessResource<ConsoleAccessResource.ConsoleAccept, Unit> {
    enum class ConsoleAccept {
        LOGIN
    }

    override val name = "CONSOLE"
    override val resourceIdType = Unit::class
    override val attributeType = ConsoleAttribute::class
    override val accept = ConsoleAccept::class

    override fun <T : Any> getAttribute(id: Unit, path: String, type: KClass<T>): Optional<T> {
        TODO("Not yet implemented")
    }

    interface ConsoleAttribute : Attribute {
        /**
         * 支持控制台登陆的角色（id）
         */
        val roles: Set<String>
    }
}
