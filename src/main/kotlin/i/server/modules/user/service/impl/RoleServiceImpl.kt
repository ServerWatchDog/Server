package i.server.modules.user.service.impl

import i.server.modules.user.model.PermissionsLinkRoleTable
import i.server.modules.user.model.PermissionsTable
import i.server.modules.user.model.RoleResultView
import i.server.modules.user.model.RoleView
import i.server.modules.user.model.RolesTable
import i.server.modules.user.model.UserLinkRoleTable
import i.server.modules.user.model.UsersTable
import i.server.modules.user.service.IRoleService
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl : CRUDServiceImpl<RoleView, RoleResultView, Int, RolesTable>, IRoleService {
    override val table = RolesTable
    override fun RolesTable.tableToOutput(it: ResultRow): RoleResultView {
        val users = UserLinkRoleTable.leftJoin(UsersTable).select { UserLinkRoleTable.role eq it[id].value }
            .map { link ->
                RoleResultView.RoleUserResultView(
                    link[UsersTable.id].value,
                    link[UsersTable.name],
                    link[UsersTable.email],
                )
            }
        val permissions = PermissionsLinkRoleTable.leftJoin(PermissionsTable).select {
            PermissionsLinkRoleTable.role eq it[id].value
        }.map { link ->
            link[PermissionsTable.id].value
        }

        return RoleResultView(
            it[table.id].value,
            it[table.name],
            permissions, users,
            it[table.createTime],
            it[table.updateTime]
        )
    }

    override fun RolesTable.inputToTable(it: UpdateBuilder<Int>, input: RoleView) {
        it[name] = input.name
        it[description] = input.description
    }

    override fun ResultRow.insertAfterHook(id: Int, input: RoleView) {
        UserLinkRoleTable.batchInsert(input.users) { data ->
            this[UserLinkRoleTable.user] = data
            this[UserLinkRoleTable.role] = id
        }
        PermissionsLinkRoleTable.batchInsert(input.permissions) { data ->
            this[PermissionsLinkRoleTable.role] = id
            this[PermissionsLinkRoleTable.permissions] = data
        }
    }

    override fun ResultRow.updateAfterHook(id: Int, input: RoleView) {
        if (input.users.isNotEmpty()) {
            UserLinkRoleTable.deleteWhere {
                UserLinkRoleTable.role eq id
            }
        }
        if (input.permissions.isNotEmpty()) {
            PermissionsLinkRoleTable.deleteWhere {
                PermissionsLinkRoleTable.role eq id
            }
        }
        insertAfterHook(id, input)
    }
}
