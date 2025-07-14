package com.example.ds.linkedlist;

import java.util.PriorityQueue;

public class MergeKSortedListsWithPriorityQueue {

    Node mergeKSortedListsWithPriorityQueue(Node[] lists) {
        if(lists == null || lists.length==0) return null;

        PriorityQueue<Node> minHeap = new PriorityQueue<>((a,b) -> a.data - b.data);

        for (Node head : lists) {
            if(head!=null) {
                minHeap.add(head);
            }
        }

        Node dummy = new Node(0);
        Node cur = dummy;

        while(!minHeap.isEmpty()) {
            Node minNode = minHeap.poll();
            cur.next = minNode;
            cur = minNode;

            if(minNode.next != null) {
                minHeap.add(minNode.next);
            }
        }
        return dummy.next;
    }

    static void printList(Node head) {
        while (head != null) {
            System.out.print(head.data + " -> ");
            head = head.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        MergeKSortedListsWithPriorityQueue obj = new MergeKSortedListsWithPriorityQueue();

        // Creating 3 sorted linked lists
        Node list1 = new Node(1);
        list1.next = new Node(4);
        list1.next.next = new Node(7);

        Node list2 = new Node(2);
        list2.next = new Node(5);
        list2.next.next = new Node(8);

        Node list3 = new Node(3);
        list3.next = new Node(6);
        list3.next.next = new Node(9);

        // Storing the heads in an array
        Node[] heads = {list1, list2, list3};

        // Merging K sorted lists
        Node mergedHead = obj.mergeKSortedListsWithPriorityQueue(heads);

        // Printing the merged list
        System.out.println("Merged Sorted Linked List:");
        printList(mergedHead);
    }

}
