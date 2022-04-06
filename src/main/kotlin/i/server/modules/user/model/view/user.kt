package i.server.modules.user.model.view

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
