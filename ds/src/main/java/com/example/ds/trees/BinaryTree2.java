package com.example.ds.trees;

/**
 * Tree with Integer data
 */

import java.util.LinkedList;
import java.util.Queue;

class TreeNode {
    int data;
    TreeNode left, right;

    TreeNode(int data) {
        this.data = data;
        left = right = null;
    }
}

public class BinaryTree2 {
    TreeNode root;

    BinaryTree2() {
        this.root = buildTree();
    }

    static public void printLevelOrder(TreeNode root) {
        if (root == null) return;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                System.out.print(current.data + " ");

                if (current.left != null) queue.add(current.left);
                if (current.right != null) queue.add(current.right);
            }
            System.out.println(); // new level
        }
    }

    private TreeNode buildTree() {
        // Building the following tree:
        //         1
        //        / \
        //       2   3
        //      / \   \
        //     4   5   6
        TreeNode a = new TreeNode(1);
        TreeNode b = new TreeNode(2);
        TreeNode c = new TreeNode(3);
        TreeNode d = new TreeNode(4);
        TreeNode e = new TreeNode(5);
        TreeNode f = new TreeNode(6);

        a.left = b;
        a.right = c;
        b.left = d;
        b.right = e;
        c.right = f;

        return a;
    }

    void preOrderTraversal(TreeNode root) {
        if (root == null) return;

        System.out.print(root.data + " ");
        preOrderTraversal(root.left);
        preOrderTraversal(root.right);
    }

    void levelOrderTraversal(TreeNode root) {
        if (root == null) return;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            System.out.print(current.data + " ");

            if (current.left != null) queue.offer(current.left);
            if (current.right != null) queue.offer(current.right);
        }
    }

    int maxDepth(TreeNode root) {
        if (root == null) return 0;

        int left = maxDepth(root.left);
        int right = maxDepth(root.right);

        return 1 + Math.max(left, right);
    }

    boolean checkBalancedBT(TreeNode root) {
        return findBalancedBT(root) != -1;
    }

    int findBalancedBT(TreeNode root) {
        if (root == null) return 0;

        int left = findBalancedBT(root.left);
        if (left == -1) return -1;

        int right = findBalancedBT(root.right);
        if (right == -1) return -1;

        if (Math.abs(left - right) > 1) return -1;

        return 1 + Math.max(left, right);
    }

    public static void main(String[] args) {
        BinaryTree2 binaryTree = new BinaryTree2();

        System.out.println("Preorder Traversal:");
        binaryTree.preOrderTraversal(binaryTree.root); // Output: 1 2 4 5 3 6

        System.out.println();

        System.out.println("Level order Traversal:");
        binaryTree.levelOrderTraversal(binaryTree.root); // Output: 1 2 3 4 5 6

        System.out.println();

        System.out.println("maxDepth:");
        System.out.println(binaryTree.maxDepth(binaryTree.root)); // Output: 3

        System.out.println("checkBalancedBT:");
        System.out.println(binaryTree.checkBalancedBT(binaryTree.root)); // Output: true
    }
}
