package com.example.datastructure.linkedlist;

public class FindIntersectionPointOfTwoLists {

    static Node findIntersectionPointOfTwoLists(Node head1, Node head2) {
        if (head1 == null || head2 == null) return null;

        int length1 = calculateLength(head1);
        int length2 = calculateLength(head2);
        int leaveLength = Math.abs(length1 - length2);

        // Move the longer list's pointer ahead by leaveLength
        for (int i = 0; i < leaveLength; i++) {
            if (length1 > length2) {
                head1 = head1.next;
            } else {
                head2 = head2.next;
            }
        }

        // Traverse both lists together until an intersection is found
        while (head1 != null && head2 != null) {
            if (head1 == head2) return head1; // Found intersection
            head1 = head1.next;
            head2 = head2.next;
        }
        return null; // No intersection
    }

    static int calculateLength(Node head) {
        int length = 0;
        while (head != null) {  // Corrected condition
            length++;
            head = head.next;
        }
        return length;
    }

    public static void main(String[] args) {
        // Creating two linked lists with an intersection
        Node commonNode = new Node(8);
        commonNode.next = new Node(10);

        Node head1 = new Node(3);
        head1.next = new Node(6);
        head1.next.next = new Node(9);
        head1.next.next.next = commonNode; // Intersection starts here

        Node head2 = new Node(4);
        head2.next = commonNode; // Intersection starts here

        System.out.println("Intersection Point: " + findIntersectionPointOfTwoLists(head1, head2).data);
    }
}
