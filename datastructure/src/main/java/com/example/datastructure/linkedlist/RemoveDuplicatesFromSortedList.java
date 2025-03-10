package com.example.datastructure.linkedlist;

public class RemoveDuplicatesFromSortedList {

    static Node removeDuplicatesFromSortedList(Node head) {
        if(head == null || head.next == null) return head;

        Node cur = head;

        while(cur.next != null) {
            if(cur.data == cur.next.data) {
                cur.next = cur.next.next;
            }
            else {
                cur = cur.next;
            }
        }
        return head;
    }

    public static void main(String[] args) {
        CustomLinkedList list1 = new CustomLinkedList();
        list1.insert(1);
        list1.insert(1);
        list1.insert(3);
        list1.insert(3);
        list1.insert(4);
        list1.insert(5);

        System.out.println("First Sorted List:");
        list1.display();
        System.out.println();

        System.out.println("removeDuplicatesFromSortedList:");
        list1.head = removeDuplicatesFromSortedList(list1.head);
        list1.display();
        System.out.println();
    }
}
