package com.example.kotlin_java_core.scopefunctions

fun also() {
    val person = Person("Manisha", 29).also {
        it.name = "sasha"
        println("Person initialized with name: ${it.name} and age: ${it.age}")
    }
    println(person)

    val list = mutableListOf<String>().also {
        it.add("One")
        it.add("Two")
    }

    val numbers = mutableListOf(1, 2, 3)
    val newNumbers = numbers.also {
        println("Original list: $it") // Logging the list
        it.add(4) // Modifying the list (side effect)
        println("List after adding 4: $it") // Logging again
    } // 'numbers' list itself is returned here

    println("newNumbers" + newNumbers) // Output: [1, 2, 3, 4]
    println("numbers" + numbers) // Output: [1, 2, 3, 4]

    
}

fun main() {
    also()
}