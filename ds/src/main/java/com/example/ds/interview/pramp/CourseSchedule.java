package com.example.ds.interview.pramp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CourseSchedule {

    static boolean canFinish(int numCourses, int[][] prerequisites) {
        // your code goes here
        int n = prerequisites.length;
        List<List<Integer>> adjList = new ArrayList();
        int[] indegree = new int[numCourses];

        for(int i=0; i<numCourses; i++)
            adjList.add(new ArrayList<>());

        for(int[] row: prerequisites) {
            int u = row[0];
            int v = row[1];
            adjList.get(v).add(u);
            ++indegree[u];
        }

        Queue<Integer> queue = new LinkedList();

        for(int i=0; i<numCourses; i++) {
            if(indegree[i]==0)
                queue.add(i);
        }

        int totalCourses = 0;

        while(!queue.isEmpty()) {
            int course = queue.poll();
            totalCourses++;

            for(int neighbour: adjList.get(course)) {
                indegree[neighbour]--;
                if(indegree[neighbour] == 0) {
                    queue.add(neighbour);
                }
            }
        }

        return totalCourses==numCourses;
    }

    public static void main(String[] args) {
        // debug your code below
        int numCourses = 4;
        int[][] prerequisites = { {1, 0}, {2, 1}, {3, 2} };
        System.out.println(canFinish(numCourses, prerequisites));
    }
}
