package com.example.ds.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CourseScheduleI {

    static public boolean canFinish(int numCourses, int[][] prerequisites) {
        /**
         * - Convert and fix the directed edge flow, u --> v, creating adj list
         * In-degree --> check 0 --> yes --> Add to queue --> Add to totalCourses
         * --> take neighbour reduce its in degree --> check whose in-degree is 0 --> add in queue
         */

        if(prerequisites == null || prerequisites.length == 0) return false;

        int[] indegree = new int[numCourses];
        Queue<Integer> queue = new LinkedList<>();
        int totalCources = 0;

        List<List<Integer>> adj = new ArrayList<>();
        for(int i=0; i<numCourses; i++) adj.add(new ArrayList<>());

        for(int[] pair: prerequisites) {
            int v = pair[0];
            int u = pair[1];
            ++indegree[v];
            adj.get(u).add(v);
        }

        for(int i=0; i<numCourses; i++) {
            if(indegree[i] == 0) queue.offer(i);
        }

        if (queue.isEmpty()) return false;

        while(!queue.isEmpty()) {
            int v = queue.poll();
            totalCources++;

            for(int u: adj.get(v)) {
                if(--indegree[u] == 0) {
                    queue.offer(u);
                }
            }
        }

        return totalCources == numCourses;
    }


    public static void main(String[] args) {
        int numCourses = 2;
        int[][] prerequisites = {
                {0,1}
        };

        System.out.print("Result is = " + canFinish(numCourses, prerequisites));
    }
}
