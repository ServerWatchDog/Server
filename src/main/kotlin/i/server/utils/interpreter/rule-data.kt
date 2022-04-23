package i.server.utils.interpreter

class NumberRuleData private constructor(private val data: Double) : RuleData<Double> {

    override fun <R : RuleData<Double>> calculate(other: R, operator: String): R {
        TODO("Not yet implemented")
    }

    override fun tryCalculate(operator: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun toJVMData(): Double {
        return data
    }

    companion object : RuleDataBuilder<Double, NumberRuleData> {
        override fun verify(data: String): Boolean {
            data.toDoubleOrNull() ?: return false
            return true
        }

        override fun build(data: String): NumberRuleData {
            return NumberRuleData(data.toDouble())
        }

        override fun formJVM(jvmData: Double): RuleData<Double> {
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

class TextRuleData private constructor(private val data: String) : RuleData<String> {

    override fun <R : RuleData<String>> calculate(other: R, operator: String): R {
        TODO("Not yet implemented")
    }

    override fun tryCalculate(operator: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun toJVMData(): String {
        return data
    }

    companion object : RuleDataBuilder<String, TextRuleData> {
        override fun verify(data: String): Boolean {
            return true
        }

        override fun build(data: String): TextRuleData {
            return TextRuleData(data)
        }

        override fun formJVM(jvmData: String): TextRuleData {
            return TextRuleData(jvmData)
        }
    }
}
