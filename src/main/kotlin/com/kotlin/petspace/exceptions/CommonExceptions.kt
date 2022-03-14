package com.kotlin.petspace.exceptions


class BadRequestException(message: String) : RuntimeException(message)

class ConflictException(message: String) : RuntimeException(message)

class ElementNotFoundException : RuntimeException {
    constructor() : super(DEFAULT_MESSAGE) {}
    constructor(message: String) : super(message) {}

    companion object {
        private const val DEFAULT_MESSAGE = "Element not found"
    }
}

class InvalidElementException(message: String) : RuntimeException(message) {
    private var validationErrors: Map<String, String> = HashMap()

    constructor() : this(DEFAULT_MESSAGE) {}
    constructor(validationErrors: Map<String, String>) : this(DEFAULT_MESSAGE) {
        this.validationErrors = validationErrors
    }

    constructor(message: String, validationErrors: Map<String, String>) : this(message) {
        this.validationErrors = validationErrors
    }

    companion object {
        private const val DEFAULT_MESSAGE = "Invalid element"
    }
}

class UnexpectedErrorException(message: String, cause: Throwable) : RuntimeException(message, cause)

class ValidationException(message: String) : RuntimeException(message)