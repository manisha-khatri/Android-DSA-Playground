package com.example.datastructure.linkedlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class ListNode {
    int data;
    ListNode next;
    ListNode child;

    ListNode(int data) {
        this.data = data;
        this.next = null;
        this.child = null;
    }
}

/**
 * Flattening a linked list typically means converting a multi-level linked list into a single-level
 * linked list while preserving the relative order of nodes.
 */
public class FlatteningALinkedList {

    /**
     * - Put all nodes into an array
     * - Sort them based on value
     * - Convert the array into a single linked list
     *
     * TC = O(n*m) + O(xlogx) + O(n*m)
     */
    ListNode flatteningALinkedList(ListNode head) {
        if (head == null) return null;

        List<ListNode> arr = new ArrayList<>();
        ListNode temp = head;

        while (temp != null) { // --> O(n*m)
            ListNode childNd = temp;

            while (childNd != null) {
                arr.add(childNd);
                childNd = childNd.child;
            }
            temp = temp.next;
        }

        // Sort the nodes based on data value
        Collections.sort(arr, Comparator.comparingInt(a -> a.data)); // --> O(xlogx) x--> no. of elements

        // Convert sorted array back to a linked list --> O(n*m)
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;

        for (ListNode node : arr) {
            cur.child = node;
            node.next = null;  // Ensure next pointer is disconnected
            cur = node;
        }

        return dummy.child; // Return the flattened list
    }

    /**
     *  TC = N * O(2M) = O(2NM)
     *  N --> Recursion
     *  NM --> merge while list1, list2
     *
     *  SC = O(N)
     */
    ListNode flatteningALinkedListOptimized(ListNode head) {
        if(head==null || head.next == null)
            return head;

        head.next = flatteningALinkedListOptimized(head.next);

        return merge(head, head.next);
    }

    ListNode merge(ListNode head1, ListNode head2) { // Solve for 2 lists
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;

        while(head1!=null && head2!=null) {
            if(head1.data > head2.data) {
                cur.child = head2;
                cur = head2;
                head2 = head2.child;
            } else {
                cur.child = head1;
                cur = head1;
                head1 = head1.child;
            }
        }

        if(head1!=null)
            cur.child = head1;
        else
            cur.child = head2;

        return dummy.child;
    }


    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.data + " -> ");
            head = head.child;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        FlatteningALinkedList solution = new FlatteningALinkedList();

        // Creating a multi-level linked list
        ListNode head = new ListNode(5);
        head.child = new ListNode(7);
        head.child.child = new ListNode(8);
        head.child.child.child = new ListNode(30);

        head.next = new ListNode(10);
        head.next.child = new ListNode(20);

        head.next.next = new ListNode(19);
        head.next.next.child = new ListNode(22);
        head.next.next.child.child = new ListNode(50);

        head.next.next.next = new ListNode(28);
        head.next.next.next.child = new ListNode(35);
        head.next.next.next.child.child = new ListNode(40);
        head.next.next.next.child.child.child = new ListNode(45);

        System.out.println("Flattened linked list:");
        ListNode flattenedList = solution.flatteningALinkedListOptimized(head);
        printList(flattenedList);
    }
}
