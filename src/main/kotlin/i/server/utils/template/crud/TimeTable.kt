package i.server.utils.template.crud

import org.jetbrains.exposed.sql.Column
import java.time.LocalDateTime

interface TimeTable {
    val createTime: Column<LocalDateTime>
    val updateTime: Column<LocalDateTime>
}
