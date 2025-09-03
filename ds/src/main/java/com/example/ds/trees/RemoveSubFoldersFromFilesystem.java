package com.example.ds.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    Map<String, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

public class RemoveSubFoldersFromFilesystem {
    TrieNode root;

    public List<String> removeSubfolders(String[] folder) {
        root = new TrieNode();

        // Insert each folder
        for (String path : folder) {
            insert(path);
        }

        // Collect results
        List<String> result = new ArrayList<>();
        dfs(root, new ArrayList<>(), result);
        return result;
    }

    private void insert(String path) {
        TrieNode node = root;
        String[] parts = path.split("/");

        for (String part : parts) {
            if (part.isEmpty()) continue; // skip leading ""

            if (node.isEnd) {
                // Parent folder already exists, so ignore deeper ones
                return;
            }

            node.children.putIfAbsent(part, new TrieNode());
            node = node.children.get(part);
        }

        // Mark this folder as a parent
        node.isEnd = true;
        node.children.clear(); // prune any deeper subfolders
    }

    private void dfs(TrieNode node, List<String> path, List<String> result) {
        if (node.isEnd) {
            // Construct the full path
            StringBuilder sb = new StringBuilder();
            for (String p : path) {
                sb.append("/").append(p);
            }
            result.add(sb.toString());
            return; // don’t explore deeper
        }

        for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
            path.add(entry.getKey());
            dfs(entry.getValue(), path, result);
            path.remove(path.size() - 1);
        }
    }

    // ---- MAIN METHOD FOR TESTING ----
    public static void main(String[] args) {
        RemoveSubFoldersFromFilesystem sol = new RemoveSubFoldersFromFilesystem();

        String[] folders1 = {"/a", "/a/b", "/c/d", "/c/d/e", "/c/f"};
        System.out.println(sol.removeSubfolders(folders1));
        // Expected: ["/a", "/c/d", "/c/f"]

        String[] folders2 = {"/a/b/c", "/a/b/ca", "/a/b/d"};
        System.out.println(sol.removeSubfolders(folders2));
        // Expected: ["/a/b/c", "/a/b/ca", "/a/b/d"]

        String[] folders3 = {"/a/b/c", "/a", "/b", "/b/c/d", "/c/d", "/c/d/e", "/c/f"};
        System.out.println(sol.removeSubfolders(folders3));
        // Expected: ["/a", "/b", "/c/d", "/c/f"]
    }
}
