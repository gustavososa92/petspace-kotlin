package com.kotlin.petspace.utils

fun camelToSnake(str: String): String {
    val regex = "([a-z])([A-Z]+)"
    val replacement = "$1_$2"
    return str.replace(regex.toRegex(), replacement).lowercase()
}

fun snakeToCamel(str: String): String {
    var newStr=str
    while (newStr.contains("_")) {
        newStr = newStr.replaceFirst(
            "_[a-z]".toRegex(), newStr[newStr.indexOf("_") + 1].uppercase()
        )
    }
    return str

}