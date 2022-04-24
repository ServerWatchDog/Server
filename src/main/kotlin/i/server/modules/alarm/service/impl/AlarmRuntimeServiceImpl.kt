package i.server.modules.alarm.service.impl

import i.server.modules.alarm.model.InternalSupportVarsResultView
import i.server.modules.alarm.service.IAlarmRuntimeService
import i.server.modules.monitor.service.IMonitorTypeService
import i.server.utils.interpreter.RuleDataType
import org.springframework.stereotype.Service
import java.util.LinkedList

@Service
class AlarmRuntimeServiceImpl(
    private val monitorTypeService: IMonitorTypeService
) : IAlarmRuntimeService {
    override fun supportVars(): List<InternalSupportVarsResultView> {
        val list = LinkedList<InternalSupportVarsResultView>()
        list.addAll(
            monitorTypeService.getAll()
                .flatMap {
                    listOf(
                        InternalSupportVarsResultView("monitor.${it.id}", it.name, it.type, it.description),
                        InternalSupportVarsResultView(
                            "monitor.${it.id}.date",
                            "指标 ${it.name} 推送时间",
                            RuleDataType.TIME,
                            "指标 ${it.name} 推送的时间."
                        )
                    )
                }
        )
        list.add(InternalSupportVarsResultView("system.time", "系统时间", RuleDataType.TIME, "当前系统时间"))
        return list
    }
}
