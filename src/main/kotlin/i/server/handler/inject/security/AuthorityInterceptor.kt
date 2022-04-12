package i.server.handler.inject.security

import i.server.utils.BadRequestException
import i.server.utils.ForbiddenException
import i.server.utils.TokenUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.d7z.light.db.modules.session.api.ILightSession
import org.d7z.light.db.modules.session.api.SessionException
import org.d7z.logger4k.core.utils.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 权限拦截器
 */
@Configuration
class AuthorityInterceptor(
    private val lightSession: ILightSession,
    private val applicationContext: ApplicationContext,

) : HandlerInterceptor {

    @Value("\${debug.skip-auth}")
    private var skipAuth: Boolean = false

    val logger = getLogger()
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod || skipAuth) {
            return true
        }
        // 通过查询注解来获取权限
        val permission = handler.getMethodAnnotation(Permission::class.java)
            ?: AnnotationUtils.findAnnotation(handler.bean.javaClass, Permission::class.java) ?: return kotlin.run {
            logger.debug("放行路径 {}.", request.requestURI)
            true
        }
        val token = TokenUtils.decodeToken { header ->
            request.getHeader(header) ?: throw ForbiddenException("需要 Token.")
        }
        val sessionType = try {
            lightSession.getSessionType(token)
        } catch (e: SessionException) {
            logger.debug("无法解析传入token：{}, 原因：{}.", token, e.message)
            throw BadRequestException("Token 不合法.")
        }
        val tag = permission.tag
        if (sessionType != tag) {
            logger.debug("Token 异常，未知类型：{}，原始token：{}.", sessionType, token)
            throw BadRequestException("Token 不合法.")
        }
        val authService = applicationContext.getBean(sessionType, IAuthorityService::class.java)
        if (authService.verify(token, permission.permissions).not()) {
            logger.debug(
                "Token 权限问题或已经过期. 路径：{} 需要 {} 权限.",
                request.contextPath, permission.permissions
            )
            throw ForbiddenException("token 没有权限或已过期！")
        }
        return true
    }
}
