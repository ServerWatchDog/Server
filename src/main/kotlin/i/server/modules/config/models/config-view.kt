package i.server.modules.config.models

import i.server.utils.interpreter.RuleDataType

data class TableConfigView(
    val key: String,
    val value: String,
    val defaultValue: String = "",
    val description: String = "",
    val isInternalData: Boolean = true,
    val type: RuleDataType = RuleDataType.TEXT
)

data class ConfigView(
    val key: String,
    val value: String,
    val description: String,
    val defaultValue: String,
    val type: RuleDataType
)

data class ConfigUpdateView(
    val key: String,
    val value: String
)
