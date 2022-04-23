package i.server.utils.template.crud

import i.server.utils.BadRequestException
import i.server.utils.autoRollback
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

interface CRUDServiceImpl<IN : Any, OUT : CRUDResultView<ID>, ID : Comparable<ID>, TABLE : IdTable<ID>> :
    CRUDService<IN, OUT, ID> {

    val table: TABLE

    fun TABLE.tableToOutput(it: ResultRow): OUT
    fun TABLE.inputToTable(it: UpdateBuilder<Int>, input: IN)

    fun ResultRow.insertAfterHook(id: ID, input: IN) {
    }

    fun ResultRow.updateAfterHook(id: ID, input: IN) {
    }

    fun dataCheck(input: IN) {
    }

    fun insertCheck(input: IN) {
    }

    override fun select(pageable: Pageable): PageView<OUT> = autoRollback {
        table.selectAll().limit(pageable.pageSize, pageable.offset).map {
            table.tableToOutput(it)
        }.let {
            PageView(it, pageable.pageNumber, pageable.pageSize, table.selectAll().count())
        }
    }

    @Transactional
    override fun insert(input: IN): OUT = autoRollback {
        insertCheck(input)
        dataCheck(input)
        table.insertAndGetId {
            inputToTable(it, input)
            if (this is TimeTable) {
                it[this.createTime] = LocalDateTime.now()
                it[this.updateTime] = LocalDateTime.now()
            }
        }.value.let { id ->
            table.select { table.id eq id }.first().let {
                it.insertAfterHook(id, input)
                table.tableToOutput(it)
            }
        }
    }

    @Transactional
    override fun update(id: ID, input: IN): OUT = autoRollback {
        dataCheck(input)
        if (table.select { table.id eq id }.empty()) {
            throw BadRequestException("更新错误！未找到 id 为 $id 的数据.")
        }
        table.update({ table.id.eq(id) }) {
            inputToTable(it, input)
            if (this is TimeTable) {
                it[this.updateTime] = LocalDateTime.now()
            }
        }
        table.select { table.id eq id }.first().let {
            it.updateAfterHook(id, input)
            table.tableToOutput(it)
        }
    }

    @Transactional
    override fun delete(id: ID): SimpleView<Boolean> = autoRollback {
        SimpleView(table.deleteWhere { table.id eq id } != 0)
    }
}
