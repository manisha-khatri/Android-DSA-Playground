package com.example.ds.graph;
import java.util.*;

public class AlienDictionary2 {

    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        // Step 1: Initialize graph and in-degree
        for (String word : words) {
            for (char c : word.toCharArray()) {
                if (!graph.containsKey(c)) {
                    graph.put(c, new HashSet<>());
                }
                if (!inDegree.containsKey(c)) {
                    inDegree.put(c, 0);
                }
            }
        }

        // Step 2: Build edges (from word pairs)
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];

            // Edge case: word1 is prefix but longer → invalid
            if (word1.length() > word2.length() && word1.startsWith(word2)) {
                return "";
            }

            int len = Math.min(word1.length(), word2.length());
            for (int j = 0; j < len; j++) {
                char from = word1.charAt(j);
                char to = word2.charAt(j);
                if (from != to) {
                    if (!graph.get(from).contains(to)) {
                        graph.get(from).add(to);
                        inDegree.put(to, inDegree.get(to) + 1);
                    }
                    break;
                }
            }
        }

        // Step 3: Topological Sort (BFS)
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.append(current);

            for (char neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Step 4: Validate result length
        return result.length() == graph.size() ? result.toString() : "";
    }

    public static void main(String[] args) {
        AlienDictionary2 solver = new AlienDictionary2();
        String[] words = {"wrt", "wrf", "er", "ett", "rftt"};
        System.out.println(solver.alienOrder(words)); // Output: "wertf"
    }
}
