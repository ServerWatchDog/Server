package i.server.modules.abac.api

import java.util.Optional
import kotlin.reflect.KClass

/**
 * 资源声明
 */
interface IAccessResource<ID : Any> {
    /**
     * 资源代号
     */
    val name: String

    /**
     * 资源 ID 类型
     */
    val resourceIdType: KClass<ID>

    /**
     * 资源属性声明
     */
    val attributeType: KClass<out Attribute>

    /**
     * 获取资源属性
     *
     * 其中，路径类似于 “a.b.c[0].d”
     *
     * @param id ID 资源对应ID
     * @param path String 资源路径
     * @param type KClass<T> 资源路径对应的类型
     * @return Optional<T> 资源结果
     */
    fun <T : Any> getAttribute(id: ID, path: String, type: KClass<T>): Optional<T>

    /**
     * 资源属性声明
     *
     */
    interface Attribute
}
