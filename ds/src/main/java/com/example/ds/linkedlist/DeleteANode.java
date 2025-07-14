package com.example.ds.linkedlist;

public class DeleteANode {
    static void deleteNode(Node node) {
        if (node == null || node.next == null) {
            return;
        }
        Node nextNode = node.next;
        node.data = nextNode.data;   // Copy data from next node
        node.next = nextNode.next;   // Skip the next node
    }

    public static void main(String[] args) {
        // Creating Linked List: 1 -> 2 -> 3 -> 4 -> 5
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);

        System.out.println("Original Linked List:");
        printList(head);

        // Suppose we are given node 3 (head.next.next)
        deleteNode(head.next.next);

        System.out.println("Linked List after deleting node 3:");
        printList(head);
    }

    static void printList(Node head) {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("null");
    }
}
