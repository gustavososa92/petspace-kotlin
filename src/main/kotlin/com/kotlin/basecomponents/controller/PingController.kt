package com.kotlin.basecomponents.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller responsible for /ping implementation.
 */
@RestController
class PingController {
    /**
     * @return "pong" String.
     */
    @Hidden
    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}