package i.server.modules.abac.service.impl

import i.server.modules.abac.api.IAccessResource
import i.server.modules.abac.api.IAccessResource.Attribute
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.reflect.KClass

/**
 * 环境上下文
 */
@Service
class ContextAccessResourceImpl : IAccessResource<Unit> {
    override val name: String = "CONTEXT"
    override val resourceIdType = Unit::class
    override val attributeType = ContextAttribute::class

    override fun <T : Any> getAttribute(id: Unit, path: String, type: KClass<T>): Optional<T> {
        TODO("Not yet implemented")
    }

    interface ContextAttribute : Attribute
}
