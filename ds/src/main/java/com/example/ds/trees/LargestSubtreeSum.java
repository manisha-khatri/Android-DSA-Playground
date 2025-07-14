package com.example.ds.trees;

public class LargestSubtreeSum {
    public static void main(String[] args) {
        BinaryTree2 binaryTree = new BinaryTree2();

        System.out.print(largestSubtreeSum(binaryTree.root));
    }

    static int maxSubtreeSum = Integer.MIN_VALUE;

    static int largestSubtreeSum(TreeNode root) {
        maxSubtreeSum = Integer.MIN_VALUE;
        helper(root);
        return maxSubtreeSum;
    }

    static int helper(TreeNode root) {
        if(root == null) return 0;

        int left = helper(root.left);
        int right = helper(root.right);

        int totalSum = Math.max(maxSubtreeSum, left + right + root.data);
        maxSubtreeSum = Math.max(totalSum, left+right+root.data);

        return totalSum;
    }
}
