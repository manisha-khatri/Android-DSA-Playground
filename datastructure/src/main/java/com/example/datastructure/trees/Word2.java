package com.example.datastructure.trees;

import java.util.List;

class Solution {
    public static boolean findWords(char[][] board, String[] words) {
        int row = board.length;
        int col = board[0].length;
        String word = words[0];
        boolean vis[][] = new boolean[row][col];
        boolean res = false;

        for(int i=0; i<row; i++) {
            for(int j=0; j<col; j++) {
                res = res || validWord(board, word, i, j, vis);
            }
        }

        return res;
    }

    static boolean validWord(char[][] board, String word, int i, int j, boolean[][] vis) {
        if(i<0 || i>word.length()-1 || j<0 || j>word.length()-1 || vis[i][j] == true)
            return false;

        if(board[i][j] != word.charAt(0)) {
            return false;
        } else {
            vis[i][j] = true;
        }

        boolean left, right, top, bottom;

        left = validWord(board, word.substring(1), i-1, j, vis);
        right = validWord(board, word.substring(1), i, j+1, vis);
        top = validWord(board, word.substring(1), i+1, j, vis);
        bottom = validWord(board, word.substring(1), i, j-1, vis);


        return (left || right || top || bottom);
    }

    public static void main(String[] args) {
        char[][] board = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };

        // List of words
        String[] words = {"oath", "pea", "eat", "rain"};

        //public boolean findWords(char[][] board, String[] words) {

        boolean res = findWords(board, words);
    }
}