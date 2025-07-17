package com.example.ds.trees;


public class BinaryTreeMaximumPathSum {

    static int maxSum = Integer.MIN_VALUE;

    static public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }

    static int maxGain(TreeNode root) {
        if(root == null)
            return 0;

        int left = Math.max(maxGain(root.left), 0);
        int right = Math.max(maxGain(root.right), 0);

        int localMaxVal = root.data + left + right;

        maxSum = Math.max(maxSum, localMaxVal);

        return root.data + Math.max(left, right);
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);


        System.out.println("Maximum Path Sum: " + maxPathSum(root)); // Output: 6

    }
}
