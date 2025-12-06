package com.example.ds.trees;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class zigzagLevelOrder {
    static public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        if(root == null) return result;
        queue.offer(root);

        boolean leftToRight = true;
        while(!queue.isEmpty()) {
            int size = queue.size();
            Deque<Integer> level = new LinkedList<>();

            for(int i=0; i<size; i++) {
                TreeNode node = queue.poll();
                if(leftToRight) {
                    level.addLast(node.data);
                } else {
                    level.addFirst(node.data);
                }
                if(node.left!=null) queue.offer(node.left);
                if(node.right!=null) queue.offer(node.right);
            }
            leftToRight = !leftToRight;
            result.add(new ArrayList<>(level));
        }
        return result;
    }

    public static void main(String[] args) {

        // Creating sample tree:
        //        3
        //       / \
        //      9  20
        //         /  \
        //        15   7

        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        List<List<Integer>> result = zigzagLevelOrder(root);

        System.out.println("Zigzag Level Order Traversal:");
        System.out.println(result);
    }
}
