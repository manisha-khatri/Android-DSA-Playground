package com.example.ds.stack;

import java.util.Stack;

public class DecodeString {
    static public String decodeString(String s) {
        Stack<StringBuilder> strStack = new Stack<>();
        Stack<Integer> numStack = new Stack<>();
        StringBuilder cur = new StringBuilder();
        int k=0;

        for(char ch: s.toCharArray()) {
            if(Character.isDigit(ch)) {
                k = 10*k + (ch-'0');
            } else if (ch == '[') {
                numStack.push(k);
                strStack.push(cur);
                cur = new StringBuilder();
                k=0;
            } else if (ch == ']') {
                int repeat = numStack.pop();
                StringBuilder decoded = strStack.pop();
                while(repeat-->0) {
                    decoded.append(cur);
                }
                cur = decoded;
            } else {
                cur.append(ch);
            }
        }
        return cur.toString();
    }

    public static void main(String[] args) {
        String  s = "3[a2[c]]";
        System.out.println(decodeString(s));

        char ch = '5';
        int val = ch-'0';
        System.out.println(val);
    }
}
