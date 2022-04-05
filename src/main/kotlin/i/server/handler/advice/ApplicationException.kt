package i.server.handler.advice

import org.springframework.http.HttpStatus

open class ApplicationException(val code: HttpStatus, val errorMessage: String) : RuntimeException(errorMessage)
