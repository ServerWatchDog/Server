package i.server.utils.template.crud

/**
 * CRUD 返回
 * @property id String ID 代号
 */
interface CRUDResultView<ID : Any> {
    val id: ID
}
