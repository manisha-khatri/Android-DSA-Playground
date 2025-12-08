package com.example.ds.stack;

import java.util.Stack;

public class BackspaceStringCompare {
    static public boolean backspaceCompare(String s, String t) {
        Stack<Character> sStack = new Stack<>();
        Stack<Character> tStack = new Stack<>();

        for(char ch: s.toCharArray()) {
            if(ch == '#') {
                if(!sStack.isEmpty()) sStack.pop();
            } else {
                sStack.push(ch);
            }
        }

        for(char ch: t.toCharArray()) {
            if(ch == '#') {
                if(!tStack.isEmpty()) tStack.pop();
            } else {
                tStack.push(ch);
            }
        }

        return sStack.equals(tStack);
    }

    public static void main(String[] args) {
        String s = "y#fo##f";
        String t = "y#f#o##f";

        System.out.println(backspaceCompare(s,t));
    }
}
