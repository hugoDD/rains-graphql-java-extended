package com.extended.graphql.sample.schema;

import java.util.Arrays;

public class Tree {

    public static Node root() {
        Node root = new Node(0, -1, "root");
        Node node1 = new Node(1, 0, "node1");
        Node node2 = new Node(2, 0, "node2");
        Node node10 = new Node(3, 1, "node1-0");
        Node node11 = new Node(4, 1, "node1-1");
        Node node20 = new Node(5, 2, "node2-0");

        root.setChildren(Arrays.asList(node1, node2));
        node1.setChildren(Arrays.asList(node10, node11));
        node2.setChildren(Arrays.asList(node20));


        return root;

    }
}
