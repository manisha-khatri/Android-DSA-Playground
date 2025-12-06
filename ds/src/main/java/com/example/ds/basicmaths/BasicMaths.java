package com.example.ds.basicmaths;

public class BasicMaths {
    static int countDigits(int n) {
        int counter = 0;

        while(n>0) {
            counter++;
            n = n/10;
        }
        return counter;
    }

    static int reverseNum(int n) {
        int reverse = 0;
        while(n>0) {
            int lst = n%10;
            reverse = (reverse*10) + lst;
            n = n/10;
        }
        return reverse;
    }

    public static void main(String[] args) {
        System.out.println("Count Digits = " + countDigits(123445));
        System.out.println("Reverse Num = " + reverseNum(12345));
    }
}
