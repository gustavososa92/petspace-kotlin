package com.kotlin.petspace.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.kotlin.petspace.model.User
import com.kotlin.petspace.utils.fromJson
import com.kotlin.petspace.utils.toJson
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "user_type")
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = PersonRequestDTO::class, name = "Person"),
        JsonSubTypes.Type(value = InstitutionRequestDTO::class, name = "Institution")
    ]
)
abstract class UserRequestDTO {

    companion object {
        const val TYPE_PERSON = "Person"
        const val TYPE_INSTITUTION = "Institution"
    }

    @NotBlank(message = "Name is mandatory")
    lateinit var name: String

    @Email(message = "Email is mandatory")
    lateinit var userEmail: String

    @NotBlank(message = "Address is mandatory")
    lateinit var address: String

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    @NotNull(message = "Birthdate is mandatory")
    @Past(message = "Invalid date")
    lateinit var birthDate: LocalDate

    @NotBlank(message = "Phone number is mandatory")
    lateinit var phoneNumber: String

    @NotBlank(message = "User type is mandatory")
    lateinit var userType: String

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    lateinit var password: String

    fun toUser(): User {
        val json = toJson(this)
        return fromJson(json, User::class.java)
    }
}

open class PersonRequestDTO() : UserRequestDTO() {

    init {
        userType = TYPE_PERSON
    }

    @NotBlank(message = "Lastname is mandatory")
    lateinit var lastname: String

    @NotBlank(message = "Gender is mandatory")
    lateinit var gender: String
}

open class InstitutionRequestDTO : UserRequestDTO() {

    init {
        userType = TYPE_INSTITUTION
    }

    @NotBlank(message = "Description is mandatory")
    lateinit var description: String
}
