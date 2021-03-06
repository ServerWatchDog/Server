package i.server.modules.monitor.model

import i.server.modules.client.model.MiniClientResultView
import i.server.utils.interpreter.RuleDataType
import i.server.utils.template.crud.CRUDResultView

data class MonitorTypeGroupView(
    val name: String,
    val description: String,
)

data class MiniMonitorTypeGroupResultView(
    val id: Int,
    val name: String,
    val description: String,
)

data class MonitorTypeGroupResultView(
    override val id: Int,
    val name: String,
    val description: String,
    val monitorTypes: List<MiniMonitorTypeResultView>,
) : CRUDResultView<Int>

data class MiniMonitorTypeResultView(
    val id: String,
    val name: String,
)

data class MonitorTypeView(
    val id: String,
    val name: String,
    val description: String,
    val typeGroupId: Int,
    val type: RuleDataType,
)

data class MonitorTypeResultView(
    override val id: String,
    val name: String,
    val description: String,
    val type: RuleDataType,
    val typeGroup: MiniMonitorTypeGroupResultView,
) : CRUDResultView<String>

data class MiniMonitorTypeResultView2(
    val id: String,
    val name: String,
    val description: String,
    val type: RuleDataType,
)

data class ClientMonitorTypeView(
    val clientId: Int,
    val typesId: List<String>,
)

data class ClientMonitorGroupView(
    val clientId: Int,
    val groupsId: List<Int>,
)

data class ClientMonitorTypeResultView(
    val client: MiniClientResultView,
    val monitorType: List<MiniMonitorTypeResultView>,
)

data class MonitorValueTypeResultView(
    val id: String,
    val description: String,
)
