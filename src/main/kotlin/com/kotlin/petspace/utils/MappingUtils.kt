package com.kotlin.petspace.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies


val modelMapper: ModelMapper = modelMapper()
val mapper: ObjectMapper = mapper()

private fun modelMapper(): ModelMapper {
    val modelMapper = ModelMapper()
    modelMapper.configuration.matchingStrategy = MatchingStrategies.LOOSE
    return modelMapper
}

fun <T, X> toSimpleDTO(fromObject: X, toClass: Class<T>): T {
    return modelMapper.map(fromObject, toClass)
}

fun <T, X> toListSimpleDTO(fromList: List<X>, expectedType: Class<T>): List<T> {
    return fromList.map { toSimpleDTO(it, expectedType) }
}

private fun mapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    return objectMapper
}

fun <T> fromJson(json: String, expectedType: Class<T>): T {
    return mapper.readValue(json, expectedType)
}

fun toJson(any: Any): String {
    return mapper.writeValueAsString(any)
}

fun <T> fromJsonToList(json: String, expectedType: Class<T>): List<T> {
    val type = mapper.typeFactory.constructCollectionType(List::class.java, expectedType)
    return mapper.readValue(json, type)
}