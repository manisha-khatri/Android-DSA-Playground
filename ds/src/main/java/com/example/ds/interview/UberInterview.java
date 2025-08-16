package com.example.ds.interview;

/**
 * sounds like a "Word Matching with Wildcards" problem
 * — specifically, matching strings against patterns where the dash (-) acts as a
 * wildcard that can be filled in with characters from the same given array.
 *
 * example:
 * word = "Hello"
 * skeleton[] = {"Hello", "He-l-", "Hell-", "-ello"}
 * ans = {"Hello", "-ello"}
 */
public class UberInterview {

    static int wordMatching(String word, String[] skeleton) {
        int[] frequency =  new int[52]; // 0-26 --> a-z, 26-51 --> A-Z

        for(char ch: word.toCharArray()) {
            if(ch>='a' && ch<='z') {
                int indx = ch - 'a';
                ++frequency[indx];
            } else if(ch>='A' && ch<='Z') {
                int indx = ch - 'A' + 26;
                ++frequency[indx];
            }
        }

        for(String st: skeleton) {
            for(char ch: st.toCharArray()) {
                if(ch>='a' && ch<='z') {
                    int indx = ch - 'a';
                    ++frequency[indx];
                } else if(ch>='A' && ch<='Z') {
                    int indx = ch - 'A' + 26;
                    ++frequency[indx];
                }
            }
        }
        int minVal = Integer.MAX_VALUE;
        for(int pos: frequency) {
           if(pos>0) {
               minVal = Math.min(pos, minVal);
           }
        }
        return minVal!=Integer.MAX_VALUE ? minVal-1: 0;
    }

    public static void main(String[] args) {
        String word = "Hello";
        String[] skeleton = {"Hello", "He-l-", "Hell-", "-ello", "Hell", "ello"};

        System.out.println(wordMatching(word, skeleton));
    }
}
