package com.example.ds.linkedlist;

public class MergeKSortedLists {

    Node mergeKSortedLists(Node[] heads) {
        if (heads == null || heads.length == 0)
            return null;

        for (int i = 1; i < heads.length; i++) {
            heads[0] = merge(heads[0], heads[i]);
        }

        return heads[0];
    }

    Node merge(Node head1, Node head2) {
        Node dummy = new Node(0);
        Node cur = dummy;

        while (head1 != null && head2 != null) {
            if (head1.data < head2.data) {
                cur.next = head1;
                head1 = head1.next;
            } else {
                cur.next = head2;
                head2 = head2.next;
            }
            cur = cur.next; // Move cur forward
        }

        if (head1 != null) {
            cur.next = head1;
        } else {
            cur.next = head2;
        }

        return dummy.next;
    }

    // Helper function to print the linked list
    static void printList(Node head) {
        while (head != null) {
            System.out.print(head.data + " -> ");
            head = head.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        MergeKSortedLists obj = new MergeKSortedLists();

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
        Node mergedHead = obj.mergeKSortedLists(heads);

        // Printing the merged list
        System.out.println("Merged Sorted Linked List:");
        printList(mergedHead);
    }
}
