package com.example.ds.heap;

import java.util.PriorityQueue;

public class MeetingRoomIII {
    /**
     * 1. Sort meetings by start_time
     * 2.
     *  available_rooms <room_no> --> n, Always sorted smallest at the top
     *  busy_rooms <end_time, room_no>
     *      - Sorted on the basis of end_time
     *      - if(multiple end_times < meetings_start_time) then pop() smallest meeting room
     *  3. Range concept
     *      - No available meeting room -> pop() top meeting room from busy
     *      - calculate the range of current (end-start) and add it to the end_time
     */
    public static int mostBooked(int n, int[][] meetings) {
        if(meetings.length==0) return 0;

        PriorityQueue<Integer> available = new PriorityQueue<>();
        for(int i=0; i<n; i++) available.offer(i);

        PriorityQueue<long[]> busy = new PriorityQueue<>(
                (a,b) -> a[0]==b[0] ? Long.compare(a[1],b[1]): Long.compare(a[0],b[0])
        );

        int[] rooms = new int[n];

        for(int[] meeting: meetings) {
            int startTime = meeting[0];
            int endTime = meeting[1];

            if(!busy.isEmpty() && busy.peek()[0]<=startTime){
                available.offer((int)busy.poll()[1]);
            }

            if(!available.isEmpty()) {
                int room = available.poll();
                rooms[room]++;
                busy.offer(new long[]{endTime, room});
            } else {
                long[] room = busy.poll();
                int newEnd = (int)room[0] + (endTime - startTime);
                rooms[(int)room[1]]++;
                busy.offer(new long[]{newEnd, room[1]});
            }
        }

        int max = 0;
        for(int i=1; i<n; i++) {
            if(rooms[max]<rooms[i]) {
                max = i;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int n = 3;
        int[][] meetings = {{1,20},{2,10},{3,5},{4,9},{6,8}};

        System.out.print(mostBooked(n, meetings));
    }
}
