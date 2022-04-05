package i.server.modules.abac.utils

import i.server.modules.abac.api.IAccessResource.Attribute
import i.server.modules.abac.utils.Policy.ValueDataType.* // ktlint-disable no-wildcard-imports
import kotlin.reflect.KClass

/**
 * 规则解析器
 *
 * @property subjectType KClass<out Attribute> 来源对象
 * @property targetType KClass<out Attribute> 目标对象
 * @property actionType KClass<out Attribute> 动作对象
 * @property contextType KClass<out Attribute> 上下文对象
 * @property rule String 规则
 * @constructor
 */
class Policy(
    val subjectType: KClass<out Attribute>,
    val targetType: KClass<out Attribute>,
    val actionType: KClass<out Attribute>,
    val contextType: KClass<out Attribute>,
    val rule: ExecFunction,
) {

    init {
    }

    /**
     * 执行器
     */
    data class ExecFunction(
        /**
         * 第一个变量
         */
        val firstVar: ExecNode,

        /**
         * 第二个变量
         */
        val secondVar: ExecNode,

        /**
         * 动作
         */
        val step: StepType,
    )

    /**
     * 叶子节点抽象
     */
    interface ExecNode

    /**
     * 函数叶子节点
     */
    data class FunctionExecNode(
        val func: ExecFunction,
    ) : ExecNode

    /**
     * 数据叶子节点
     */
    data class ValueExecNode(
        val value: String,
        val type: ValueType,
    ) : ExecNode

    /**
     * 数据类型
     */
    enum class ValueType {
        CONSTANT, // 常量
        VARIABLE // 变量
    }

    enum class ValueDataType {
        NUMBER, COLLECTION, TEXT, BOOL, NULL
    }

    enum class StepType(
        val size: Int,
        val resultVar: ValueDataType,
        val firstVar: Array<ValueDataType>,
        val secondVar: Array<ValueDataType> = arrayOf(),
    ) {
        OR(1, BOOL, arrayOf(BOOL)), // 或者
        AND(1, BOOL, arrayOf(BOOL)), // 取并
        EXISTS(1, BOOL, ValueDataType.values()), // 是否存在或者为空
        EQ(2, BOOL, ValueDataType.values(), ValueDataType.values()), // 是否相等
        NOT(1, BOOL, arrayOf(BOOL)), // 取反
        ADD(2, NUMBER, arrayOf(NUMBER), arrayOf(NUMBER)), // 相加
        SUB(2, NUMBER, arrayOf(NUMBER), arrayOf(NUMBER)), // 相减
        MUL(2, NUMBER, arrayOf(NUMBER), arrayOf(NUMBER)), // 相乘
        DIV(2, NUMBER, arrayOf(NUMBER), arrayOf(NUMBER)), // 相除
        REM(2, NUMBER, arrayOf(NUMBER), arrayOf(NUMBER)), // 取余
        CONTAIN(2, BOOL, arrayOf(COLLECTION), arrayOf(NUMBER)), // 包含
        JOIN(2, BOOL, arrayOf(COLLECTION), arrayOf(COLLECTION)), // 相交
    }
}
