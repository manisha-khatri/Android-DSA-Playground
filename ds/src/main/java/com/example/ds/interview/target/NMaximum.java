package com.example.ds.interview.target;

public class NMaximum {
    static int getSecondMax(int[] n) {
        int max = Integer.MIN_VALUE, secondMax = Integer.MIN_VALUE;
        for(int val: n) {
            if(val>max) {
                secondMax = max;
                max = val;
            } else if(val>secondMax) {
                secondMax = val;
            }
        }
        return secondMax;
    }

    static int getThirdMax(int[] n) {
        int max = Integer.MIN_VALUE, secondMax = Integer.MIN_VALUE, thirdMax = Integer.MIN_VALUE;
        for(int val: n) {
            if(val>max) {
                secondMax = max;
                max = val;
            } else if(val>secondMax) {
                thirdMax = secondMax;
                secondMax = val;
            } else if(val>thirdMax) {
                thirdMax = val;
            }
        }
        return thirdMax;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,7,6,3,4,5};
        System.out.println(" Second Max = " + getSecondMax(arr));
        System.out.println(" Third Max = " + getThirdMax(arr));
    }
}
