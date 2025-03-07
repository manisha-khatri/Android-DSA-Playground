package com.example.datastructure.linkedlist;

public class ReverseLinkedList {
    public static void main(String[] args) {
        CustomLinkedList list = new CustomLinkedList();
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insert(40);

        System.out.println("Original List:");
        list.display(); // 10 -> 20 -> 30 -> 40 -> null
        System.out.println();

        System.out.println("Reversed List Iterative:");
        list.head = reverse(list.head);
        list.display(); // 10 -> 20 -> 30 -> 40 -> null
        System.out.println();

        System.out.println("Reversed List:");
        list.head = reverseRec(list.head);
        list.display(); // 10 -> 20 -> 30 -> 40 -> null
        System.out.println();

    }

    /**
     * Iterative Approach
     * 3 pointers (prev, cur, next)
     */
    static Node reverse(Node head) {
        Node prev = null;
        Node cur = head;

        while(cur != null) {
            Node nxt = cur.next;
            cur.next = prev;
            prev = cur;
            cur = nxt;
        }
        return prev;
    }

    static Node reverseRec(Node head) {
        if(head == null || head.next == null)
            return head;

        Node newHead = reverseRec(head.next);
        Node k = head.next;
        k.next = head;
        head.next = null;

        return newHead;
    }


}
