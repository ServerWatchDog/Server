package i.server.utils.interpreter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

private val dataFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

enum class RuleDataType(val description: String, val check: (String) -> Boolean) {
    TEXT(
        "文本",
        {
            true
        }
    ), // 文本
    NUMBER(
        "数字",
        {
            Optional.ofNullable(it.toLongOrNull() ?: it.toDoubleOrNull()).isPresent
        }
    ),
    BOOL(
        "布尔",
        {
            Optional.ofNullable(it.toBooleanStrictOrNull()).isPresent
        }
    ),
    TIME(
        "时间",
        {
            try {
                LocalDateTime.parse(it, dataFormat)
                true
            } catch (e: Exception) {
                false
            }
        }
    ),
    ANY(
        "未知类型，不可使用",
        { false }
    ),
}
