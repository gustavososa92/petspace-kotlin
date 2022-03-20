package com.kotlin.basecomponents.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.kotlin.basecomponents.config.DateFormatConfiguration
import java.text.SimpleDateFormat
import java.util.*
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies


val customModelMapper: ModelMapper = modelMapper()
val customObjectMapper: ObjectMapper = objectMapper()

private fun modelMapper(): ModelMapper {
    val modelMapper = ModelMapper()
    modelMapper.configuration.matchingStrategy = MatchingStrategies.LOOSE
    return modelMapper
}

fun <T, X> toSimpleDTO(fromObject: X, toClass: Class<T>): T {
    return customModelMapper.map(fromObject, toClass)
}

fun <T, X> toListSimpleDTO(fromList: List<X>, expectedType: Class<T>): List<T> {
    return fromList.map { toSimpleDTO(it, expectedType) }
}

private fun objectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    objectMapper.registerModule(createAfterburnerModule())
    objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    val sdf = SimpleDateFormat(DateFormatConfiguration.DATE_FORMAT, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    objectMapper.registerModule(JavaTimeModule())
    objectMapper.dateFormat = sdf
    return objectMapper
}

fun <T> fromJson(json: String, expectedType: Class<T>): T {
    return customObjectMapper.readValue(json, expectedType)
}

fun toJson(any: Any): String {
    return customObjectMapper.writeValueAsString(any)
}

fun <T> fromJsonToList(json: String, expectedType: Class<T>): List<T> {
    val type = customObjectMapper.typeFactory.constructCollectionType(List::class.java, expectedType)
    return customObjectMapper.readValue(json, type)
}

private fun createAfterburnerModule(): AfterburnerModule {
    return AfterburnerModule()
}