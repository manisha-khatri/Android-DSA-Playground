package com.example.datastructure.linkedlist;

public class MergeTwoSortedLinkedLists {

    static Node MergeTwoSortedLinkedLists(Node head1, Node head2) {
        if(head1 == null) return head2;
        if(head2 == null) return head1;

        Node resultHead, tempRes;
        if(head1.data < head2.data) {
            resultHead = head1;
            head1 = head1.next;
        } else {
            resultHead = head2;
            head2 = head2.next;
        }
        tempRes = resultHead;

        while (head1 != null && head2 != null) {
            if(head1.data < head2.data) {
                tempRes.next = head1;
                head1 = head1.next;
            } else {
                tempRes.next = head2;
                head2 = head2.next;
            }
            tempRes = tempRes.next;
        }

        if (head1 != null) tempRes.next = head1;
        if (head2 != null) tempRes.next = head2;

        return resultHead;
    }

    // Utility function to display a linked list
    static void displayList(Node head) {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        // Create first sorted linked list: 1 -> 3 -> 5 -> 7
        CustomLinkedList list1 = new CustomLinkedList();
        list1.insert(1);
        list1.insert(3);
        list1.insert(5);
        list1.insert(7);

        System.out.println("First Sorted List:");
        list1.display();
        System.out.println();

        // Create second sorted linked list: 2 -> 4 -> 6 -> 8
        CustomLinkedList list2 = new CustomLinkedList();
        list2.insert(2);
        list2.insert(4);
        list2.insert(6);
        list2.insert(8);

        System.out.println("Second Sorted List:");
        list2.display();
        System.out.println();

        // Merge two sorted linked lists
        Node mergedHead = MergeTwoSortedLinkedLists(list1.head, list2.head);

        // Display the merged linked list
        System.out.println("Merged Sorted List:");
        displayList(mergedHead);
    }
}
