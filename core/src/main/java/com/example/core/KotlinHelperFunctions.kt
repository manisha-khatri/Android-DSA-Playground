package com.example.core

fun kotlinHelperFunctions() {
    //takeIf
    val input: String? = "Kotlin"
    val result = input?.takeIf { it.startsWith("K") }?.uppercase() ?: "Invalid"
    println(result) // Output: KOTLIN


    // repeat
    repeat(5) {
        println("printing $it")
    }

    //removeIf
    val numbers = mutableListOf(1,2,3,4,5,6,7,8,9,10)
    numbers.removeIf { it % 2 == 0 }
    println(numbers)
}

fun main() {
    kotlinHelperFunctions()
}