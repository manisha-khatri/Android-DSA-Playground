package com.example.core.generics

import java.awt.Container

fun main() {
    val iObj = IntContainer(3)
    iObj.getValue()

    val sObj = StringContainer("Hello")
    sObj.getValue()

    val obj1 = Container(2)
    obj1.getValue()

    val obj2 = Container("Hello")
    obj2.getValue()
}

class Container<T>(var data: T) {
    fun setValue(value: T) {
        data = value
    }

    fun getValue(): T {
        return data
    }
}

class IntContainer(var data: Int) {
    fun setValue(value: Int) {
        data = value
    }

    fun getValue(): Int {
        return data
    }
}

class StringContainer(var data: String) {
    fun setValue(value: String) {
        data = value
    }

    fun getValue(): String {
        return data
    }
}