package i.server.modules.user.model.view

import i.server.utils.template.crud.CRUDResultView
import java.time.LocalDateTime

data class RoleResultView(
    override val id: Int,
    val name: String,
    val permissions: List<String>,
    val users: List<RoleUserResultView>,
    override val updateTime: LocalDateTime,
    override val createTime: LocalDateTime,
) : CRUDResultView<Int> {
    data class RoleUserResultView(
        val id: String,
        val name: String,
        val email: String,
    )
}

data class RoleView(
    val name: String,
    val users: List<Int>,
)
