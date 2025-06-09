package com.example.datastructure.trees;

/**
 * Tree with String data
 */

import java.util.LinkedList;
import java.util.Queue;

class Node {
    String data;
    Node left, right;

    Node(String data) {
        this.data = data;
        left = right = null;
    }
}

public class BinaryTree {
    Node root;

    BinaryTree() {
        this.root = buildTree();
    }


    public void printLevelOrder(Node root) {
        if (root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            // Print all nodes at current level
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                System.out.print(current.data + " ");

                if (current.left != null) queue.add(current.left);
                if (current.right != null) queue.add(current.right);
            }
            System.out.println(); // next line for next level
        }
    }


    private Node buildTree() {
        // Let's build this tree:
        //         A
        //        / \
        //       B   C
        //      / \   \
        //     D   E   F
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
        Node f = new Node("F");

        a.left = b;
        a.right = c;
        b.left = d;
        b.right = e;
        c.right = f;

        return a;
    }

    void preOrderTraversal(Node root) { //Node > Left > Right
        if(root == null) return;

        System.out.print(root.data + " ");
        preOrderTraversal(root.left);
        preOrderTraversal(root.right);
    }

    void levelOrderTraversal(Node root) {
        if(root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while(!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.data + " ");

            if(current.left != null)
                queue.offer(current.left);
            if(current.right != null)
                queue.offer(current.right);
        }
    }

    int maxDepth(Node root) {
        if(root == null) return 0;

        int left = maxDepth(root.left);
        int right = maxDepth(root.right);

        return 1 + Math.max(left, right);
    }

    boolean checkBalancedBT(Node root) {
        return findBalancedBT(root) !=-1;
    }

    int findBalancedBT(Node root) {
        if(root == null) return 0;

        int left = findBalancedBT(root.left);
        if(left==-1) return -1;

        int right = findBalancedBT(root.right);
        if(right==-1) return -1;

        if(Math.abs(left-right) > 1) return -1;

        return 1 + Math.max(left, right);
    }


    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();

        System.out.println("Preorder Traversal:");
        binaryTree.preOrderTraversal(binaryTree.root); // Output: A B D E C F

        System.out.println();

        System.out.println("Level order Traversal:");
        binaryTree.levelOrderTraversal(binaryTree.root); // Output: A B C D E F

        System.out.println();

        System.out.println("maxDepth:");
        System.out.println(binaryTree.maxDepth(binaryTree.root)); // Output: 3

        System.out.println("checkBalancedBT:");
        System.out.println(binaryTree.checkBalancedBT(binaryTree.root)); // Output: 3
    }
}
