package i.server.modules.abac.api

import kotlin.reflect.KClass

/**
 * 资源声明
 */
interface ObjectAccessResource<ACCEPT : Enum<ACCEPT>, ID : Any> : IAccessResource<ID> {
    /**
     * 支持的操作
     */
    val accept: KClass<ACCEPT>
}
