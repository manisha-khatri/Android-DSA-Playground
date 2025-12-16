package com.example.core

inline fun message1(a: () -> Unit) { a() }

fun main() {
    message1 {
        println("This is 1st message")
        return@message1
    }
    message1 {
        println("This is 2nd message")
    }
}