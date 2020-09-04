package com.wys.interview.leetcode;

class MinStack1 {
    private Node header;
    /** initialize your data structure here. */
    public MinStack1() {

    }

    public void push(int x) {
        if(header == null){
            header = new Node(x,x,null);
        }else{
            header = new Node(x,Math.min(x,header.min),header);
        }
    }

    public void pop() {
        if(header!=null){
            header = header.next;
        }
    }

    public int top() throws Exception {
        if(header == null) throw new Exception("this stack is empty");
        return header.val;
    }

    public int getMin() throws Exception {
        if(header == null) throw new Exception("this stack is empty");
        return header.min;
    }
    class Node{
        int val;
        int min;
        Node next;
        public Node(int val,int min,Node next){
            this.val = val;
            this.min = min;
            this.next = next;
        }
    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(x);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */