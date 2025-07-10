package com.example.core.scopefunctions

fun higherOrderFunctions() {
    // Function as a parameter

    val f1 = f1(1, 2, { a, b ->
        val c = a+b
        "$a + $b = $c"
    })
    println(f1)


    val loginResult = login(
        "manisha",
        "123",
        onSuccess = { println("Successfully logged in!!") },
        onFailure = { println("Login Failed!!") }
    )
    println("Was login successful? $loginResult")

    // Function Return a Function
    val greetingSasha = greeting("sasha")
    println(greetingSasha())

}

fun greeting(name: String): () -> String {
    return {"Hello $name"}
}

fun f1(a:Int, b:Int, f2: (Int, Int) -> String): String {
    return f2(a,b)
}

fun login(username: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit): Boolean {
    return if(username == "manisha" && password == "123") {
        onSuccess()
        true
    } else {
        onFailure()
        false
    }
}

fun main() {
    higherOrderFunctions()
}