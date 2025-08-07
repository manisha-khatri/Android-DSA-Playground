package com.example.ds.linkedlist.types;

public class DoublyLinkedList {
     class Node {
        int data;
        Node prev;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    private Node head, tail;

     void insertAtHead(int data) {
         Node newNode = new Node(data);

         if(head == null) {
             tail = head = newNode;
         } else {
             newNode.next = head;
             head.prev = newNode;
             head = newNode;
         }
     }

     void insertAtTail(int data) {
         Node newNode = new Node(data);

         if(tail == null) {
             head = tail = newNode;
         } else {
             tail.next = newNode;
             newNode.prev = tail;
             tail = newNode;
         }
     }

     void delete(int data) {
         Node cur = head;

         while(cur!=null) {
             if(cur.data == data) {
                 if(cur == head) {
                     head = cur.next;
                     if(head != null) head.prev = null;
                 } else if (cur == tail) {
                     tail = cur.prev;
                     if(tail != null) tail.next = null;
                 } else {
                     cur.prev.next = cur.next;
                     cur.next.prev = cur.prev;
                 }
                 return;
             }

            cur = cur.next;
         }
     }

     void printForward() {
         Node cur = head;

         while(cur != null) {
             System.out.print(cur.data + " ");
             cur = cur.next;
         }
         System.out.println();
     }

     void printBackward() {
         Node cur = tail;

         while(cur != null) {
             System.out.print(cur.data + " ");
             cur = cur.prev;
         }
         System.out.println();
     }

    public static void main(String[] args) {
        DoublyLinkedList dll = new DoublyLinkedList();

        dll.insertAtHead(10);
        dll.insertAtHead(20);
        dll.insertAtTail(5);

        System.out.print("Forward: ");
        dll.printForward();   // 20 <-> 10 <-> 5 <-> null

        System.out.print("Backward: ");
        dll.printBackward();  // 5 <-> 10 <-> 20 <-> null

        dll.delete(10);
        System.out.print("After deletion: ");
        dll.printForward();   // 20 <-> 5 <-> null
    }
}
