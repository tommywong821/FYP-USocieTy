package ngok3.fyp.backend.util.exception

import ngok3.fyp.backend.util.exception.model.CASException
import ngok3.fyp.backend.util.exception.model.ErrorMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    fun handleCASException(ex: CASException): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "CAS Server Error"
        )
        return ResponseEntity(errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler
    fun handleException(ex: java.lang.Exception): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error"
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}