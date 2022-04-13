package i.server.modules.monitor.service.impl

import i.server.modules.monitor.model.MiniMonitorTypeResultView
import i.server.modules.monitor.model.MonitorTypeGroupResultView
import i.server.modules.monitor.model.MonitorTypeGroupTable
import i.server.modules.monitor.model.MonitorTypeGroupView
import i.server.modules.monitor.model.MonitorTypeTable
import i.server.modules.monitor.service.IMonitorTypeGroupService
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service

@Service
class MonitorTypeGroupServiceImpl :
    IMonitorTypeGroupService,
    CRUDServiceImpl<MonitorTypeGroupView, MonitorTypeGroupResultView, Int, MonitorTypeGroupTable> {
    override val table = MonitorTypeGroupTable

    override fun MonitorTypeGroupTable.tableToOutput(it: ResultRow): MonitorTypeGroupResultView {
        return MonitorTypeGroupResultView(
            it[id].value, it[name], it[description],
            MonitorTypeTable.select { MonitorTypeTable.group eq it[id].value }.map { item ->
                MiniMonitorTypeResultView(item[MonitorTypeTable.id].value, item[MonitorTypeTable.name])
            }
        )
    }

    override fun MonitorTypeGroupTable.inputToTable(it: UpdateBuilder<Int>, input: MonitorTypeGroupView) {
        it[name] = input.name
        it[description] = input.description
    }
}
