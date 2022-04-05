package i.server.modules.abac.api

/**
 * 传输层资源
 *
 * @property type String 资源代号
 * @property data 携带的数据
 */
interface IRequestAttributes {
    val type: String
    val data: Any
}
