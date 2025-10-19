package com.example.ds.interview.microsoft;

import java.util.*;

public class TrendingHashtagsOptimal {
    static class Event {
        String hashtag;
        int timestamp;
        Event(String hashtag, int timestamp) {
            this.hashtag = hashtag;
            this.timestamp = timestamp;
        }
    }

    static int K;
    static int T;
    static Map<String, Integer> frequency;
    static Deque<Event> window;
    TrendingHashtagsOptimal(int k, int t) {
        K=k;
        T=t;
        frequency = new HashMap<>();
        window = new ArrayDeque<>();
    }

    static void addHash(String hashTag, int timestamp) {
        window.offer(new Event(hashTag, timestamp));
        frequency.put(hashTag, frequency.getOrDefault(hashTag,0) +1);
        removeExpired(timestamp);
    }

    static void removeExpired(int timestamp) {
        // 1,2,3,4,5,6,  7-5 = 2
        if(!window.isEmpty() && window.peek().timestamp<=timestamp-T) {
            Event old = window.pollFirst();
            String hash = old.hashtag;
            frequency.put(hash, frequency.get(hash)-1);
            if(frequency.get(hash) == 0) frequency.remove(hash);
        }
    }

    static void findKTending(int currentTime) {
        removeExpired(currentTime);

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a,b) -> {
            if(!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue(); // dec
            } else {
                return a.getKey().compareTo(b.getKey());
            }
        });
        pq.addAll(frequency.entrySet());

        int count=0;
        for(int i=count; i<K && !pq.isEmpty(); i++) {
            Map.Entry<String, Integer> entry = pq.poll();
            System.out.println(entry.getKey() + " (" + entry.getValue() + ") ");
            count++;
        }
    }

    public static void main(String[] args) {
        List<Event> stream = Arrays.asList(
                new Event("#AI", 1),
                new Event("#Tech", 2),
                new Event("#AI", 3),
                new Event("#Code", 4),
                new Event("#AI", 6),
                new Event("#Tech", 7),
                new Event("#Dev", 8),
                new Event("#Tech", 9),
                new Event("#Code", 10)
        );
        int K=3, T=5;
        TrendingHashtagsOptimal tracker = new TrendingHashtagsOptimal(K,T);

        for(Event tag: stream) {
            addHash(tag.hashtag, tag.timestamp);
        }
        findKTending(10);

    }
}
