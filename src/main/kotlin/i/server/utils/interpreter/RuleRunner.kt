package i.server.utils.interpreter

import com.fasterxml.jackson.annotation.JsonIgnore

class RuleRunner(
    private val syntaxTree: RuleCompiler.RuleTree,
    private val expressions: List<RuleCompiler.ExecuteExpression>
) {
    @JsonIgnore
    val variables = getVariable(syntaxTree)

//    fun execute():

    /**
     * 获取变量
     */
    private fun getVariable(
        syntaxTree: RuleCompiler.RuleTree,
        container: MutableSet<String> = hashSetOf()
    ): Set<String> {
        arrayOf(syntaxTree.left, syntaxTree.right).forEach {
            if (it is RuleCompiler.VarRuleTreeData) {
                container.add(it.variable)
            }
            if (it is RuleCompiler.FuncRuleTreeData) {
                getVariable(it.tree, container)
            }
        }
        return container
    }
}
