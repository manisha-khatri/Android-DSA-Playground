package com.example.datastructure.trees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tree with dynamic buildTree method
 * takes int array as input
 */
class Node3 {
    int data;
    Node3 left, right;

    Node3(int data) {
        this.data = data;
        left = right = null;
    }
}

public class BinaryTree3 {
    Node3 root;

    public BinaryTree3(Integer[] input) {
        // Example usage
        this.root = buildTreeFromArray(input);
    }

    public Node3 buildTreeFromArray(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) return null;

        Node3 root = new Node3(values[0]);
        Queue<Node3> queue = new LinkedList<>();
        queue.offer(root);

        int i = 1;

        while (i < values.length) {
            Node3 current = queue.poll();

            if (i < values.length && values[i] != null) {
                current.left = new Node3(values[i]);
                queue.offer(current.left);
            }
            i++;

            if (i < values.length && values[i] != null) {
                current.right = new Node3(values[i]);
                queue.offer(current.right);
            }
            i++;
        }

        return root;
    }

    public void printLevelOrder(Node3 root) {
        if (root == null) return;

        Queue<Node3> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node3 current = queue.poll();
                System.out.print(current.data + " ");

                if (current.left != null) queue.offer(current.left);
                if (current.right != null) queue.offer(current.right);
            }

            System.out.println(); // Newline for next level
        }
    }

    public static void main(String[] args) {
        Integer[] input = {3, 9, 20, null, null, 15, 7};
        BinaryTree3 tree = new BinaryTree3(input);

        System.out.println("Level Order Traversal:");
        tree.printLevelOrder(tree.root);
    }
}
