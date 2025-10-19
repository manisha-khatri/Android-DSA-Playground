package com.example.ds.interview.microsoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trending hashtags problem
 *
 * Question
 * Design a system that tracks hashtags in real time and returns the top K trending hashtags within the last T minutes.
 * You are given:
 * A stream of hashtags with timestamps.
 * Two integers: K (number of top hashtags) and T (time window in minutes).
 * After processing the stream, output the top K hashtags that appeared in the last T minutes, sorted by frequency (and lexicographically for ties).
 * Input
 * hashtags = [("#AI", 1), ("#Tech", 2), ("#AI", 3), ("#Code",4),("#AI", 6), ("#Tech", 7), ("#Dev", 8),
 * ("#Tech", 9), ("#Code", 10)]
 * K = 3
 * T = 5
 * current_time = 10
 *
 * Expected Output
 * Top 3 trending hashtags in last 5 minutes:
 * #Tech (2)
 * #Code (1)
 * #Dev (1)
 *
 * Explanation
 * Current time = 10, so we consider hashtags from time 6 to 10.
 * Frequencies in this window:
 * #AI → 1 (at time 6)
 * #Tech → 2 (at times 7, 9)
 * #Code → 1 (at times 10)
 * #Dev → 1 (at times 8)
 * Top 3:
 * #Tech (2), #Code (1), #Dev (1)
 */
public class TrendingHashtagsBruteForce {

    /**
     * Return --> top k trending hashtags within the last T minutes (list<=k && hashtags_timestamp<T)
     * stream = [hashtags+timestamps]
     * k--> number of hashtags i need
     * T --> window
     * sort
     * 1. Frequency
     * 2. Ties --> lexo
     *
     */

    static void getKTrendingHashtags(List<Tag> stream, int k, int T, int currentTime) {
        int timeWindow = currentTime - T;
        Map<String, Integer> hashtags = new HashMap<>();

        for(Tag tag: stream) {
            if(tag.timestamps >= timeWindow && tag.timestamps<=currentTime) {
                hashtags.put(tag.hashTag, hashtags.getOrDefault(tag.hashTag,0)+1);
            }
        }

        //sort
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtags.entrySet());
        sortedHashtags.sort((a,b) -> {
                if(!a.getValue().equals(b.getValue())) {
                    return b.getValue() - a.getValue(); // desc
                } else {
                    return a.getKey().compareTo(b.getKey());
                }
            }
        );

        for(int i=0; i<Math.min(k,sortedHashtags.size()); i++) {
            System.out.println(sortedHashtags.get(i).getKey() + " (" + sortedHashtags.get(i).getValue() + ") ");
        }
    }


    static class Tag {
        int timestamps;
        String hashTag;

        Tag(String hashTag, int timestamps) {
            this.timestamps = timestamps;
            this.hashTag = hashTag;
        }
    }

    public static void main(String[] args) {
        List<Tag> stream = new ArrayList<>();
        stream.add(new Tag("#AI",1));
        stream.add(new Tag("#Tech",2));
        stream.add(new Tag("#AI",3));
        stream.add(new Tag("#Code",4));
        stream.add(new Tag("#AI",6));
        stream.add(new Tag("#Tech",7));
        stream.add(new Tag("#Dev",8));
        stream.add(new Tag("#Tech",9));
        stream.add(new Tag("#Code",10));

        int K = 3;
        int T = 5;
        int currentTime = 10;
        getKTrendingHashtags(stream, K, T, currentTime);
    }

}
