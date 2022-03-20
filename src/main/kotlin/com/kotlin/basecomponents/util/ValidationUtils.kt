package com.kotlin.basecomponents.util

import com.kotlin.basecomponents.exceptions.InvalidElementException
import java.util.function.*
import javax.validation.ConstraintViolation
import javax.validation.Validation

object ValidationUtils {
    private val validator = Validation.buildDefaultValidatorFactory().validator

    fun <T> validateElement(element: T) {
        val violations = validator.validate(element)
        val errors: MutableMap<String, String> = HashMap()
        violations.forEach(Consumer { v: ConstraintViolation<T> ->
            val errorKey: String = camelToSnake(v.propertyPath.toString())
            errors[errorKey] = v.message
        })
        if (errors.isNotEmpty()) {
            throw InvalidElementException(errors)
        }
    }
}
