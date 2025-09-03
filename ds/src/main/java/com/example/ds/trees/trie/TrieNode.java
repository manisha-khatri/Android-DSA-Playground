package com.example.ds.trees.trie;

public class TrieNode {
    TrieNode[] children = new TrieNode[26]; // a-z
    boolean isEndOfWord = false;
}

class Trie {
    TrieNode root;

    Trie() {
        root = new TrieNode();
    }

    // Insert a word into the trie
    public void insert(String word) {
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            int index = c - 'a'; // assuming lowercase
            if (node.children[index] == null)
                node.children[index] = new TrieNode();
            node = node.children[index];
        }
        node.isEndOfWord = true;
    }

    boolean searchWordWithComma(String word) {
        return searchWithComma(word, 0, root);
    }

    private boolean searchWithComma(String word, int i, TrieNode node) {
        if(word.length() == i) {
            return node.isEndOfWord;
        }

        char ch = word.charAt(i);

        if(ch == '.') {
           for(TrieNode child : node.children) {
               if(child!=null && searchWithComma(word, i+1, child)) {
                   return true;
               }
           }
        } else {
            int indx = ch - 'a';

            if(node.children[indx] == null) return false;
            node = node.children[indx];

            return searchWithComma(word, i+1, node);
        }
        return false;
    }

    // Search
    public boolean search(String str) {
        TrieNode node = root;

        for(char ch: str.toCharArray()) {
            int index = ch - 'a';

            if(node.children[index] == null)
                return false;

            node = node.children[index];
        }
        return node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;

        for(char ch: prefix.toCharArray()) {
            int index = ch - 'a';
            if(node.children[index] == null)
                return false;

            node = node.children[index];
        }
        return true;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();

        trie.insert("apple");
        trie.insert("app");
        trie.insert("bat");
        trie.insert("batman");

        System.out.println("Search 'apple': " + trie.search("apple")); // true
        System.out.println("Search 'app': " + trie.search("app"));     // true
        System.out.println("Search 'ap': " + trie.search("ap"));       // false
        System.out.println("StartsWith 'ap': " + trie.startsWith("ap")); // true
        System.out.println("Search 'batman': " + trie.search("batman")); // true
        System.out.println("StartsWith 'bat': " + trie.startsWith("bat")); // true
        System.out.println("Search 'battle': " + trie.search("battle")); // false

        System.out.println("searchWordWithComma : " + trie.searchWordWithComma("..ple"));
    }
}