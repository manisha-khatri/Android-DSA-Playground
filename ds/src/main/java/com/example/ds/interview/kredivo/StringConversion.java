package com.example.ds.interview.kredivo;

public class StringConversion {

    // "mac*book"->"mabook"
    static String convertString(String str) {
        StringBuilder sb = new StringBuilder();

        for(char ch: str.toCharArray()) {
            if(ch == '*') {
                if(sb.length()>0) sb.deleteCharAt(sb.length()-1);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(convertString("mac*book"));
    }
}
