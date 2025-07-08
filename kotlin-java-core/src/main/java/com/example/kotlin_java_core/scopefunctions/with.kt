package com.example.kotlin_java_core.scopefunctions

import java.awt.Rectangle

fun with() {
    /**
     *  with
     *  1. Perform multiple operations on the same object
     *  2. Grouping operation on non-nullable object
     *  3. Object Configuration with Immediate Computation (No Need to Return the Object)
     */

    // 1.Perform multiple operations on the same object
    val builder = StringBuilder()
    var res = with(builder) {
        append("Manisha")
        append("Khatri")
    }
    println(res)

    builder.let {
        it.append("Manisha")
        it.append("Khatri")
    }

    builder.run {
        append("Manisha")
        append("Khatri")
    }

    val res1 = with(builder) {
        append("Shasha")
        append("Grey")
        9
    }
    println(res1)
    println(builder.toString())

    // 2. Grouping operation on non-nullable object
    val person = Person("manisha", 29)
    with(person) {
        println(name)
        println(age)
    }

    person.let {
        println(it.name)
        println(it.age)
    }

    person.run {
        println(name)
        println(age)
    }

   with(person) {
       name = "geo"
   }

    // 3. Object Configuration with Immediate Computation (No Need to Return the Object)

    val area = with(Rectangle1(5, 10)) {
        length * breadth
    }
    println(area)

    val switch = with(Company("Google", 70)) {
        "Got the job in $name with $salaryInLakh package"
    }
    println(switch)

    val switch2 = Company("Google", 70).run {
        "Got the job in $name with $salaryInLakh package"
    }
    println(switch2)

}

data class Rectangle1(val length: Int, val breadth: Int)
data class Company(val name: String, val salaryInLakh: Int)

fun main() {
    with()
}

