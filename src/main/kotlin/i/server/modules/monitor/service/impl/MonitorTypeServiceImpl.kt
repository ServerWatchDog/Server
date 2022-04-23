package i.server.modules.monitor.service.impl

import i.server.modules.monitor.model.MiniMonitorTypeGroupResultView
import i.server.modules.monitor.model.MiniMonitorTypeResultView2
import i.server.modules.monitor.model.MonitorType
import i.server.modules.monitor.model.MonitorTypeGroupTable
import i.server.modules.monitor.model.MonitorTypeResultView
import i.server.modules.monitor.model.MonitorTypeTable
import i.server.modules.monitor.model.MonitorTypeView
import i.server.modules.monitor.model.MonitorValueTypeResultView
import i.server.modules.monitor.service.IMonitorTypeService
import i.server.utils.autoRollback
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service

@Service
class MonitorTypeServiceImpl :
    IMonitorTypeService,
    CRUDServiceImpl<MonitorTypeView, MonitorTypeResultView, String, MonitorTypeTable> {
    override val table = MonitorTypeTable

    override fun MonitorTypeTable.tableToOutput(it: ResultRow): MonitorTypeResultView {
        return MonitorTypeResultView(
            it[id].value,
            it[name],
            it[description],
            it[type],
            MonitorTypeGroupTable.select { MonitorTypeGroupTable.id eq it[table.group] }.first().let { item ->
                MiniMonitorTypeGroupResultView(
                    item[MonitorTypeGroupTable.id].value,
                    item[MonitorTypeGroupTable.name],
                    item[MonitorTypeGroupTable.description]
                )
            }
        )
    }

    override fun MonitorTypeTable.inputToTable(it: UpdateBuilder<Int>, input: MonitorTypeView) {
        it[id] = input.id
        it[name] = input.name
        it[description] = input.description
        it[type] = input.type
        it[group] = input.typeGroupId
    }

    override fun getTypeList(): Set<MonitorValueTypeResultView> {
        return MonitorType.values().map { MonitorValueTypeResultView(it.name, it.description) }.toSet()
    }

    override fun getAll(): List<MiniMonitorTypeResultView2> = autoRollback {
        MonitorTypeTable.selectAll().map { item ->
            MiniMonitorTypeResultView2(
                item[table.id].value,
                item[table.name],
                item[table.description],
                item[table.type],
            )
        }
    }
}
