package com.example.datastructure.trie;

class TrieNode1 {
    TrieNode1[] children = new TrieNode1[26];
    boolean endOfWord = false;
}

class Trie1 {
    TrieNode1 root;

    Trie1() {
        root = new TrieNode1();
    }

    void insert(String word) {
        if(word == null) return;

        TrieNode1 temp = root;
        for(int i=0; i<word.length(); i++) {
            char ch = word.charAt(i);
            int indx = ch - 'a';

            if(temp.children[indx] == null) {
                temp.children[indx] = new TrieNode1();
            }
            temp = temp.children[indx];
            if(i == word.length()-1) temp.endOfWord = true;
        }
    }

    boolean search(String word) {
        if(word == null) return false;

        TrieNode1 temp = root;
        for(int i=0; i<word.length(); i++) {
            char ch = word.charAt(i);
            int indx = ch - 'a';

            if(temp.children[indx] == null) return false;

            temp = temp.children[indx];
        }

        return temp.endOfWord;
    }

    boolean startsWith(String word) {
        TrieNode1 node = root;

        for(int i=0; i<word.length(); i++) {
            char ch = word.charAt(i);
            int indx = ch - 'a';
            if(node.children[indx] == null) return false;
            node = node.children[indx];
        }
        return true;
    }

    public static void main(String[] args) {
        Trie1 trie = new Trie1();

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
    }
}


