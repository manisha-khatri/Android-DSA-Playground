package com.example.ds.trees.trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AutocompleteSystem {

   class TrieNode {
       Map<Character, TrieNode> children = new HashMap();
       Map<String, Integer> freqMap = new HashMap<>();
   }

   TrieNode root;
   StringBuilder current; // user typed string
   TrieNode curNode;

    public AutocompleteSystem(String[] sentences, int[] times) {
      current = new StringBuilder();
      root = new TrieNode();

      for(int i=0; i<sentences.length; i++) {
          insert(sentences[i], times[i]);
      }
      curNode = root;
    }

    void insert(String sentence, int frequency) {
        TrieNode node = root;

        for(char ch: sentence.toCharArray()) {
            if(!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);

            int oldValue = node.freqMap.getOrDefault(sentence, 0);
            node.freqMap.put(sentence, oldValue + frequency);
        }
    }

    public List<String> input(char c) { // c=i ""
        if(c == '#') {
            String sentence = current.toString();
            insert(sentence,1);
            current = new StringBuilder();
            curNode = root;
            return new ArrayList<>();
        }

        current.append(c); // i
        if(curNode != null) {
            curNode = curNode.children.get(c);
        }
        if(curNode == null) {
            return new ArrayList<>();
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(curNode.freqMap.entrySet());
        list.sort((a,b) -> {
            if(!a.getValue().equals(b.getValue())) { // frequency are different
                return b.getValue() - a.getValue();
            }
            return a.getKey().compareTo(b.getKey()); // compare strings, lex order
        });

        List<String> result = new ArrayList<>();
        for(int i=0; i<Math.min(3, list.size()); i++) {
            result.add(list.get(i).getKey());
        }
        return result;
    }

    public static void main(String[] args) {
        // Initialize with given sentences and frequencies
        String[] sentences = {"i love you", "island", "iroman", "i love leetcode", "i a Bangalore"};
        int[] times = {5, 3, 2, 2, 3};

        AutocompleteSystem obj = new AutocompleteSystem(sentences, times);

        // Simulate user typing
        System.out.println(obj.input('i')); // ["i love you", "island", "i love leetcode"]
        System.out.println(obj.input(' ')); // ["i love you", "i love leetcode"]
        System.out.println(obj.input('a')); // []
        System.out.println(obj.input('#')); // [] (sentence "i a" is saved)

        // Now try typing "i a" again to check if it learned
        System.out.println(obj.input('i')); // includes "i a" now
        System.out.println(obj.input(' ')); // includes "i a" now
        System.out.println(obj.input('a')); // ["i a"]
    }
}
