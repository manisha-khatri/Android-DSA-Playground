package com.example.kotlin_java_core.scopefunctions

fun run() {
    /**
     *  run
     *  1. Object Configuration + Compute and Return a Result
     *  2. Null Safety + Lambda Result
     *  3. Local scope creation (Temporary variable)
     */

    // 1. Object Configuration + Compute and Return a Result
    var person = Person("Manisha", 29)
    var statement = person.run {
        name = "Sasha"
        age = 29
        "$name is $age year old"
    }
    println(statement)


    // 2. Null Safety + Lambda Result (Like let)
    val person1: Person ?= Person("Manisha", 29)
    statement = person1?.run {
        "$name is the best"
    } ?: "I am the best"
    println(statement)


    // 3. Local scope creation (Temporary variable)
   val finalPrice = run {
       val price = 100
       val tax = price * 0.8
       price+tax
    }

}

