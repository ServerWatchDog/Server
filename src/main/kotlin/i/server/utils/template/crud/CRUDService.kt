package i.server.utils.template.crud

import i.server.utils.template.PageView
import i.server.utils.template.SimpleView
import org.springframework.data.domain.Pageable

/**
 * 通用 CRUD 模板
 * @param IN : Any INPUT 数据
 * @param OUT : Any OUTPUT 数据
 * @param ID : Any 主键对象
 */
interface CRUDService<IN : Any, OUT : CRUDResultView<ID>, ID : Comparable<ID>> {
    /**
     * 带分页的查询
     */
    fun select(pageable: Pageable): PageView<OUT>

    /**
     * 插入
     */
    fun insert(input: IN): OUT

    /**
     * 更新
     */
    fun update(id: ID, input: IN): OUT

    /**
     * 删除
     */
    fun delete(id: ID): SimpleView<Boolean>
}
