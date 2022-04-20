package i.server.modules.user.model

import i.server.utils.template.crud.TimeCRUDResultView
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
    val role: List<MiniRoleResultView>,

) : TimeCRUDResultView<Int>

data class MiniRoleResultView(
    val id: Int,
    val name: String,
)

data class MiniUserResultView(
    val id: Int,
    val name: String,
    val email: String,
)

data class RoleResultView(
    override val id: Int,
    val name: String,
    val description: String,
    val permissions: List<String>,
    val users: List<MiniUserResultView>,
    override val updateTime: LocalDateTime,
    override val createTime: LocalDateTime,
) : TimeCRUDResultView<Int>

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
