package com.example.ds.linkedlist;

public class ReverseNodesInKGroup {

    static Node reverseNodesInKGroup1(Node head, int k) {
        if(head==null || head.next==null) return head;
        if(k<=1) return head;

        /**
         * 1. Find start and end
         *      - if (end->indx!=k) return Node
         * 2. start.next = Call reverse(start, end)
         */

        Node startKth, endKth, temp = head, newHead = null, nxtNode, prevNode;

        while(temp != null) {

            // Finding start and end of kth
            startKth = temp;
            for(int i=0; i<k; i++){
                temp = temp.next;
            }
            endKth = temp;

            //Reverse
            nxtNode = endKth.next;
            endKth.next = null;
            reverse(startKth);

            //update newHead
            if(newHead==null)
                newHead = endKth;

            //keep the last kth node pointing to null
            prevNode = startKth;

            temp = nxtNode;
        }
        return newHead;
    }

    static Node reverseNodesInKGroup(Node head, int k) {
        Node temp = head;
        Node prevLast = null;

        while(temp != null) {
            Node kthNode = findKthNode(temp, k);
            if(kthNode == null) {
                if(prevLast!=null) prevLast.next = temp;
                break;
            }
            Node nxtNode = kthNode.next;
            kthNode.next = null;
            reverse(temp);

            if(temp == head) {
                head = kthNode;
            } else {
                prevLast.next = kthNode;
            }
            prevLast = temp;
            temp = nxtNode;
        }
        return head;
    }

    static Node findKthNode(Node temp, int k) {
        k-=1;
        while(k>0 && temp!=null) {
            temp = temp.next;
            k--;
        }
        return temp;
    }

    static Node reverse(Node head) {
       Node prev=null, cur=head, nxt;

       while(cur!=null) {
           nxt = cur.next;
           cur.next = prev;
           prev = cur;
           cur = nxt;
       }
       return prev;
    }

    public static void main(String[] args) {
        CustomLinkedList list = new CustomLinkedList();
        list.insert(1);
        list.insert(2);
        list.insert(3);
        list.insert(4);
        list.insert(5);
        list.insert(6);
        list.insert(7);
        list.insert(8);

        System.out.println("Original List:");
        list.display();
        System.out.println();

        System.out.println("reverseNodesInKGroup :");
        list.head = reverseNodesInKGroup(list.head, 3);
        list.display();

    }
}
