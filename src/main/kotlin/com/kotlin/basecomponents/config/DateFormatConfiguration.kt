package com.kotlin.basecomponents.config

import java.time.format.DateTimeFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.FormattingConversionService

@Configuration
class DateFormatConfiguration {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    }

    private val registrar = DateTimeFormatterRegistrar()

    fun DateFormatConfiguration() {
        registrar.setDateFormatter(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
        registrar.setTimeFormatter(
            DateTimeFormatter.ofPattern("HH:mm:ss")
        )
        registrar.setDateTimeFormatter(
            DateTimeFormatter.ofPattern(DATE_FORMAT)
        )
    }

    @Autowired
    fun configure(conversionService: FormattingConversionService) {
        registrar.registerFormatters(conversionService)
    }
}
