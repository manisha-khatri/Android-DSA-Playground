package com.example.datastructure.interview;

import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int value;
    List<TreeNode> children;

    TreeNode(int value) {
        this.value = value;
        this.children = new ArrayList<>();
    }
}

class ArrayComparisonTree {
    public static void main(String[] args) {
        int[] arr = {1, 6, 2, 7, 2};
        generateSubsets(arr, 0, new ArrayList<>());
    }

    public static void generateSubsets(int[] arr, int index, List<Integer> currentSubset) {
        if (index == arr.length) {
            System.out.println(currentSubset);
            return;
        }

        // Exclude the current element
        generateSubsets(arr, index + 1, new ArrayList<>(currentSubset));

        // Include the current element
        currentSubset.add(arr[index]);
        generateSubsets(arr, index + 1, new ArrayList<>(currentSubset));
    }
}
