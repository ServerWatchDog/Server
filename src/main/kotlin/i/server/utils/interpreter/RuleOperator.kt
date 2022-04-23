package i.server.utils.interpreter

import i.server.utils.interpreter.RuleCompiler.ConstExecuteVariable
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class RuleOperator(val opera: Array<String>, val types: Array<RuleDataType>, val execute: Execute) {
    AND(arrayOf("&", " and ", " AND "), arrayOf(RuleDataType.BOOL), OrAndExecute(false)),
    OR(arrayOf("|", " or ", " OR "), arrayOf(RuleDataType.BOOL), OrAndExecute(true)),
    GREATER_EQ(arrayOf(">="), arrayOf(RuleDataType.NUMBER, RuleDataType.TIME), CompareExecute(">=")),
    LESS_EQ(arrayOf("<="), arrayOf(RuleDataType.NUMBER, RuleDataType.TIME), CompareExecute("<=")),
    EQ(arrayOf("="), RuleDataType.values(), CompareExecute("=")),
    GREATER(arrayOf(">"), arrayOf(RuleDataType.NUMBER, RuleDataType.TIME), CompareExecute(">")),
    LESS(arrayOf("<"), arrayOf(RuleDataType.NUMBER, RuleDataType.TIME), CompareExecute("<")),
    ADD(arrayOf("+"), arrayOf(RuleDataType.NUMBER, RuleDataType.TIME, RuleDataType.TEXT), CalculateExecute("+")),
    MINUS(arrayOf("-"), arrayOf(RuleDataType.NUMBER), CalculateExecute("-")),
    TIMES(arrayOf("*"), arrayOf(RuleDataType.NUMBER, RuleDataType.TIME), CalculateExecute("*")),
    DIV(arrayOf("/"), arrayOf(RuleDataType.NUMBER), CalculateExecute("/")),
    REM(arrayOf("%"), arrayOf(RuleDataType.NUMBER), CalculateExecute("%")),
    COPY_LEFT(emptyArray(), RuleDataType.values(), CopyLeft());

    interface Execute {
        /**
         * 尝试执行获取返回对象类型
         */
        fun tryExecute(dataType: RuleDataType): RuleDataType

        /**
         * 执行并返回结果
         */
        fun execute(first: String, second: String, type: RuleDataType): ConstExecuteVariable
    }

    class CopyLeft : Execute {
        override fun tryExecute(dataType: RuleDataType): RuleDataType {
            return dataType
        }

        override fun execute(first: String, second: String, type: RuleDataType): ConstExecuteVariable {
            return ConstExecuteVariable(first, type)
        }
    }

    companion object {
        val dataFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    }

    class OrAndExecute(private val isOr: Boolean) : Execute {
        override fun tryExecute(dataType: RuleDataType): RuleDataType {
            if (dataType != RuleDataType.BOOL) {
                throw RuleBuildException("不支持 $dataType 类型.")
            }
            return RuleDataType.BOOL
        }

        override fun execute(first: String, second: String, type: RuleDataType): ConstExecuteVariable {
            return if (isOr) {
                ConstExecuteVariable((first.toBoolean() || second.toBoolean()).toString(), RuleDataType.BOOL)
            } else {
                ConstExecuteVariable((first.toBoolean() && second.toBoolean()).toString(), RuleDataType.BOOL)
            }
        }
    }

    class CompareExecute(private val func: String) : Execute {
        override fun tryExecute(dataType: RuleDataType): RuleDataType {
            if (func in arrayOf(">=", ">", "<=", "<") && dataType !in arrayOf(
                    RuleDataType.NUMBER,
                    RuleDataType.TIME,
                    EQ
                )
            ) {
                throw RuleBuildException("表达式 $func 不支持 $dataType 类型.")
            }
            return RuleDataType.BOOL
        }

        override fun execute(first: String, second: String, type: RuleDataType): ConstExecuteVariable {
            when (type) {
                RuleDataType.NUMBER -> {
                    val firstDec = BigDecimal(first)
                    val secondDec = BigDecimal(second)
                    return when (func) {
                        ">=" -> firstDec.compareTo(secondDec) in arrayOf(0, 1)
                        ">" -> firstDec.compareTo(secondDec) == 1
                        "<=" -> firstDec.compareTo(secondDec) in arrayOf(0, -1)
                        "<" -> firstDec.compareTo(secondDec) == -1
                        "=" -> firstDec.compareTo(secondDec) == 0
                        else -> throw RuleBuildException("不支持表达式 $func.")
                    }.let { ConstExecuteVariable(it.toString(), RuleDataType.BOOL) }
                }
                RuleDataType.TIME -> {
                    val firstTime = LocalDateTime.parse(first, dataFormat)
                    val secondTime = LocalDateTime.parse(second, dataFormat)
                    return when (func) {
                        ">=" -> firstTime.isAfter(secondTime) && firstTime.isEqual(secondTime)
                        ">" -> firstTime.isAfter(secondTime)
                        "<=" -> firstTime.isBefore(secondTime) && firstTime.isEqual(secondTime)
                        "<" -> firstTime.isBefore(secondTime)
                        "=" -> firstTime.isEqual(secondTime)
                        else -> throw RuleBuildException("不支持表达式 $func.")
                    }.let { ConstExecuteVariable(it.toString(), RuleDataType.BOOL) }
                }
                else -> {
                    if (func == "=") {
                        return ConstExecuteVariable((first == second).toString(), RuleDataType.BOOL)
                    } else {
                        throw RuleBuildException("类型 $type 不支持表达式 $func.")
                    }
                }
            }
        }
    }

    class CalculateExecute(private val func: String) : Execute {
        override fun tryExecute(dataType: RuleDataType): RuleDataType {
            if (func == "+" && dataType !in arrayOf(RuleDataType.NUMBER, RuleDataType.TEXT) ||
                func == "-" && dataType !in arrayOf(RuleDataType.NUMBER, RuleDataType.TIME, RuleDataType.TIME) ||
                func in arrayOf("*", "/", "%") && dataType !in arrayOf(RuleDataType.NUMBER)
            ) {
                throw RuleBuildException("表达式 $func 不支持 $dataType 类型.")
            }
            if (dataType == RuleDataType.TIME) {
                return RuleDataType.NUMBER
            }
            return dataType
        }

        override fun execute(first: String, second: String, type: RuleDataType): ConstExecuteVariable {
            return when (func) {
                "+" -> {
                    when (type) {
                        RuleDataType.NUMBER -> BigDecimal(first).plus(BigDecimal(second)).toPlainString()
                        RuleDataType.TEXT -> first + second
                        else -> throw RuleBuildException("表达式 $func 不支持 $type 类型.")
                    }.let { ConstExecuteVariable(it, type) }
                }
                "-" -> {
                    when (type) {
                        RuleDataType.NUMBER -> ConstExecuteVariable(
                            BigDecimal(first).minus(BigDecimal(second)).toPlainString(), RuleDataType.NUMBER
                        )
                        RuleDataType.TEXT -> ConstExecuteVariable(first + second, RuleDataType.TEXT)
                        RuleDataType.TIME -> ConstExecuteVariable(
                            Duration.between(
                                LocalDateTime.parse(first, dataFormat),
                                LocalDateTime.parse(second, dataFormat)
                            ).toSeconds().toString(),
                            RuleDataType.NUMBER
                        )
                        else -> throw RuleBuildException("表达式 $func 不支持 $type 类型.")
                    }
                }
                "*", "/", "%" -> {
                    if (type == RuleDataType.NUMBER) {
                        when (func) {
                            "*" -> BigDecimal(first).times(BigDecimal(second)).toPlainString()
                            "/" -> BigDecimal(first).div(BigDecimal(second)).toPlainString()
                            "%" -> BigDecimal(first).rem(BigDecimal(second)).toPlainString()
                            else -> throw RuleBuildException("表达式 $func 不支持 $type 类型.")
                        }.let { ConstExecuteVariable(it, RuleDataType.NUMBER) }
                    } else {
                        throw RuleBuildException("表达式 $func 不支持 $type 类型.")
                    }
                }
                else -> throw RuleBuildException("表达式 $func 不支持 $type 类型.")
            }
        }
    }
}
