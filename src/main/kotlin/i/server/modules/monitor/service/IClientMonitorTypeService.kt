package i.server.modules.monitor.service

import i.server.modules.monitor.model.ClientMonitorGroupView
import i.server.modules.monitor.model.ClientMonitorTypeResultView
import i.server.modules.monitor.model.ClientMonitorTypeView

interface IClientMonitorTypeService {
    fun updateByType(clientMonitor: ClientMonitorTypeView): ClientMonitorTypeResultView
    fun updateByGroup(clientMonitor: ClientMonitorGroupView): ClientMonitorTypeResultView
    fun getClientMonitor(clientId: Int): List<String>
}
