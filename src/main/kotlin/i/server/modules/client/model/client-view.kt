package i.server.modules.client.model

import i.server.utils.template.crud.TimeCRUDResultView
import java.time.LocalDateTime

data class ClientView(
    val name: String,
    val description: String,
    val refreshToken: Boolean,
    val enable: Boolean,
)

data class MiniClientResultView(
    val id: Int,
    val name: String,
)

data class ClientResultView(
    override val id: Int,
    val name: String,
    val description: String,
    val token: String,
    val enable: Boolean,
    val groups: List<MinClientGroupResultView>,
    override val createTime: LocalDateTime,
    override val updateTime: LocalDateTime,
) : TimeCRUDResultView<Int>
