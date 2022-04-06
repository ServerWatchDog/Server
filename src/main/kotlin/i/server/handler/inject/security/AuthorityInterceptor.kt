package i.server.handler.inject.security

import i.server.utils.ForbiddenException
import i.server.utils.TokenUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.d7z.light.db.modules.session.api.ILightSession
import org.d7z.logger4k.core.utils.getLogger
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 权限拦截器
 */
@Configuration
class AuthorityInterceptor(
    lightSession: ILightSession,

) : HandlerInterceptor {
    val logger = getLogger()
    private val sessions = lightSession.findSessionGroupContext("users")

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        // 通过查询注解来获取权限
        val permission = handler.getMethodAnnotation(Permission::class.java)
            ?: AnnotationUtils.findAnnotation(handler.bean.javaClass, Permission::class.java) ?: return kotlin.run {
            logger.debug("放行路径 {}.", request.pathInfo)
            true
        }

        val token = TokenUtils.decodeToken { header ->
            request.getHeader(header) ?: throw ForbiddenException("需要 Token.")
        }
        val session =
            sessions.querySession(token).orElseThrow {
                logger.debug("Token {} 未找到.", token)
                throw ForbiddenException("Token 已过期.")
            }

        session.refresh()
        if (permission.permissions.isEmpty()) {
            return true
        }
//        if (sessionService.verify(session, permission.permissions).not()) {
        if (true) {
            logger.debug(
                "权限校验问题. 路径：{} 需要 {} 权限.",
                request.contextPath, permission.permissions
            )
            throw ForbiddenException("没有权限！")
        }
        return true
    }
}
