package com.example.ds.heap;

import java.util.Arrays;
import java.util.PriorityQueue;

public class MeetingRoomII {
    public static int minMeetingRooms(int[][] intervals) {
        if(intervals.length==0) return 0;

        Arrays.sort(intervals, (a,b) -> a[0]-b[0]);
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        heap.add(intervals[0][1]);

        for(int i=1; i<intervals.length; i++) {
            if(intervals[i][0]>=heap.peek()) {
                heap.poll();
            }
            heap.offer(intervals[i][1]);
        }
        return heap.size();
    }

    public static void main(String[] args) {
        int[][] intervals = {{0,30},{5,10},{15,20}};

        System.out.print(minMeetingRooms(intervals));
    }
}
