package com.example.core.oopprinciples


open class Car {
    open fun drive() {
        println("driving..")
    }
}

class BMW: Car() { // extend
    override fun drive() {
        println("driving..BMW")
    }
}

interface Food {
    fun eat()
}

class HealthyFood: Food { // implements
    override fun eat() {
        println("eating healthy food!")
    }
}

fun main() {
    val car: Car = BMW()
    car.drive()

    val food: Food = HealthyFood()
    food.eat()
}