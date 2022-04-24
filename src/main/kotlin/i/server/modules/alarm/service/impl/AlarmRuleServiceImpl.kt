package i.server.modules.alarm.service.impl

import i.server.modules.alarm.model.AlarmRuleResultView
import i.server.modules.alarm.model.AlarmRuleTable
import i.server.modules.alarm.model.AlarmRuleView
import i.server.modules.alarm.model.SupportVarsResultView
import i.server.modules.alarm.service.IAlarmRuleService
import i.server.modules.alarm.service.IAlarmRuntimeService
import i.server.utils.BadRequestException
import i.server.utils.interpreter.RuleCompiler
import i.server.utils.interpreter.RuleDataType
import i.server.utils.template.SimpleView
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class AlarmRuleServiceImpl(
    private val alarmRuntime: IAlarmRuntimeService,
) :
    IAlarmRuleService,
    CRUDServiceImpl<AlarmRuleView, AlarmRuleResultView, Int, AlarmRuleTable> {
    override val table = AlarmRuleTable

    override fun supportVars(): List<SupportVarsResultView> {
        return alarmRuntime.supportVars()
            .map { SupportVarsResultView(it.id, it.name, it.type.description, it.description) }
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
            val dataTypeMap = alarmRuntime.supportVars().associate { it.id to it.type }
            val build = RuleCompiler(input.expression).build()
            val returnType = build.tryExecute(dataTypeMap)
            if (returnType != RuleDataType.BOOL) {
                throw BadRequestException("表达式错误，要求最后结果为 BOOL 类型，实际是 $returnType 类型.")
            }
        } catch (e: Exception) {
            throw BadRequestException(e.message ?: throw e)
        }
    }

    override fun checkRule(data: SimpleView<String>): SimpleView<String> {
        val dataTypeMap = alarmRuntime.supportVars().associate { it.id to it.type }
        return try {
            val build = RuleCompiler(data.data).build()
            val returnType = build.tryExecute(dataTypeMap)
            if (returnType != RuleDataType.BOOL) {
                throw BadRequestException("表达式错误，要求最后结果为 BOOL 类型，实际是 $returnType 类型.")
            }
            "表达式没有问题"
        } catch (e: Exception) {
            e.message ?: throw e
        }.let { SimpleView(it) }
    }

    override fun AlarmRuleTable.inputToTable(it: UpdateBuilder<Int>, input: AlarmRuleView) {
        it[name] = input.name
        it[expression] = input.expression
    }

    override fun AlarmRuleTable.tableToOutput(it: ResultRow): AlarmRuleResultView {
        return AlarmRuleResultView(it[id].value, it[name], it[expression])
    }
}
