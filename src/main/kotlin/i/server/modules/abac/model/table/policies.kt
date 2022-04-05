package i.server.modules.abac.model.table

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object PoliciesTable : UUIDTable("t_abac_policies") {
    val subject = varchar("subject", 64).default("").index()
    val target = varchar("target", 64).default("").index()
    val rule = varchar("rule", 4096)
    val action = enumeration("action", PoliciesAction::class)
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime())
    val updateTime = datetime("update_time").defaultExpression(CurrentDateTime())
}

enum class PoliciesAction {
    ACCEPT,
    DROP,
}

class PolicyEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PolicyEntity>(PoliciesTable)

    val subject by PoliciesTable.subject
    val target by PoliciesTable.target
    var rule by PoliciesTable.rule
    var action by PoliciesTable.action
    val createTime by PoliciesTable.createTime
    var updateTime by PoliciesTable.updateTime
}
