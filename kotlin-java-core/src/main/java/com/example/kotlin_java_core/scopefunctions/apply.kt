package com.example.kotlin_java_core.scopefunctions

fun apply() {
    val person = Person("sasha", 29)
    person.apply {
        name = "Rosha"
        age = 26
    }
    println(person)

    val anotherPerson = Person1("Frank", 50).apply {
        city = "Berlin"
    }.apply {
        age += 10
    }

}

fun main() {
    apply()
}

data class Person1(var name: String, var age: Int, var city: String = "")