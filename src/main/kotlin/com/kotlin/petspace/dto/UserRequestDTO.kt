package com.kotlin.petspace.dto

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

abstract class UserRequestDTO {
    @NotBlank(message = "Debe ingresar el nombre")
    lateinit var name: String

    @Email(message = "Debe ingresar un mail válido")
    lateinit var userEmail: String

    @NotBlank(message = "Debe ingresar una dirección")
    lateinit var address: String

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    @NotNull(message = "Debe ingresar la fecha de nacimiento")
    @Past(message = "Debe ingresar una fecha válida")
    lateinit var birthDate: LocalDate

    @NotBlank(message = "Debe ingresar su telefono")
    lateinit var phoneNumber: String

    @NotBlank(message = "Debe ingresar el tipo")
    lateinit var type: String

    fun toUser(): User {
        val json = toJson(this)
        return fromJson(json, User::class.java)
    }
}


open class PersonRequestDTO() : UserRequestDTO() {
    @NotBlank(message = "Debe ingresar su apellido")
    lateinit var lastaname: String

    @NotBlank(message = "Debe ingresar su genero")
    lateinit var gender: String

}

class PersonWithPassDTO : PersonRequestDTO() {
    @NotBlank(message = "Debe ingresar la contraseña")
    lateinit var password: String

}


open class InstitutionRequestDTO : UserRequestDTO() {
    @NotBlank(message = "Debe ingresar una descripcion")
    lateinit var description: String

}

class InstitutionWithPassDTO : InstitutionRequestDTO() {
    @NotBlank(message = "Debe ingresar la contraseña")
    lateinit var password: String

}
