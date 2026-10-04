package com.interview.tree.model;

import java.util.*;

/** Shared graph node for Clone Graph. Trie implementations own separate node types. */
public class Node {
    public int val;
    public List<Node> neighbors;
    public Node() { this(0); }
    public Node(int val) { this.val=val; this.neighbors=new ArrayList<>(); }
    public Node(int val, ArrayList<Node> neighbors) { this.val=val; this.neighbors=neighbors; }
}
