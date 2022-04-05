package i.server.modules.abac.service.impl

import i.server.modules.abac.api.IAccessResource
import i.server.modules.abac.api.IAccessResource.Attribute
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.reflect.KClass

/**
 * 动作上下文
 */
@Service
class ActionAccessResourceImpl : IAccessResource<Thread> {
    override val name: String = "DEFAULT-ACTION"
    override val resourceIdType = Thread::class
    override val attributeType = ActionAttribute::class

    override fun <T : Any> getAttribute(id: Thread, path: String, type: KClass<T>): Optional<T> {
        TODO("Not yet implemented")
    }

    interface ActionAttribute : Attribute {
        val action: String
    }
}
