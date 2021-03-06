package i.server.handler.advice

import org.d7z.logger4k.core.utils.getLogger
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.http.HttpStatus
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 错误包装
 */
@ControllerAdvice
@RestControllerAdvice
class ErrorControllerAdvice {

    private val logger = getLogger()

    @ExceptionHandler(ApplicationException::class)
    fun applicationAdvice(error: ApplicationException): ErrorResult {
        TransactionManager.currentOrNull()?.rollback()
        if (error.code.is5xxServerError) {
            logger.errorThrowable("发生未知错误.", error)
        } else {
            logger.debugThrowable("发生错误.", error)
        }
        return ErrorResult(error.code, message = error.errorMessage)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionAdvice(error: Exception): ErrorResult {
        TransactionManager.currentOrNull()?.rollback()
        logger.errorThrowable("服务器内部错误.", error)
        return ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, message = "服务器内部错误")
    }

    @ExceptionHandler(ExposedSQLException::class)
    fun exposedSQLExceptionAdvice(error: ExposedSQLException): ErrorResult {
        TransactionManager.currentOrNull()?.rollback()
        logger.warn("数据库出现问题： {}", error.message)
        return ErrorResult(HttpStatus.BAD_REQUEST, message = "数据逻辑不合法.")
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun mediaTypeNotSupportedExceptionAdvice(error: HttpMediaTypeNotSupportedException): ErrorResult {
        TransactionManager.currentOrNull()?.rollback()
        logger.errorThrowable("请求格式错误.", error)
        return ErrorResult(HttpStatus.BAD_REQUEST, message = "请求体不合法.")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun exceptionAdvice(error: MethodArgumentNotValidException): ErrorResult {
        TransactionManager.currentOrNull()?.rollback()
        logger.debugThrowable("格式化异常.", error)
        return ErrorResult(
            HttpStatus.BAD_REQUEST,
            message = error.fieldErrors.joinToString {
                "字段${it.field}${it.defaultMessage ?: ""}"
            }
        )
    }
}
