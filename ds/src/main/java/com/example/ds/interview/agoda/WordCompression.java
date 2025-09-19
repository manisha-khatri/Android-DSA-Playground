package com.example.ds.interview.agoda;

import java.util.Stack;

/**
 * A student compresses a big word by removing groups of consecutive equal characters.
 * An operation consists of choosing a group of k consecutive equal characters and removing them.
 * The student performs this operation as long as possible. Determine the final word after all the possible
 * operations are performed. The word is a, b, b, c, c, b, k, 3.
 *
 * String word = "abbcc"
 * int k = 2
 * Result = a
 *
 * arr = [a,c,0,0,0]
 * count = [1,2,0,0,0]
 * top = 0
 * c = c
 *
 */
public class WordCompression {

    // what I did
    static String wordCompression(String word, int k) {
        char[] stackAr = new char[word.length()];
        int[] count = new int[word.length()];
        int top=-1;

        for(int i=0; i<word.length(); i++) {
            char c = word.charAt(i);

            if(top>=0 && c==stackAr[top]) {
                count[top]++;
                if(count[top]==k) {
                    top--;
                }
            } else {
                top++;
                stackAr[top] = c;
                count[top] = 1;
            }
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<=top; i++) {
            for(int j=0; j<count[i]; j++) sb.append(stackAr[i]);
        }

        return sb.toString();
    }

    class Pair {
        char c;
        int count;
        Pair(char ch, int count) {
            c = ch;
            this.count = count;
        }
    }

    String wordCompression2(String word, int k) {
        Stack<Pair> stack = new Stack<>();

        for(int i=0; i<word.length(); i++) {
            char c = word.charAt(i);

            if(!stack.isEmpty() && stack.peek().c == c) {
                stack.peek().count++;
                if(stack.peek().count == k) {
                    stack.pop();
                }
            } else {
                stack.push(new Pair(c,1));
            }
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<stack.size(); i++) {
            for(int j=0; j<stack.get(i).count; j++) sb.append(stack.get(i).c);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String word = "abbccccbb";
        int k = 4;
        System.out.println("Way 1 -> " + wordCompression(word, k));

        System.out.println("Way 2 -> " + wordCompression(word, k));
    }
}
