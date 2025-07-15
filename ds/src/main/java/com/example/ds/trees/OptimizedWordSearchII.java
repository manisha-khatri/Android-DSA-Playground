package com.example.ds.trees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Trie {
    Trie[] next = new Trie[26];
    String word = null;
}

public class OptimizedWordSearchII {

    public static List<String> findWords(char[][] board, String[] words) {
        Set<String> res = new HashSet<>();
        Trie root = buildTrie(words);

        int row = board.length;
        int col = board[0].length;

        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++) {
                // Only start DFS if the first character exists in Trie
                if (root.next[board[i][j] - 'a'] != null) {
                    dfs(i, j, board, res, root);
                }
            }
        }

        return new ArrayList<>(res);
    }

    static Trie buildTrie(String[] words) {
        Trie root = new Trie();

        for(String word: words) {
            Trie node = root;

            for(char c: word.toCharArray()) {
                int index = c - 'a';
                if(node.next[index] == null) {
                    node.next[index] = new Trie();
                }
                node = node.next[index];
            }
            node.word = word;
        }
        return root;
    }

    static void dfs(int i, int j, char[][] board, Set<String> res, Trie node) {
        if(i<0 || i>=board.length || j<0 || j>=board[0].length || board[i][j] == '#')
            return;

        if(node.next[board[i][j] - 'a'] == null) return; // Imp: Keep this after # check, as # is a special character

        Trie curNode = node.next[board[i][j] - 'a'];
        if(curNode.word != null) {
            res.add(curNode.word);
        }

        // Go to the next char
        char c = board[i][j];
        board[i][j] = '#';
        dfs(i+1, j, board, res, curNode);
        dfs(i, j+1, board, res, curNode);
        dfs(i-1, j, board, res, curNode);
        dfs(i, j-1, board, res, curNode);

        board[i][j] = c;
    }

    public static void main(String[] args) {
        char[][] board = {
                {'a', 'c'},
                {'p', 'e'},
        };

        String[] words = {"app", "ape", "ace"};

        List<String> result = findWords(board, words);

        System.out.println(result);
    }
}
