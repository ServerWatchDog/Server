package i.server.utils

import i.server.handler.advice.ApplicationException
import org.springframework.http.HttpStatus

class BadRequestException(message: String) : ApplicationException(HttpStatus.BAD_REQUEST, message)
