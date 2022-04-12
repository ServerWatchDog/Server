package i.server.modules.user.model

import i.server.utils.template.crud.CRUDResultView
import jakarta.validation.constraints.Email
import java.time.LocalDateTime

data class UserView(
    val name: String,
    @Email val email: String,
    val phone: String,
    val password: String,
    val twoFactor: String,
)

data class UserResultView(
    override val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    override val createTime: LocalDateTime,
    override val updateTime: LocalDateTime,
    val role: List<String>,

) : CRUDResultView<Int>

data class RoleResultView(
    override val id: Int,
    val name: String,
    val permissions: List<String>,
    val users: List<RoleUserResultView>,
    override val updateTime: LocalDateTime,
    override val createTime: LocalDateTime,
) : CRUDResultView<Int> {
    data class RoleUserResultView(
        val id: Int,
        val name: String,
        val email: String,
    )
}

data class RoleView(
    val name: String,
    val description: String,
    val users: List<Int>,
    val permissions: List<String>,
)

data class PermissionsResultView(
    val id: String,
    val description: String,
)
