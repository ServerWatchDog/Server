package i.server.modules.user.service.impl

import i.server.modules.user.model.table.UsersTable
import i.server.modules.user.model.view.UserResultView
import i.server.modules.user.model.view.UserView
import i.server.modules.user.service.IUserService
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserServiceImpl : IUserService, CRUDServiceImpl<UserView, UserResultView, Int>() {
    override val table = UsersTable
    override val tableToOutput: IdTable<Int>.(ResultRow) -> UserResultView = {
        UserResultView(
            it[UsersTable.id].value,
            it[UsersTable.name],
            it[UsersTable.email],
            it[UsersTable.phone],
            it[UsersTable.createTime],
            it[UsersTable.updateTime],
            emptyList()
        )
    }
    override val inputToTable: IdTable<Int>.(UpdateBuilder<Int>, UserView) -> Unit =
        { it: UpdateBuilder<Int>, userView: UserView ->
            it[table.email] = userView.email
            it[table.phone] = userView.phone
            it[table.name] = userView.name
        }
}
