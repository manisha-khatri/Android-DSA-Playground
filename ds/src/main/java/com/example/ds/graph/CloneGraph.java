package com.example.ds.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Node {
    public int val;
    public List<Node> neighbors;

    Node() {
        val = 0;
        neighbors = new ArrayList<>();
    }

    Node(int val) {
        this.val = val;
        neighbors = new ArrayList<>();
    }

    Node(int val, List<Node> neighbors) {
        this.val = val;
        this.neighbors = neighbors;
    }
}

public class CloneGraph {

    Map<Node, Node> cloned = new HashMap<>();

    public Node cloneGraph(Node node) {
        if(node == null) return node;

        if(cloned.containsKey(node)) {
            return cloned.get(node);
        }

        Node copy = new Node(node.val);
        cloned.put(node, copy);

        for(Node neighbor: node.neighbors) {
            copy.neighbors.add(cloneGraph(neighbor));
        }

        return copy;
    }


    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        node1.neighbors.add(node2);
        node1.neighbors.add(node4);

        node2.neighbors.add(node1);
        node2.neighbors.add(node3);

        node3.neighbors.add(node2);
        node3.neighbors.add(node4);

        node4.neighbors.add(node1);
        node4.neighbors.add(node3);

        CloneGraph graph = new CloneGraph();
        Node clone = graph.cloneGraph(node1);

        System.out.println("Cloned Node: " + clone.val); // Output: 1
    }
}
