package com.example.ds.graph;

import java.util.Arrays;

public class NetworkDelayTime {

    static public int networkDelayTime(int[][] times, int n, int k) {
        int[] dist = new int[n+1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        for(int i=1; i<=n-1; i++) {
            for(int[] time: times) {
                int u = time[0];
                int v = time[1];
                int w = time[2];

                if(dist[u]!=Integer.MAX_VALUE && dist[u]+ w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        int maxTime = 0;
        for(int i=1; i<=n; i++) {
            if(dist[i] == Integer.MAX_VALUE) {
                return -1;
            }
            maxTime = Math.max(maxTime, dist[i]);
        }

        return maxTime;
    }

    public static void main(String[] args) {
        int[][] times = {
                {1, 2, 1},
                {2, 3, 2},
                {1, 3, 2}
        };
        int n = 3;
        int k = 1;

        System.out.println("Result = " + networkDelayTime(times, n, k));
    }
}
