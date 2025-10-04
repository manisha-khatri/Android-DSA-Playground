package com.example.ds.interview.uber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Given a lexicographically sorted dictionary (array of words) of an alien
 * language, find the order of characters in the language. In the example below,
 * the word - xww comes before wxyz denotes x comes before w in the dictionary.
 * In the same manner, wxyz comes before wxyw denotes z comes before w in the dictionary, and so on
 *
 * 📝 Problem Restated
 * You’re still solving the Alien Dictionary (find character order from sorted words), but instead of returning a single string, you must return a 2D String array (String[][]) where:
 * Each row represents one "level" of characters that can appear together (i.e., no ordering constraints within that level).
 * Characters in earlier rows must come before characters in later rows.
 * So essentially, this is topological sorting by levels (a layer-wise BFS like Kahn’s algorithm).
 */

public class UberInterviewII {

    static String[][] alienOrder(String[] words) {
        Set<Integer> wordsPresent = new HashSet<>();
         for(String word: words) {
             for(char ch: word.toCharArray()) {
                 wordsPresent.add(ch - 'a');
             }
         }

        List<List<Integer>> graph = new ArrayList<>();
        for(int i=0; i<26; i++) graph.add(new ArrayList<>());

        int[] indegree = new int[26];

        for(int i=1; i<words.length; i++) {
            String w1 = words[i-1];
            String w2 = words[i];
            int minLen = Math.min(w1.length(),w2.length());

            int j=0;
            while(j<minLen && w1.charAt(j)==w2.charAt(j))
                j++;

            if(j<minLen) {
                char c1= w1.charAt(j); // abc
                char c2= w2.charAt(j); // abd
                int m1 = c1-'a';
                int m2 = c2-'a';
                if(!graph.get(m1).contains(m2)) { // Avoid Duplicates
                    graph.get(m1).add(m2);
                    indegree[m2]++;
                }
            } else if(w1.length()>w2.length()) {
                return new String[0][0];
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for(int c: wordsPresent) {
            if(indegree[c]==0) {
                queue.offer(c);
            }
        }

        List<List<String>> levels = new ArrayList<>();
        while(!queue.isEmpty()) {
            int size = queue.size();
            List<String> level = new ArrayList<>();

            for(int i=0; i<size; i++) {
                int node = queue.poll();
                level.add(Character.toString((char)(node + 'a')));

                for(int neighbor: graph.get(node)) {
                    if(--indegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
            levels.add(level);
        }

        String[][] result = new String[levels.size()][];
        for(int i=0; i<levels.size(); i++) {
            result[i] = levels.get(i).toArray(new String[0]);
        }
        return result;
    }


    public static void main(String[] args) {
        String[] words1 = {"xww", "wxyz", "wxyw", "ywx", "ywz"};
        String[][] order1 = alienOrder(words1);
        System.out.println(Arrays.deepToString(order1));

        String[] words2 = {"abc", "abd", "abex", "abem"};
        String[][] order2 = alienOrder(words2);
        System.out.println(Arrays.deepToString(order2));
    }
}
