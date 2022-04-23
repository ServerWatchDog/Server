package i.server.modules.alarm.service.impl

import i.server.modules.alarm.model.AlarmRuleResultView
import i.server.modules.alarm.model.AlarmRuleTable
import i.server.modules.alarm.model.AlarmRuleView
import i.server.modules.alarm.model.InternalSupportVarsResultView
import i.server.modules.alarm.model.SupportVarsResultView
import i.server.modules.alarm.service.IAlarmRuleService
import i.server.modules.monitor.model.MonitorType
import i.server.modules.monitor.service.IMonitorTypeService
import i.server.utils.BadRequestException
import i.server.utils.interpreter.RuleCompiler
import i.server.utils.interpreter.RuleDataType
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.LinkedList

@Service
class AlarmRuleServiceImpl(
    private val monitorTypeService: IMonitorTypeService
) :
    IAlarmRuleService,
    CRUDServiceImpl<AlarmRuleView, AlarmRuleResultView, Int, AlarmRuleTable> {
    override val table = AlarmRuleTable

    override fun supportVars(): List<SupportVarsResultView> {
        return internalSupportVars().map { SupportVarsResultView(it.id, it.name, it.type.description, it.description) }
    }

    fun internalSupportVars(): List<InternalSupportVarsResultView> {
        val list = LinkedList<InternalSupportVarsResultView>()
        list.addAll(
            monitorTypeService.getAll()
                .flatMap {
                    listOf(
                        InternalSupportVarsResultView("monitor.${it.id}", it.name, it.type, it.description),
                        InternalSupportVarsResultView(
                            "monitor.${it.id}.date",
                            "指标 ${it.name} 推送时间",
                            MonitorType.TIME,
                            "指标 ${it.name} 推送的时间."
                        )
                    )
                }
        )
        list.add(InternalSupportVarsResultView("system.time", "系统时间", MonitorType.TIME, "当前系统时间"))
        return list
    }

    override fun insertCheck(input: AlarmRuleView) {
        transaction {
            if (table.select { table.name eq input.name }.count() != 0L) {
                throw BadRequestException("名称不能重复! ")
            }
        }
    }

    override fun dataCheck(input: AlarmRuleView) {
        try {
            val dataTypeMap = internalSupportVars().associate { it.id to it.type.alias }
            val runner = RuleCompiler(input.expression).build().tryExecute(dataTypeMap)
            if (runner != RuleDataType.BOOL) {
                throw BadRequestException("表达式错误，要求最后结果为 BOOL 类型，实际是 $runner 类型.")
            }
        } catch (e: Exception) {
            throw BadRequestException(e.message ?: "")
        }
    }

    override fun AlarmRuleTable.inputToTable(it: UpdateBuilder<Int>, input: AlarmRuleView) {
        it[name] = input.name
        it[expression] = input.expression
    }

    override fun AlarmRuleTable.tableToOutput(it: ResultRow): AlarmRuleResultView {
        return AlarmRuleResultView(it[id].value, it[name], it[expression])
    }
}
