package com.kotlin.petspace.errors

import com.kotlin.petspace.utils.camelToSnake
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import java.time.LocalDateTime
import java.util.*

open class ApiError(var message: String, var status: Int) {

    var timestamp = LocalDateTime.now()

    var errorId = UUID.randomUUID()

    open fun getDetails(): String = message

    override fun toString(): String {
        return "${getDetails()}. Status: $status. Error ID: $errorId."
    }
}

class ValidationApiError : ApiError {
    var validationErrors: Map<String, String>

    constructor(e: MethodArgumentNotValidException) : super(
        HttpStatus.BAD_REQUEST.reasonPhrase,
        HttpStatus.BAD_REQUEST.value()
    ) {
        validationErrors = generateValidationErrorsMap(e.fieldErrors)
    }

    constructor(validationErrors: Map<String, String>) : super(
        HttpStatus.BAD_REQUEST.reasonPhrase,
        HttpStatus.BAD_REQUEST.value()
    ) {
        this.validationErrors = validationErrors
    }

    private fun generateValidationErrorsMap(errors: List<FieldError>): Map<String, String> {
        val errorMap: MutableMap<String, String> = HashMap()
        errors.forEach { errorMap[camelToSnake(it.field)] = it.defaultMessage!! }
        return errorMap
    }

    override fun getDetails(): String {
        return "$message. Validation errors: $validationErrors"
    }
}