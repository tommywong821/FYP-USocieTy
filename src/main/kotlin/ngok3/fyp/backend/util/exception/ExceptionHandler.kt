package ngok3.fyp.backend.util.exception

import io.jsonwebtoken.MalformedJwtException
import ngok3.fyp.backend.util.exception.model.CASException
import ngok3.fyp.backend.util.exception.model.ErrorMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    fun handleException(ex: DuplicateKeyException): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            "Client Side Error: ${ex.message}"
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleJWTException(ex: MalformedJwtException): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            "Invalid JWT token"
        )
        return ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler
    fun handleCASException(ex: CASException): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "CAS Server Error: ${ex.message}"
        )
        return ResponseEntity(errorMessage, HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler
    fun handleException(ex: AccessDeniedException): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized Access: ${ex.message}"
        )
        return ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler
    fun handleException(ex: java.lang.Exception): ResponseEntity<ErrorMessage> {
        logger.error("handleException: ${ex.message}")
        val errorMessage = ErrorMessage(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error: ${ex.message}"
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}