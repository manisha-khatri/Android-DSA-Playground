package com.example.core

/**
 * What do we call this?
 * 1. Callback Interface
 * 2. Anonymous Inner Class implementation of an Interface
 *
 * - We pass an anonymous class that implements the interface right on the spot.
 * - You’re creating an object of a class that doesn’t have a name (object : ClickListener { ... }).
 * - It’s called anonymous because you don’t formally name the class.
 *
 */
fun withoutLambda() {
    val handler = ClickHandler()
    handler.setOnClickListener(object: ClickListener {
        override fun onClick(message: String) {
            println(message)
        }
    })
}

interface ClickListener {
    fun onClick(message: String)
}

class ClickHandler {
    fun setOnClickListener(listener: ClickListener) {
        listener.onClick("Clicked without lambda!!")
    }
}

fun withLambda() {
    val handler2 = ClickHandler2()
    handler2.setOnClickListener { message ->
        println(message)
    }
}

class ClickHandler2 {
    fun setOnClickListener(listener: (String) -> Unit) {
        listener("Clicked with lambda!!")
    }
}

fun lambda() {
    val sum = {a:Int, b:Int -> a+b}
    println(sum(1,2))

    val multiply: (Int, Int) -> Int = {a:Int, b:Int -> a*b}
    print(multiply(2,3))

    val greet: (String) -> Unit = {name: String -> println("Hello $name")}
    greet("manisha")

    // Without Lambda:
    val withoutLambda = WithoutLambda()
    withoutLambda.setConflictListener(object : ConflictListener {
        override fun onConflict(message: String) {
            println(message)
        }
    })

    // WithLambda
    val withLambda = WithLambda()
    withLambda.setConflictListener { message ->
        println("Handled conflict: $message")
    }
}

// “Conflict Listener” with Lambda
class WithoutLambda {
    fun setConflictListener(listener: ConflictListener) {
        listener.onConflict("Without Lambda!-- setConflictListener()")
    }
}

interface ConflictListener {
    fun onConflict(message: String)
}

class WithLambda {
    fun setConflictListener(listener: (String) -> Unit) {
        listener("With Lambda!-- setConflictListener()")
    }
}

fun main() {
    lambda()
    withoutLambda()
    withLambda()
}
