package i.server.utils.interpreter

import i.server.utils.interpreter.RuleCompiler.*

// ktlint-disable no-wildcard-imports

class RuleRunner(
    private val expressions: List<ExecuteExpression>
) {
    val ruleVariables = expressions.flatMap {
        val listOf = mutableListOf<VariableExecuteVariable>()
        if (it.left is VariableExecuteVariable) {
            listOf.add(it.left)
        }
        if (it.right is VariableExecuteVariable) {
            listOf.add(it.right)
        }
        listOf
    }

    fun tryExecute(variables: Map<String, RuleDataType>): RuleDataType {
        val copy = expressions.map { it.copy() }.toMutableList()
        val typeMap = LinkedHashMap<Int, RuleDataType>()
        fun ExecuteExpression.loadOnesType(
            master: VariableExecuteVariable,
            other: IExecuteVariable
        ) {
            val masterType = variables[master.name] ?: throw RuleBuildException("没有变量 $master.")
            val otherType = when (other) {
                is ContextExecuteVariable -> typeMap[other.lastId]
                    ?: throw RuleBuildException("未知错误")
                is DataTypeUpdater -> other.type
                else -> throw RuleBuildException("未知错误")
            }
            if (masterType != otherType) {
                throw RuleBuildException("变量 ${master.name}($masterType) 与 $other 类型不一致.")
            }
            val tryExecute = exp.execute.tryExecute(masterType)
            typeMap[id] = tryExecute
            returnType = tryExecute
        }
        for ((i, v) in copy.withIndex()) {
            if (v.left is VariableExecuteVariable && v.right is VariableExecuteVariable) {
                val left = variables[v.left.name] ?: throw RuleBuildException("没有变量 ${v.left}.")
                val right = variables[v.right.name] ?: throw RuleBuildException("没有变量 ${v.right}.")
                if (left != right) {
                    throw RuleBuildException("变量 ${v.left.name}($left) 与 ${v.right.name}($right) 类型不一致.")
                }
                typeMap[v.id] = v.exp.execute.tryExecute(left)
            } else if (v.left is VariableExecuteVariable) {
                v.loadOnesType(v.left, v.right)
            } else if (v.right is VariableExecuteVariable) {
                v.loadOnesType(v.right, v.left)
            } else if (v.left is ConstExecuteVariable && v.right is ConstExecuteVariable) {
                if (v.left.type != v.right.type) {
                    throw RuleBuildException("变量 ${v.right} 与 ${v.left} 类型不一致.")
                }
                typeMap[v.id] = v.exp.execute.tryExecute(v.left.type)
            }
        }
        return typeMap[copy.last().id]!!
    }

    fun execute(variables: Map<String, Pair<RuleDataType, String>>): Pair<RuleDataType, String> {
        val typeMap = LinkedHashMap<Int, ConstExecuteVariable>()
        var lastResult = ConstExecuteVariable("", RuleDataType.ANY)
        fun IExecuteVariable.value(): ConstExecuteVariable {
            return when (this) {
                is ConstExecuteVariable -> {
                    return this
                }
                is VariableExecuteVariable -> {
                    val data = variables[this.name] ?: throw RuleBuildException("没有变量 ${this.name}.")
                    ConstExecuteVariable(data.second, data.first)
                }
                is ContextExecuteVariable -> {
                    typeMap[this.lastId] ?: throw RuleBuildException("未知错误！无法拿到上下文 #${this.lastId} 数据.")
                }
                else -> throw RuleBuildException("未知错误！")
            }
        }
        for ((i, v) in expressions.withIndex()) {
            val left = v.left.value()
            val right = v.right.value()
            if (left.type != right.type) {
                throw RuleBuildException("表达式 \" $left ${v.exp} $right \" 左右类型不一致，.")
            }
            lastResult = v.exp.execute.execute(left.data, right.data, left.type)
            typeMap[v.id] = lastResult
        }
        return Pair(lastResult.type, lastResult.data)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(
                RuleCompiler(
                    """
                        (0.1 + 0.2 )/3
                    """.trimIndent()
                ).build().execute(mapOf()).second
            )
        }
    }
}
