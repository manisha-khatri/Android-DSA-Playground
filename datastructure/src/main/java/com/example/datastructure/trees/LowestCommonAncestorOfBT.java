package com.example.datastructure.trees;

public class LowestCommonAncestorOfBT {
    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();

        binaryTree.printLevelOrder(binaryTree.root);

        Node nd = lowestCommonAncestorOfBT(binaryTree.root, new Node("B"), new Node("F"));
        System.out.println("lowestCommonAncestorOfBT = " + nd.data);
    }

    static Node lowestCommonAncestorOfBT(Node root, Node p, Node q) {
        if(root == null) return null;
        if(root.data == p.data || root.data == q.data) return root;

        Node left = lowestCommonAncestorOfBT(root.left, p, q);
        Node right = lowestCommonAncestorOfBT(root.right, p, q);

        if(left !=null && right!=null) return root;
        if(left != null) return left;
        if(right != null) return right;

        return null;
    }
}
