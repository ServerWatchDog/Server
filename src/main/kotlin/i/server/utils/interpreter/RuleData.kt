package i.server.utils.interpreter

interface RuleData<T : Any> {
    fun tryCalculate(operator: String): Boolean
    fun <R : RuleData<T>> calculate(other: R, operator: String): R
    fun toJVMData(): T
}
