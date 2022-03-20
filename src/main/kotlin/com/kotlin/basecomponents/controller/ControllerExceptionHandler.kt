package com.kotlin.basecomponents.controller

import com.kotlin.basecomponents.apimodels.ApiError
import com.kotlin.basecomponents.apimodels.ValidationApiError
import com.kotlin.basecomponents.exceptions.BadRequestException
import com.kotlin.basecomponents.exceptions.ConflictException
import com.kotlin.basecomponents.exceptions.ElementNotFoundException
import com.kotlin.basecomponents.exceptions.InvalidElementException
import com.kotlin.basecomponents.exceptions.UnexpectedErrorException
import com.kotlin.basecomponents.exceptions.ValidationException
import com.kotlin.basecomponents.util.camelToSnake
import java.util.*
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException


/**
 * Basic handling for exceptions.
 */
@ControllerAdvice
class ControllerExceptionHandler(val messageSource: MessageSource) {
    @ExceptionHandler(ElementNotFoundException::class)
    fun handleElementNotFound(ex: ElementNotFoundException): ResponseEntity<ApiError> {
        val apiError = ApiError(
            ex.message!!,
            HttpStatus.NOT_FOUND.value()
        )
        return generateError(apiError)
    }

    @ExceptionHandler(UnexpectedErrorException::class)
    fun handleUnexpectedError(ex: UnexpectedErrorException): ResponseEntity<ApiError> {
        val apiError = ApiError(
            String.format(ex.message!!),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return generateError(apiError, ex.cause)
    }

    @ExceptionHandler(InvalidElementException::class)
    fun handleCustomValidationError(ex: InvalidElementException): ResponseEntity<ApiError> {
        val apiError = ValidationApiError(ex.validationErrors)
        return generateError(apiError)
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflictError(ex: ConflictException): ResponseEntity<ApiError> {
        val apiError = ApiError(
            ex.message!!,
            HttpStatus.CONFLICT.value()
        )
        return generateError(apiError)
    }

    /**
     * Handler for not found routes.
     *
     * @param req the incoming request.
     * @return [ResponseEntity] with 404 status code and the route that was not found in the body.
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleRouteNotFound(req: HttpServletRequest): ResponseEntity<ApiError> {
        val apiError = ApiError(
            String.format("Route '%s' not found", req.requestURI),
            HttpStatus.NOT_FOUND.value()
        )
        return generateError(apiError)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiError> {
        val apiError = ApiError(
            String.format("Method '%s' not allowed", e.method),
            HttpStatus.METHOD_NOT_ALLOWED.value()
        )
        return generateError(apiError)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val apiError = ValidationApiError(ex)
        return generateError(apiError)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun validationErrors(e: HttpMessageNotReadableException?): ResponseEntity<ApiError> {
        val apiError = ApiError(INVALID_BODY_DEFAULT_MESSAGE, HttpStatus.BAD_REQUEST.value())
        return generateError(apiError, e)
    }

    //MissingServletRequestParameterException: missing query parameter
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParameter(ex: MissingServletRequestParameterException): ResponseEntity<ApiError> {
        val apiError = ApiError(
            "${camelToSnake(ex.parameterName)}: must not be null",
            HttpStatus.BAD_REQUEST.value()
        )
        return generateError(apiError)
    }

    // BindException: missing internal property of complex query param or complex query param property type miss match
    @ExceptionHandler(BindException::class)
    fun handleBindError(ex: BindException): ResponseEntity<ApiError> {
        val e = ex.fieldError
        val fieldKey: String = camelToSnake(e!!.field)
        var message: String? = null
        for (i in e.codes!!.indices) {
            try {
                message = messageSource.getMessage(e.codes!![i], null, Locale.US)
                if (message != null) {
                    break
                }
            } catch (nsme: NoSuchMessageException) {
                message = e.defaultMessage
            }
        }
        val apiError = ApiError(
            "$fieldKey: $message",
            HttpStatus.BAD_REQUEST.value()
        )
        return generateError(apiError)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        var fieldKey = ex.name
        if (request.getParameter(ex.name) != null) {
            fieldKey = camelToSnake(fieldKey)
        }
        val message = messageSource.getMessage(
            String.format(
                "typeMismatch", ex.requiredType!!
                    .name
            ), null, Locale.US
        )
        val apiError = ApiError(
            "$fieldKey: $message",
            HttpStatus.BAD_REQUEST.value()
        )
        return generateError(apiError)
    }

    @ExceptionHandler(Exception::class)
    fun handleAnythingElse(ex: Exception?): ResponseEntity<ApiError> {
        val apiError = ApiError(
            "Unexpected error",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return generateError(apiError, ex)
    }

    @ExceptionHandler(value = [ValidationException::class])
    fun handleException(e: ValidationException): ResponseEntity<ApiError> {
        val apiError = ApiError(e.message!!, HttpStatus.BAD_REQUEST.value())
        return generateError(apiError, e)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleException(e: BadRequestException): ResponseEntity<ApiError> {
        val apiError = ApiError(e.message!!, HttpStatus.BAD_REQUEST.value())
        return generateError(apiError, e)
    }

    private fun generateError(apiError: ApiError): ResponseEntity<ApiError> {
        return generateError(apiError, null)
    }

    private fun generateError(apiError: ApiError, cause: Throwable?): ResponseEntity<ApiError> {
        if (apiError.status >= 500) {
            LOGGER.error(apiError.toString(), cause)
        } else {
            LOGGER.warn(apiError.toString(), cause)
        }
        return ResponseEntity
            .status(apiError.status)
            .body<ApiError>(apiError)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)
        private const val INVALID_BODY_DEFAULT_MESSAGE = "Invalid body"
    }
}