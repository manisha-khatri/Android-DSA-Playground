package com.example.datastructure.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryProcessAndRobots {
    /**
     * During a clash:
     * • The process with more robots overtakes the one with fewer robots, gaining its robots.
     * • If both processes have the same number of robots, one process randomly wins and gains the other's robots.
     * This process repeats until only one inventory process remains.
     * The task is to identify which inventory processes have a possibility of surviving until the end in at least one scenario.
     * Return the indices of these processes (in ascending order, 1-indexed) that could potentially emerge as the last surviving process.
     *
     * robots = [1, 6, 2, 7, 2]
     *
     * solution:
     * - sort the elements, calc sum
     *        [1, 6, 2, 7, 2]
     *        [1, 2, 2, 6,  7]
     *  sum =  1  3  5  11  18
     *
     *  - last indices is always 1 ans pos[7] = 4
     *  compare(sum[i-1], element[i])
     *  {
     *      if(sum[i-1] < element[i]) ans = element[i]
     *  }
     *
     */

    static List<Integer> calculateWinnerProcesses(int robots[], int n) {
        ArrayList<Integer> robots1 = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();

        for(int robo: robots) {
            robots1.add(robo);
        }

        int sortedRob[] = robots;
        Arrays.sort(sortedRob);

        int roboSum[] = new int[n];
        roboSum[0] = sortedRob[0];
        for(int i=1; i<n; i++) {
            roboSum[i] = roboSum[i-1] + sortedRob[i];
        }

       result.add(robots1.indexOf(sortedRob[n-1])); // last element is always a ans

        for(int i=n-2; i>0; i--) {
            if(roboSum[i-1] < sortedRob[i]){
                result.add(robots1.indexOf(sortedRob[i]));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] robots = {4,1,2,5};
        List<Integer> result = calculateWinnerProcesses(robots, robots.length);

        for(int ans: result) {
            System.out.println(ans);
        }
    }


}
