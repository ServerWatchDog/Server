package i.server.modules.abac.service

import i.server.modules.abac.api.IRequestAttributes

/**
 *  授权接口
 */
interface IAccessService {
    /**
     * 权限决策
     *
     * @param subject RequestAttributes 来源
     * @param target RequestAttributes 目标
     * @param action RequestAttributes 动作
     * @param context RequestAttributes 上下文
     * @return Boolean 授权结果
     */
    fun requestPermission(
        subject: IRequestAttributes,
        target: IRequestAttributes,
        action: IRequestAttributes,
        context: IRequestAttributes,
    ): Boolean

}
