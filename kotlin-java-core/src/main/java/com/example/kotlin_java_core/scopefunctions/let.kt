package com.example.kotlin_java_core.scopefunctions

fun let() {
    /**
     *  Let
     *  1. Null check
     *  2. As local variable
     *  3. As a parameter
     */

    // 1. Null check
    val p1: Person? = null
    p1?.let {
        it.name = "Sasha"
        it.age = 29
    }

    // 2. As a local variable --> price acts as a local variable
    val totalPrice = (5*8).let { price ->
        val tax = price * 0.8
        tax + price
    }

    // 3. As a parameter
    val res = "Manisha".let {
        processName(it)
        println(it)
    }

    val length = " Manisha "    // Chaining
        .let{ it.trim() }
        .let{ it.uppercase() }
        .let { it.length }

}

fun processName(name: String) {
    println("processing: $name")
}

data class Person(var name: String, var age: Int)
