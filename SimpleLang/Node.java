package com.batswen.simplelang;

class Node {
        public NodeType nt;
        public Node left, right;
        public String value;
        public int line;
        public int pos;

        Node() {
            this.nt = null;
            this.left = null;
            this.right = null;
            this.value = null;
            this.line = 0;
            this. pos = 0;
        }
        Node(NodeType node_type, Node left, Node right, String value, int line, int pos) {
            this.nt = node_type;
            this.left = left;
            this.right = right;
            this.value = value;
            this.line = line;
            this. pos = pos;
        }
        public static Node make_node(NodeType nodetype, Node left, Node right, int line, int pos) {
            return new Node(nodetype, left, right, "", line, pos);
        }
        public static Node make_node(NodeType nodetype, Node left, int line, int pos) {
            return new Node(nodetype, left, null, "", line, pos);
        }
        public static Node make_leaf(NodeType nodetype, String value, int line, int pos) {
            return new Node(nodetype, null, null, value, line, pos);
        }
        @Override
        public String toString() {
            return this.nt.toString() + ", Line: " + this.line + ", Pos: " + this.pos;
        }
    }
