package i.server.utils.interpreter

interface RuleDataBuilder<R : RuleData> {
    fun verify(data: String): Boolean
    fun build(data: String): R
}
