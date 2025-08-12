package com.example.ds.arrays.impl;

public class ThreeDArray {
    public static void main(String[] args) {
        int[][][] arr = new int[2][3][4];

        for(int i=0; i<arr.length; i++) {
            for(int j=0; j<arr[i].length; j++) {
                for(int k=0; k<arr[i][j].length; k++) {
                    System.out.print(arr[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println("----");
        }

    }
    /**
     * Output:
     *
     * 0 0 0 0
     * 0 0 0 0
     * 0 0 0 0
     * ----
     * 0 0 0 0
     * 0 0 0 0
     * 0 0 0 0
     * ----
     *
     */
}
