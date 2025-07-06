package com.example.kotlin_java_core;

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String name);
}

class Subject {
    List<Observer> observers = new ArrayList<Observer>();

    void addObserver(Observer o) {
        observers.add(o);
    }

    void removeObserver(Observer o) {
        observers.remove(o);
    }

    void notifyObserver(String message) {
        for(Observer ob: observers) {
            ob.update(message);
        }
    }
}

class ConcreateObserver implements Observer {
    private String name;

    ConcreateObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}

class ObserverPattern {
    public static void main(String[] args) {
        Subject sb = new Subject();
        Observer ob1 = new ConcreateObserver("Observer 1");
        Observer ob2 = new ConcreateObserver("Observer 2");
        Observer ob3 = new ConcreateObserver("Observer 3");

        sb.addObserver(ob1);
        sb.addObserver(ob2);
        sb.addObserver(ob3);

        sb.notifyObserver("New updates!!");
    }
}