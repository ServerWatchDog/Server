package i.server.utils.interpreter

import java.util.LinkedList

class RuleInterpreter(
    role: String,
) {

    private val syntaxTree: RuleTree

    enum class ItemType {
        VAR, CONST, EXP, FUNC,
    }

    init {
        val grammars = getGrammar(role)
        semanticCheck(grammars)
        syntaxTree = loadSyntaxTree(grammars)
        println(syntaxTree)
        println(getVariable(syntaxTree))
        println(getExecute(syntaxTree).joinToString("\r\n"))
    }

    private fun getExecute(syntaxTree: RuleTree): List<String> {
        var id: Int = 0
        val linkedList: LinkedList<String> = LinkedList()
        fun internalExecute(syntaxTree: RuleTree): Int {
            if (syntaxTree.left !is FuncRuleTreeData && syntaxTree.right !is FuncRuleTreeData) {
                linkedList.addFirst("#${++id} : ${syntaxTree.left} ${syntaxTree.expr} ${syntaxTree.right}")
                return id
            } else if (syntaxTree.left is FuncRuleTreeData && syntaxTree.right is FuncRuleTreeData) {
                val id1 = internalExecute(syntaxTree.left.tree)
                val id2 = internalExecute(syntaxTree.right.tree)
                linkedList.addLast("#${++id} : #$id1 ${syntaxTree.expr} #$id2")
                return id
            } else if (syntaxTree.left is FuncRuleTreeData) {
                val id1 = internalExecute(syntaxTree.left.tree)
                linkedList.addLast("#${++id} : #$id1 ${syntaxTree.expr} ${syntaxTree.right}")
                return id
            } else if (syntaxTree.right is FuncRuleTreeData) {
                val id2 = internalExecute(syntaxTree.right.tree)
                linkedList.addLast("#${++id} : ${syntaxTree.left} ${syntaxTree.expr} #$id2")
                return id
            } else {
                throw RuleBuildException("未知错误")
            }
        }
        internalExecute(syntaxTree)
        return linkedList.sorted()
    }

    private fun getVariable(syntaxTree: RuleTree, container: MutableSet<String> = hashSetOf()): Set<String> {
        arrayOf(syntaxTree.left, syntaxTree.right).forEach {
            if (it is VarRuleTreeData) {
                container.add(it.variable)
            }
            if (it is FuncRuleTreeData) {
                getVariable(it.tree, container)
            }
        }
        return container
    }

    data class RuleTree(
        val left: RuleTreeData,
        val expr: String,
        val right: RuleTreeData,
    ) {
        override fun toString() = "$left $expr $right"
    }

    interface RuleTreeData
    class EmptyRuleTreeData : RuleTreeData {
        override fun toString() = ""
    }

    data class FuncRuleTreeData(val tree: RuleTree) : RuleTreeData {
        override fun toString() = "( $tree )"
    }

    data class VarRuleTreeData(val variable: String) : RuleTreeData {
        override fun toString() = variable
    }

    data class ConstRuleTreeData(val data: String) : RuleTreeData {
        override fun toString() = "\'$data\'"
    }

    private fun <D : Pair<ItemType, Any>> List<D>.covert(): RuleTreeData {
        return if (size == 1) {
            val first = first()
            @Suppress("UNCHECKED_CAST")
            when (first.first) {
                ItemType.VAR -> VarRuleTreeData(first.second as String)
                ItemType.CONST -> ConstRuleTreeData(first.second as String)
                ItemType.FUNC -> {
                    val tree = loadSyntaxTree(first.second as List<Pair<ItemType, Any>>) // 优化嵌套
                    if (tree.left is EmptyRuleTreeData && tree.right !is EmptyRuleTreeData) {
                        tree.right
                    } else if (tree.right is EmptyRuleTreeData && tree.left !is EmptyRuleTreeData) {
                        tree.left
                    } else {
                        FuncRuleTreeData(tree)
                    }
                }
                else -> throw RuleBuildException("未知错误")
            }
        } else {
            val childTree = loadSyntaxTree(this) // 优化嵌套
            if (childTree.left is EmptyRuleTreeData && childTree.right !is EmptyRuleTreeData) {
                childTree.right
            } else if (childTree.right is EmptyRuleTreeData && childTree.left !is EmptyRuleTreeData) {
                childTree.left
            } else {
                FuncRuleTreeData(childTree)
            }
        }
    }

    private fun loadSyntaxTree(grammars: List<Pair<ItemType, Any>>): RuleTree {
        if (grammars.size == 1) {
            return RuleTree(grammars.covert(), "", EmptyRuleTreeData())
        }
        val data = operators.firstNotNullOf { exp ->
            grammars.withIndex().firstOrNull() {
                it.value.first == ItemType.EXP && it.value.second.toString() == exp
            }
        }
        val leftList = grammars.subList(0, data.index).covert()
        val rightList = grammars.subList(data.index + 1, grammars.size).covert()
        return RuleTree(leftList, (data.value.second as String).uppercase(), rightList)
    }

    /**
     * 语法检查器
     */
    @Suppress("UNCHECKED_CAST")
    private fun semanticCheck(grammars: List<Pair<ItemType, Any>>) {
        for (pairs in grammars.windowed(2, 2, false)) { // 语法检查
            if (pairs.first().first !in arrayOf(ItemType.VAR, ItemType.CONST, ItemType.FUNC)) {
                throw RuleBuildException("${pairs.first().second}  应为变量或函数体.")
            }
            if (pairs.last().first != ItemType.EXP) {
                throw RuleBuildException("${pairs.last().second}  应为运算规则.")
            }
        }
        if (grammars.last().first !in arrayOf(ItemType.VAR, ItemType.CONST, ItemType.FUNC)) {
            throw RuleBuildException("${grammars.last().second}  应为变量或函数体.")
        }
        grammars.filter { it.first == ItemType.VAR }.map { it.second }.forEach { // 变量命名检查
            if ((it as String).contains(Regex("^[a-zA-Z][_\\\\.a-zA-Z0-9]+\$")).not()) {
                throw RuleBuildException("变量 $it 不符合命名要求.")
            }
        }
        grammars.filter { it.first == ItemType.FUNC }.map { it.second }.forEach { // 子函数体检查
            semanticCheck(it as List<Pair<ItemType, Any>>)
        }
    }

    /**
     * 获取解析后的语法
     */
    private fun getGrammar(role: String): List<Pair<ItemType, Any>> {
        fun findNextOperator(data: String, offset: Int): IndexedValue<String> {
            var index = offset
            while (index < data.length) {
                val current = data[index]
                for (operator in operators) { // 运算符查找
                    if (operator == current.toString()) {
                        return IndexedValue(index, operator)
                    }
                    if (operator.startsWith(current)) {
                        val opera = data.substring(index, (index + operator.length).coerceAtMost(data.length))
                        if (opera == operator) {
                            return IndexedValue(index, opera)
                        }
                    }
                }
                index++
            }
            return IndexedValue(-1, "")
        }

        val grammar = LinkedList<Pair<ItemType, Any>>()
        if (role.isBlank()) {
            throw RuleBuildException("表达式为空，无法解析")
        }
        var pointer = 0
        while (pointer < role.length) {
            val current = role[pointer]
            if (current == '\'') { // 常量查找
                val last = role.indexOf("\'", pointer + 1, false)
                if (last == -1) {
                    throw RuleBuildException("语法错误，无法找到常量结束符:${role.substring(pointer)}")
                }
                grammar.add(Pair(ItemType.CONST, role.substring(pointer + 1, last)))
                pointer = last + 1
            }
            if (current == '(') { // 函数查找
                val last = role.substring(pointer + 1).let {
                    var leftC = 0
                    for (c in it.withIndex()) {
                        if (c.value == '(') {
                            leftC++
                        }
                        if (c.value == ')') {
                            if (leftC == 0) {
                                return@let c.index + pointer + 1
                            }
                            leftC--
                        }
                    }
                    throw RuleBuildException("语法错误，无法找到结束符')' :${role.substring(pointer)}")
                }
                if (last == -1) {
                    throw RuleBuildException("语法错误，无法找到结束符')' :${role.substring(pointer)}")
                }
                grammar.add(Pair(ItemType.FUNC, getGrammar(role.substring(pointer + 1, last).trim())))
                pointer = last + 1
            }
            if (current in arrayOf(' ', '\r', '\n')) {
                pointer += 1
                continue
            }
            val nextOperator = findNextOperator(role, pointer)
            when (nextOperator.index) {
                -1 -> {
                    val variable = role.substring(pointer).trim()
                    if (variable.isNotBlank()) {
                        grammar.add(Pair(ItemType.VAR, variable))
                    }
                    break
                }
                else -> {
                    val variable = role.substring(pointer, nextOperator.index).trim()
                    if (variable.isNotBlank()) {
                        grammar.add(Pair(ItemType.VAR, variable))
                    }
                    grammar.add(Pair(ItemType.EXP, nextOperator.value))
                    pointer = nextOperator.index + nextOperator.value.length
                }
            }
        }
        return grammar
    }

    companion object {

        private val operators = arrayListOf("OR", "or", "AND", "and", "=", "+", "-", "*", "/", "%")

        @JvmStatic
        fun main(args: Array<String>) {
            RuleInterpreter("(user.id + user.name + '') AND ( asa + ( ((( 'data' * asa ))) + '12' ) )")
        }
    }
}
