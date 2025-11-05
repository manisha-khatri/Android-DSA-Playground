package com.example.ds.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class NumBusesToDestination {

    static int numBusesToDestination(int[][] routes, int source, int destination) {
        if(source == destination) return 0;
        Map<Integer, List<Integer>> stopToBuses = new HashMap<>();

        for(int bus=0; bus<routes.length; bus++) {
            for(int stop: routes[bus]) {
                if(!stopToBuses.containsKey(stop)) {
                    stopToBuses.put(stop, new ArrayList<>());
                }
                stopToBuses.get(stop).add(bus);
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visitedBuses = new HashSet<>();
        Set<Integer> visitedStops = new HashSet<>();

        if(!stopToBuses.containsKey(source)) return -1;
        for(int bus: stopToBuses.get(source)) {
            queue.offer(bus);
            visitedBuses.add(bus);
        }

        int busesTaken = 1;
        while(!queue.isEmpty()) {
            int size = queue.size();

            for(int i=0; i<size; i++) {
                int curBus = queue.poll();
                for(int stop: routes[curBus]) {
                    if(stop==destination) return busesTaken;
                    if(visitedStops.contains(stop)) continue;
                    visitedStops.add(stop);

                    for(int nextBus: stopToBuses.get(stop)) {
                        if(!visitedBuses.contains(nextBus)) {
                            queue.add(nextBus);
                            visitedBuses.add(nextBus);
                        }
                    }
                }
            }
            busesTaken++;

        }
        return -1;
    }

    public static void main(String[] args) {
        int[][] routes = {{1,2,7}, {3,6,7}};
        int source = 1;
        int target = 6;

        System.out.println(numBusesToDestination(routes, source, target));

    }
}
