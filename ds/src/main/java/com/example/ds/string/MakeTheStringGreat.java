package com.example.ds.string;

public class MakeTheStringGreat {
    static public String makeGood(String s) {
        char[] arr = s.toCharArray();
        int i = 0; // write pointer

        for (int j = 0; j < arr.length; j++) {
            arr[i] = arr[j];
            // if i > 0 and arr[i] & arr[i-1] form bad pair → undo
            if (i > 0 && Math.abs(arr[i] - arr[i - 1]) == 32) {
                i--; // remove both (pop effect)
            } else {
                i++; // valid, push effect
            }
        }

        return new String(arr, 0, i);
    }

    public static void main(String[] args) {
        String s = "aAbBcC";
        System.out.println(makeGood(s));
    }
}
