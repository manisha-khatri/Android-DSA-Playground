package com.example.ds.trees;

import java.util.*;

public class ConstructBinaryTreeFromPreorderAndInorderTraversal {

    public static void main(String[] args) {
        int[] preorder = {3,9,20,15,7};
        int[] inorder = {9,3,15,20,7};

        TreeNode root = buildTree(preorder, inorder);
    }

    static TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i=0; i<inorder.length; i++) {
            map.put(inorder[i],i);
        }

        return helper(preorder, 0, preorder.length, inorder, 0, inorder.length, map);
    }

    static TreeNode helper(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd, Map<Integer, Integer> map) {
        if(preEnd<=preStart || inEnd<=inStart) {
            return null;
        }

        int data = preorder[preStart];
        TreeNode root = new TreeNode(data);
        int rootInd = map.get(data);
        int numsLeft = rootInd-inStart;

        root.left = helper(preorder, preStart+1, preStart+numsLeft, inorder, inStart, rootInd-1, map);
        root.right = helper(preorder, preStart+numsLeft+1, preEnd, inorder, rootInd+1, inEnd, map);

        return root;
    }

}
