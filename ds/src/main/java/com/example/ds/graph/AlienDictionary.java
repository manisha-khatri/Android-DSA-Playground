package com.example.ds.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AlienDictionary {
    String alienOrder(String[] words) {
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();

        // Step 1: Initialize graph and in-degree
        for(String word: words) {
            for(char c: word.toCharArray()) {
                if(!graph.containsKey(c)) {
                    graph.put(c, new HashSet<>());
                }
                if(!indegree.containsKey(c)) {
                    indegree.put(c,0);
                }
            }
        }

        // Step 2: Build edges (from word pairs)
        for(int i=0; i<words.length-1; i++) {
            String word1 = words[i];
            String word2 = words[i+1];

            if(word1.length()>word2.length() && word1.startsWith(word2)) return "";

            int min = Math.min(word1.length(), word2.length());

            for(int j=0; j<min; j++) {
                char from = word1.charAt(j);
                char to = word2.charAt(j);

                if(from!=to) {
                    if(!graph.get(from).contains(to)) {
                        graph.get(from).add(to);
                        indegree.put(to, indegree.get(to)+1);
                    }
                    break; // Only check the 1st different char
                }
            }
        }

        // Step 3: Topological Sort (BFS)
        Queue<Character> queue = new LinkedList<>();
        for(char c: indegree.keySet()) {
            if(indegree.get(c) == 0)
                queue.offer(c);
        }

        StringBuilder res = new StringBuilder();

        while(!queue.isEmpty()) {
            char cur = queue.poll();
            res.append(cur);

            for(char neighbour: graph.get(cur)) {
                indegree.put(neighbour, indegree.get(neighbour)-1);
                if(indegree.get(neighbour) == 0) {
                    queue.offer(neighbour);
                }
            }
        }

        return res.length() == graph.size() ? res.toString(): "";
    }

    public static void main(String[] args) {
        AlienDictionary solve = new AlienDictionary();
        String words[] = {"wrt", "wrf", "er", "ett", "rftt"};

        System.out.println(solve.alienOrder(words)); // Output: "wertf"
    }
}
