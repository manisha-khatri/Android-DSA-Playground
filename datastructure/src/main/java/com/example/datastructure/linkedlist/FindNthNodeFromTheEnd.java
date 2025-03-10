package com.example.datastructure.linkedlist;

public class FindNthNodeFromTheEnd {

    static Node findNthNodeFromTheEnd(Node head, int n) {
        if(head == null) return null;

        Node slow = head, fast = head;

        for(int i=0; i<n; i++) {
            if (fast == null) return null; // If n > length of list
            fast = fast.next;
        }
        while (fast.next!=null) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        CustomLinkedList list1 = new CustomLinkedList();
        list1.insert(1);
        list1.insert(2);
        list1.insert(3);
        list1.insert(4);
        list1.insert(5);
        list1.insert(6);

        System.out.println("First Sorted List:");
        list1.display();
        System.out.println();

        System.out.println("findNthNodeFromTheEnd:" + findNthNodeFromTheEnd(list1.head, 3).data);
        System.out.println();
    }
}
