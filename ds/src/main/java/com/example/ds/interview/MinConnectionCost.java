package com.example.ds.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinConnectionCost {

    static int getMinConnectionCost(int[] warehouseCapacity, List<Integer> additionalHubs) {
        int sum = 0;
        int indx = 0;

        // Convert additionalHubs into a mutable list
        List<Integer> mutableHubs = new ArrayList<>(additionalHubs);
        mutableHubs.add(warehouseCapacity.length); // Now it won’t throw an exception

        for (int i : mutableHubs) {
            i--; // Decrementing the value (but this has no effect outside the loop)
            for (; indx < i; indx++) {
                sum = sum + Math.abs(warehouseCapacity[i] - warehouseCapacity[indx]);
            }
            indx++;
        }
        return sum;
    }

    /**
     * Fix:
     * - array is call by reference, put it in a separate array
     */

    static List<Integer> getMinConnectionCost(int[] warehouseCapacity, List<List<Integer>> additionalHubs, int q) {
        List<Integer> result = new ArrayList<>();
        for(int i=0; i<q; i++) {
            result.add(getMinConnectionCost(warehouseCapacity, additionalHubs.get(i)));
        }
        return result;
    }

    public static void main(String[] args) {
        int[] warehouseCapacity = {0, 2, 5, 9, 12, 18};
        List<List<Integer>> additionalHubs = Arrays.asList(
                Arrays.asList(2, 5),
                Arrays.asList(1, 3)
        );
        int q = 2;

        for(int res: getMinConnectionCost(warehouseCapacity, additionalHubs, q)) {
            System.out.println(res);
        }
    }
}
