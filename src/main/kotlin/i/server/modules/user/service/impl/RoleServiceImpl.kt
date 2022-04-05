package i.server.modules.user.service.impl

import i.server.modules.user.model.table.RolesTable
import i.server.modules.user.model.view.RoleResultView
import i.server.modules.user.model.view.RoleView
import i.server.modules.user.service.IRoleService
import i.server.utils.template.crud.CRUDServiceImpl
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl : CRUDServiceImpl<RoleView, RoleResultView, Int>, IRoleService {
    override val table = RolesTable
    override val tableToOutput: IdTable<Int>.(ResultRow) -> RoleResultView = {

        RoleResultView(
            it[table.id].value,
            it[table.name],
            emptyList(), emptyList(),
            it[table.createTime],
            it[table.updateTime]
        )
    }
    override val inputToTable: IdTable<Int>.(UpdateBuilder<Int>, RoleView) -> Unit
        get() = TODO("Not yet implemented")
}
