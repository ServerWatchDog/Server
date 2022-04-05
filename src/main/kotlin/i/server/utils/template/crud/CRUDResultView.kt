package i.server.utils.template.crud

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

/**
 * CRUD 返回
 * @property id String ID 代号
 */
interface CRUDResultView<ID : Any> {
    val id: ID

    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @get:JsonSerialize(using = LocalDateTimeSerializer::class)
    val createTime: LocalDateTime

    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @get:JsonSerialize(using = LocalDateTimeSerializer::class)
    val updateTime: LocalDateTime
}
