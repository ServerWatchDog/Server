package i.server.modules.client.model

import i.server.modules.user.model.MiniRoleResultView
import i.server.utils.template.crud.TimeCRUDResultView
import java.time.LocalDateTime

data class ClientGroupView(
    val description: String,
    val roleId: Int,
    val clients: List<Int>,
)

data class MinClientGroupResultView(
    val roleName: String,
    val description: String,
)

data class ClientGroupResultView(
    override val id: Int,
    val description: String,
    val role: MiniRoleResultView,
    val clients: List<MiniClientResultView>,
    override val createTime: LocalDateTime,
    override val updateTime: LocalDateTime,

) : TimeCRUDResultView<Int>
