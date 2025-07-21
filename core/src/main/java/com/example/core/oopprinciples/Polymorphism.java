package com.example.core.oopprinciples;

class Calculator {
    // Overloaded add method
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }
}


class Animal {
    void sound() {
        System.out.println("Animal makes a sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Dog barks");
    }
}

class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("Cat meows");
    }
}

public class Polymorphism {
    public static void main(String[] args) {
        // Compile-Time Polymorphism -- Method Overloading.
        Calculator calc = new Calculator();
        System.out.println(calc.add(2, 3));       // Calls int version
        System.out.println(calc.add(2.5, 3.5));   // Calls double version


        // Run-Time Polymorphism -- Method Overriding.
        Animal a1 = new Dog();   // Upcasting
        Animal a2 = new Cat();

        a1.sound();  // Output: Dog barks
        a2.sound();  // Output: Cat meows

    }
}
