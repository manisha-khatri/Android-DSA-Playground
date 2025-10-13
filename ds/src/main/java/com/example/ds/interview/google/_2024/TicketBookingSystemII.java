package com.example.ds.interview.google._2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Follow-up: We can observe that by entering each additional letter, the number of possible destinations
 * reduces. We want to make the system a little smart. After choosing each letter, the keyboard must change
 * its appearance. Only keys that can be chosen in the next step should be highlighted in GREEN color, all
 * other keys should be GRAY-ed out (the travelers still can press gray-ed keys).
 *
 * Example:
 * N = 5, destinations = [“DELHI”, “DUBAI”, “BANGALORE”, “BOMBAY”, “CHENNAI”]
 * Keys pressed by the traveler in the order = [“D”, “E”, “R”, BACKSPACE, “L”, “H”, “I”, ENTER]
 *
 * Initially, when no key is pressed, only [“D”, “B”, “C”] should be GREEN colored.
 * After the “D” is pressed, only “E”, “U” and BACKSPACE should be GREEN colored.
 * After the “E” is pressed, only “L” and BACKSPACE should be GREEN colored.
 * After the “R” is pressed, only BACKSPACE should be GREEN colored.
 * After the BACKSPACE is pressed, only “L” and BACKSPACE should be GREEN colored.
 * After the pressed, only “H” and BACKSPACE should be GREEN colored.
 * After the “H“L” is ” is pressed, only “I” and BACKSPACE should be GREEN colored.
 * After the “I” is pressed, only ENTER and BACKSPACE should be GREEN colored.
 */

class TicketBookingSystemII {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
    }

    static class Trie {
        TrieNode root = new TrieNode();

        void insert(String word) {
            TrieNode node = root;
            for(char c: word.toCharArray()) {
                if(!node.children.containsKey(c)) {
                    node.children.put(c, new TrieNode());
                }
                node = node.children.get(c);
            }
            node.isEndOfWord = true;
        }

        TrieNode findNode(String prefix) {
            TrieNode node = root;
            for(char c: prefix.toCharArray()) {
                if(node.children.get(c) == null) return null;
                node = node.children.get(c);
            }
            return node;
        }

        Set<String> getNextKeys(String prefix) {
            Set<String> nextKeys = new HashSet<>();
            TrieNode node = findNode(prefix);

            if(node == null) {
                nextKeys.add("BACKSPACE");
                return nextKeys;
            }

            for(char c: node.children.keySet()) {
                nextKeys.add(String.valueOf(c));
            }
            nextKeys.add("BACKSPACE");
            if(node.isEndOfWord) nextKeys.add("ENTER");

            return nextKeys;
        }
    }

    public static void main(String[] args) {
        List<String> destinations = Arrays.asList("DELHI", "DUBAI", "BANGALORE", "BOMBAY", "CHENNAI");
        List<String> keysPressed = Arrays.asList("D", "E", "R", "BACKSPACE", "L", "H", "I", "ENTER");
        System.out.println("Destinations = " + destinations);
        System.out.println("Keys Pressed = " + keysPressed);
        System.out.println();

        Trie trie = new Trie();
        for(String dest: destinations) trie.insert(dest);

        StringBuilder currentWord = new StringBuilder();

        System.out.println("Initial GREEN keys: " + getInitialGreenKeys(destinations));

        for(String key: keysPressed) {
            if(key.equals("BACKSPACE")) {
                if(currentWord.length()>0)
                    currentWord.deleteCharAt(currentWord.length()-1);
            } else if (key.equals("ENTER")) {
                boolean valid = false;
                TrieNode node = trie.findNode(currentWord.toString());
                if(node!=null && node.isEndOfWord) valid = true;

                System.out.println("Pressed ENTER → " + (valid ? "YES (Valid Destination)" : "NO (Invalid)"));
            } else {
                currentWord.append(key);
            }

            Set<String> nextKeys = trie.getNextKeys(currentWord.toString());
            System.out.println("After pressing '" + key + "', current input = " + currentWord);
            System.out.println("GREEN keys: " + nextKeys);
            System.out.println();
        }
    }

    static Set<String> getInitialGreenKeys(List<String> destinations) {
        Set<String> set = new HashSet<>();
        for(String d: destinations) {
            set.add(String.valueOf(d.charAt(0)));
        }
        return set;
    }
}



