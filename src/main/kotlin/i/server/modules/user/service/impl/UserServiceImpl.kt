package i.server.modules.user.service.impl

import i.server.modules.user.model.table.RolesTable
import i.server.modules.user.model.table.UserLinkRoleTable
import i.server.modules.user.model.table.UsersTable
import i.server.modules.user.model.view.UserResultView
import i.server.modules.user.model.view.UserView
import i.server.modules.user.service.IUserService
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserServiceImpl : IUserService, CRUDServiceImpl<UserView, UserResultView, Int, UsersTable> {
    override val table = UsersTable
    override fun UsersTable.inputToTable(it: UpdateBuilder<Int>, input: UserView) {
        it[table.email] = input.email
        it[table.phone] = input.phone
        it[table.name] = input.name
        if (input.password.isNotEmpty()) {
            it[table.password] = input.password
        }
        it[table.twoFactor] = input.twoFactor
    }

    override fun UsersTable.tableToOutput(it: ResultRow): UserResultView {
        val map = UserLinkRoleTable.leftJoin(RolesTable).select { UserLinkRoleTable.user eq it[UsersTable.id].value }
            .map { item -> item[RolesTable.name] }
        return UserResultView(
            it[id].value,
            it[name],
            it[email],
            it[phone],
            it[createTime],
            it[updateTime],
            map
        )
    }
}
