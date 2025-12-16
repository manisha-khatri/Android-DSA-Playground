package com.example.core.generics

abstract class Car

class Audi: Car()

class Mercedes: Car()

class CarMechanic<out T: Car> {
    fun repair() {}
}

class Workshop {
    fun addMechanic(mechanic: CarMechanic<Car>) {

    }
}

fun main() {
    val mechanic = CarMechanic<Audi>()
    val workshop = Workshop()

    workshop.addMechanic(mechanic)
}
/**
 out -->
 - out keyword in front of Generic t <out T>
 - To tell our class that it also accepts the sub classes of this Car class
 */