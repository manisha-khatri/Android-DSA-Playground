package com.example.datastructure.trees;

import java.util.ArrayList;
import java.util.List;

public class WordSearchII {
    public static List<String> findWords(char[][] board, String[] words) {
        List<String> res = new ArrayList<>();

        for(String word: words) {
            if(exist(board, word)) {
                res.add(word);
            }
        }
        return res;
    }

    public static boolean exist(char[][] board, String word) {
        int row = board.length;
        int col = board[0].length;
        boolean vis[][] = new boolean[row][col];

        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++) {
                if(dfs(i, j, vis, word, 0, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean dfs(int i, int j, boolean vis[][], String word, int index, char[][] board) {
        if(index == word.length())
            return true;

        if(i<0 || j<0 || i>=board.length || j>=board[0].length || vis[i][j] || board[i][j]!=word.charAt(index))
            return false;

        vis[i][j] = true;

        boolean found = dfs(i+1, j, vis, word, index+1, board) ||
                        dfs(i, j+1, vis, word, index+1, board) ||
                        dfs(i-1, j, vis, word, index+1, board) ||
                        dfs(i, j-1, vis, word, index+1, board);

        vis[i][j] = false;

        return found;
    }

    public static void main(String[] args) {
        char[][] board = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };

        String[] words = {"oath", "pea", "eat", "rain"};

        List<String> result = findWords(board, words);

        System.out.println(result);  // Output: [oath, eat]
    }
}
