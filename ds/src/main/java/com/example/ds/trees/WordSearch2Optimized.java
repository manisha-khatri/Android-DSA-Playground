package com.example.ds.trees;

import java.util.ArrayList;
import java.util.List;

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isWordEnd = false;
}

public class WordSearch2Optimized {
    TrieNode root = null;

    WordSearch2Optimized() {
        root = new TrieNode();
    }
    public static List<String> findWords(char[][] board, String[] words) {
        List<String> res = new ArrayList<>();

        return res;
    }

    public static void main(String[] args) {
        char[][] board = {
                {'a', 'c'},
                {'p', 'e'},
        };

        String[] words = {"app", "ape", "ace"};

        List<String> result = findWords(board, words);

        System.out.println(result);  // Output: [oath, eat]
    }
}
