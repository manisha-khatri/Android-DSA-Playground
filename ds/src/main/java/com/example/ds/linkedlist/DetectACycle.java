package com.example.ds.linkedlist;

public class DetectACycle {

    static boolean isCyclePresent(Node head) {
        if(head == null || head.next == null)
            return false;
        Node fast = head, slow = head;
        while(fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow) return true;
        }
        return false;
    }

    static Node findMiddle(Node head) {
        if(head == null || head.next == null)
            return head;

        Node fast, slow;
        fast = slow = head;

        while(fast!=null && fast.next!=null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    public static void main(String[] args) {
        CustomLinkedList list = new CustomLinkedList();
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insert(40);

        System.out.println("Original List:");
        list.display(); // 10 -> 20 -> 30 -> 40 -> null
        System.out.println();

        System.out.println("findMiddle :" + findMiddle(list.head).data);
        System.out.println();

        System.out.println("isCyclePresent :" + isCyclePresent(list.head));
        System.out.println();

        list.createCycle(2);
        System.out.println("isCyclePresent :" + isCyclePresent(list.head));
        System.out.println();


    }
}
