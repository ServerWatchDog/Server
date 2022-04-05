package i.server.utils.template.crud

import i.server.modules.user.model.table.UsersTable
import i.server.utils.BadRequestException
import i.server.utils.template.PageView
import i.server.utils.template.SimpleView
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface CRUDServiceImpl<IN : Any, OUT : CRUDResultView<ID>, ID : Comparable<ID>> :
    CRUDService<IN, OUT, ID> {

    val table: IdTable<ID>

    val tableToOutput: IdTable<ID>.(ResultRow) -> OUT

    val inputToTable: IdTable<ID>.(UpdateBuilder<Int>, IN) -> Unit

    @Transactional
    override fun select(pageable: Pageable): PageView<OUT> {
        return table.selectAll().limit(pageable.pageSize, pageable.offset).map {
            tableToOutput(table, it)
        }.let {
            PageView(it, pageable.pageNumber, pageable.pageSize, UsersTable.selectAll().count())
        }
    }

    @Transactional
    override fun insert(input: IN): OUT {
        return table.insertAndGetId {
            inputToTable(this, it, input)
        }.value.let {
            table.select { table.id eq it }.first().let {
                if (this is TimeTable) {
                    it[this.createTime] = LocalDateTime.now()
                    it[this.updateTime] = LocalDateTime.now()
                }
                tableToOutput(table, it)
            }
        }
    }

    @Transactional
    override fun update(id: ID, input: IN): OUT {
        if (table.select { table.id eq id }.empty()) {
            throw BadRequestException("更新错误！未找到 id 为 $id 的数据.")
        }
        table.update({ table.id.eq(id) }) {
            inputToTable(this, it, input)
            if (this is TimeTable) {
                it[this.updateTime] = LocalDateTime.now()
            }
        }
        return table.select { table.id eq id }.first().let {
            tableToOutput(table, it)
        }
    }

    @Transactional
    override fun delete(id: ID): SimpleView<Boolean> {
        return SimpleView(table.deleteWhere { table.id eq id } != 0)
    }
}
