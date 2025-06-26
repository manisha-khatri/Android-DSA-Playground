package com.example.datastructure.linkedlist;

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        next = null;
    }
}

class CustomLinkedList {
    Node head;

    public void insert(int data) {
        Node newNode = new Node(data);
        if(head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while(temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    void delete(int data) {
        if(head == null) return;

        Node temp = head;
        while(temp.next != null && temp.next.data != data) {
            temp = temp.next;
        }
        if(temp.next == null) return;

        temp.next = temp.next.next;
    }

    void deleteByPosition(int pos) {
        if(head == null) return;
        if(pos == 0) {
            head = head.next;
            return;
        }

        Node temp = head;
        int index = 0;
        while(temp.next != null && index+1 != pos) {
            temp = temp.next;
            index++;
        }
        if(temp.next == null) return;
        temp.next = temp.next.next;
    }

    void display() {
        if(head == null) return;

        Node temp = head;
        while(temp.next != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.print(temp.data + " ");
    }

    void createCycle(int pos) {
        if(pos < 0 || head == null) return;

        Node newNode = null;
        int index = 1;
        Node temp = head;
        while(temp.next != null) {
            temp = temp.next;
            index++;
            if(pos==index) newNode = temp;
        }
        if(newNode!=null) {
            temp.next = newNode;
        }
    }
}

class Main {
    public static void main(String[] args) {
        CustomLinkedList list = new CustomLinkedList();
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insert(40);

        System.out.println("Original List:");
        list.display(); // 10 -> 20 -> 30 -> 40 -> null
        System.out.println();

        list.delete(20);
        System.out.println("After deleting 20:");
        list.display(); // 10 -> 30 -> 40 -> null
        System.out.println();

        list.deleteByPosition(1);
        System.out.println("After deleting at position 1:");
        list.display(); // 10 -> 40 -> null
    }
}
















