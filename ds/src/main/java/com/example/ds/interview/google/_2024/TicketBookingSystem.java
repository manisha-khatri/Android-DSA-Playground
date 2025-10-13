package com.example.ds.interview.google._2024;

/**
 A ticket machine is a device similar to an ATM and was introduced by Indian Railways
 in order to make purchasing train tickets easier.

 The first step in buying a ticket is choosing the destination of the journey. The destination
 can be one of N destinations offered in advance, names of local and national places. The keyboard
 of the ticket machine has 28 keys - 26 uppercase alphabets from A to Z, ENTER and BACKSPACE.
 The travelers choose the destination by typing its name letter by letter. Once the destination is typed,
 they press the ENTER key to purchase the ticket. If they type the wrong letter, they use the BACKSPACE
 key to correct.

 We, as an engineer building the system, already know all N destinations. We’re receiving the next
 key pressed, by the traveler, in a data stream.

 Question 01: When the client presses ENTER, check if the entered word points to the valid destination or not.
 Example:
 N = 5, destinations = [“DELHI”, “BANGALORE”, “MUMBAI”, “CHENNAI”, “GURGAON”]
 Keys pressed by the traveler = [“D”, “E”, “R”, BACKSPACE, “L”, “H”, “I”, ENTER]

 Output: YES (the destination DELHI is valid)
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Rules:
 * 1. Ticket machine keys = 28
 *     - 26 uppercase (A-Z) +Enter + Backspace
 * 2. All the destinations known
 *
 * Steps :
 * 1. Choose Destination by typing letter by letter
 *     - N destinations offered in advance
 *     - destination = name of local and national places
 * 2. Press Enter to purchase ticket
 * 3. If wrong key click "backspace"
 *
 * Example:
 * N = 5, destinations = [“DELHI”, “BANGALORE”, “MUMBAI”, “CHENNAI”, “GURGAON”]
 * Keys pressed by the traveler = [“D”, “E”, “R”, BACKSPACE, “L”, “H”, “I”, ENTER]
 */


public class TicketBookingSystem {

    static void findDestinationOptimal(String[] destination, String[] keysTyped) {
        Set<String> destinationSet = new HashSet<>(Arrays.asList(destination));

        StringBuilder sb = new StringBuilder();
        for(String key: keysTyped) {
            if(key.equals("ENTER")) {
                break;
            } else if(key.equals("BACKSPACE")) {
                if(sb.length()>0) sb.deleteCharAt(sb.length()-1);
            } else {
                sb.append(key);
            }
        }

        String typedDestination = sb.toString();
        if(destinationSet.contains(typedDestination)) {
            System.out.println("YES");
            return;
        }
        System.out.println("NO");

    }


    static void findDestination(String[] destinations, String[] keysTyped) {
        Stack<String> stack = new Stack<>();

        for(String key: keysTyped) {
            if(key.equals("BACKSPACE")) {
                if(!stack.isEmpty()) stack.pop();
            } else if(key.equals("ENTER")) {
                break;
            } else {
                stack.push(key);
            }
        }

        StringBuilder sb = new StringBuilder();
        for(String st: stack) sb.append(st);
        String typedDestination = sb.toString();

        for(String dest: destinations) {
            if(typedDestination.equals(dest)) {
                System.out.println("YES");
                return;
            }
        }
        System.out.println("NO");
    }

    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem();
        String[] destinations = {"DELHI", "BANGALORE", "MUMBAI", "CHENNAI", "GURGAON"};
        String[] keysTyped = {"D", "E", "R", "BACKSPACE", "L", "H", "I", "ENTER"};

        findDestination(destinations, keysTyped);
        findDestinationOptimal(destinations, keysTyped);
    }
}

