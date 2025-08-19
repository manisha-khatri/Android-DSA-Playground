package com.example.ds.interview.pramp;

import java.util.Arrays;

class Solution {

    static double findGrantsCap(double[] grantsArray, double newBudget) {
        Arrays.sort(grantsArray);

        int n = grantsArray.length;
        double budget = newBudget;
        double share = 0.0;

        for(int i=0; i<n; i++) {
            int elementsLeft = n-i;
            share = budget/elementsLeft;

            if(share<=grantsArray[i]) return share;
            budget = budget - grantsArray[i];
        }

        return grantsArray[n - 1];
    }



    public static void main(String[] args) {
        // debug your code below
        double[] grantsArray = {2, 100, 50, 120, 1000};
        double newBudget = 190;
        System.out.println("Cap: " + findGrantsCap(grantsArray, newBudget));
    }
}