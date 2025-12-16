package com.example.core

inline fun calculateTimeAndRun(func: () -> Unit) {
    val start = System.currentTimeMillis()
    func()
    val end = System.currentTimeMillis()
    println("Time taken = ${end - start} ms")
}

fun loop(n: Int) {
    for(i in 1 .. n) {
    }
}


fun main() {
    calculateTimeAndRun{
        loop(500000000)
    }
}