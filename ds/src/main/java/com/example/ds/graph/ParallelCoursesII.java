package com.example.ds.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class ParallelCoursesII {
    static public int minNumberOfSemesters(int n, int[][] relations, int k) {
        List<List<Integer>> adjRelations = new ArrayList();
        for(int i=0; i<=n; i++) adjRelations.add(new ArrayList());

        int[] indegree = new int[n+1];
        for(int[] course: relations) {
            adjRelations.get(course[0]).add(course[1]);
            ++indegree[course[1]];
        }

        Queue<Integer> queue = new LinkedList<>();
        for(int i=1; i<=n; i++) {
            if(indegree[i]==0) {
                queue.add(i);
            }
        }

        int semester = 0;
        int courses = 0;

        while(!queue.isEmpty()) {
            int size = Math.min(queue.size(), k);
            semester++;

            for(int i=0; i<size; i++) {
                int course = queue.poll();
                courses++;

                for(int next: adjRelations.get(course)) {
                    if(--indegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }
        }

        return (n==courses) ? semester: -1;
    }

    public static void main(String[] args) {
        int n = 12, k = 2;
        int[][] relations = {
                {1, 2},
                {1, 3},
                {4, 8},
                {7, 5},
                {7, 6},
                {8, 9},
                {9, 10},
                {10, 11},
                {11, 12}
        };

        System.out.println(minNumberOfSemesters(n, relations, k));
    }
}
