package i.server.utils.interpreter

class NumberRuleData private constructor(private val data: Long) : RuleData<Long> {

    override fun <R : RuleData<Long>> calculate(other: R, operator: String): R {
        TODO("Not yet implemented")
    }

    override fun tryCalculate(operator: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun toJVMData(): Long {
        return data
    }

    companion object : RuleDataBuilder<Long, NumberRuleData> {
        override fun verify(data: String): Boolean {
            data.toLongOrNull() ?: return false
            return true
        }

        override fun build(data: String): NumberRuleData {
            return NumberRuleData(data.toLong())
        }

        override fun formJVM(jvmData: Long): RuleData<Long> {
            return NumberRuleData(jvmData)
        }
    }
}

class BooleanRuleData private constructor(private val data: Boolean) : RuleData<Boolean> {

    override fun <R : RuleData<Boolean>> calculate(other: R, operator: String): R {
        TODO("Not yet implemented")
    }

    override fun tryCalculate(operator: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun toJVMData(): Boolean {
        return data
    }

    companion object : RuleDataBuilder<Boolean, BooleanRuleData> {
        override fun verify(data: String): Boolean {
            data.toLongOrNull() ?: return false
            return true
        }

        override fun build(data: String): BooleanRuleData {
            return BooleanRuleData(data.toBoolean())
        }

        override fun formJVM(jvmData: Boolean): RuleData<Boolean> {
            return BooleanRuleData(jvmData)
        }
    }
}
