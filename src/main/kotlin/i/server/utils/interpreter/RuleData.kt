package i.server.utils.interpreter

interface RuleData {
    val type: RuleDataType
    fun <T : RuleData> calculate(other: T, operator: String): T
}
