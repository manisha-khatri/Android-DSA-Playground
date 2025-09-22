package com.example.core

// 1. Passing Function as a parameter
fun calculate(a: Int, b: Int, operation:(Int, Int) -> Int): Int {
    return operation(a,b)
}

// 2. Passing function from another function
fun calculate(a: Int): (Int) -> Int {
    return {b -> b * a}
}

fun doNetworkCall(onSuccess: (String) -> Unit, onError:(Throwable)-> Unit)
{
    try{
        onSuccess("data fetched")
    } catch(e: Exception) {
        onError(e)
    }
}


fun main() {
    println(calculate(2, 3, {x, y -> x + y}))

    val func2 = calculate(3)
    println(func2(2))


    doNetworkCall(
        onSuccess = { result ->
            println("✅ Success: $result")
        },
        onError = { error ->
            println("❌ Error: ${error.message}")
        }
    )


}