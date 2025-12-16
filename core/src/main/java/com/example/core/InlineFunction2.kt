package com.example.core

inline fun message(a: () -> Unit) { a() }
fun main() { message { println("this is a message") } }
