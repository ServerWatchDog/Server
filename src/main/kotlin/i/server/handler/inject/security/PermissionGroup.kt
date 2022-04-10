package i.server.handler.inject.security

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement

/**
 * 拦截请求注入，标记此注解则表明路径受到权限管理器拦截
 */
@Operation(
    security = [SecurityRequirement(name = "bearer")]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionGroup(
    /**
     * 接口标记
     */
    val tag: String,
)
