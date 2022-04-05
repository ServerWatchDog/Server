package i.server.handler.inject.encrypt

import com.fasterxml.jackson.databind.ObjectMapper
import i.server.modules.config.repositories.ConfigRepository
import i.server.modules.config.services.SoftInfoService
import i.server.utils.BadRequestException
import jakarta.annotation.Resource
import org.d7z.logger4k.core.utils.getLogger
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.util.ContentCachingRequestWrapper

/**
 * 会话信息注入
 */
@Component
class EncryptResolver(
    private val infoService: SoftInfoService,
) : HandlerMethodArgumentResolver {
    @Resource
    private lateinit var objectManager: ObjectMapper

    private val logger = getLogger()

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CryptRequestBody::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val nativeRequest = webRequest.getNativeRequest(ContentCachingRequestWrapper::class.java)
            ?: throw BadRequestException("请求体格式不合法.") // TODO: 抛出类型可能错误
        return try {
            if (nativeRequest.contentAsByteArray.isEmpty()) {
                nativeRequest.inputStream.readAllBytes() // InputStream 未被处理
            }
            val data = nativeRequest.contentAsByteArray.toString(Charsets.UTF_8)
            val encryptView = objectManager.readValue(data, EncryptView::class.java)
            val privateKey = infoService.getPrivateKey(encryptView.type)
            val decodeText = privateKey.decodeText(encryptView.cipher)
            // TODO: @Valid 失效
            objectManager.readValue(decodeText, parameter.parameter.type)
        } catch (e: Exception) {
            logger.debug("对请求体解密时发生错误.", e)
            logger.debug("请求体:\r\n{}", nativeRequest.contentAsByteArray.toString(Charsets.UTF_8))
            throw BadRequestException("请求体格式不合法.")
        }
    }
}
