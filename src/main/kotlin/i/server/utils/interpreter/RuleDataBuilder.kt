package i.server.utils.interpreter

interface RuleDataBuilder<T : Any, R : RuleData<T>> {
    fun verify(data: String): Boolean
    fun build(data: String): R
    fun formJVM(jvmData: T): RuleData<T>
}
