package com.wys.leetcode.stack;

import java.util.Stack;

/**
 * @author wangyasheng
 * @date 2020/12/10
 * @Describe:剑指offer 30. 包含 min 函数的栈
 *
 * 实现一个包含 min() 函数的栈，该方法返回当前栈中最小的值。
 */
class MinStack {
    Stack<Integer> A,B;
    public MinStack(){
        A = new Stack<>();
        B = new Stack<>();
    }

    /**
     * 入栈
     * @param value
     */
    public void push(int value){
        A.push(value);
        if (B.empty() || B.peek() >= value){
            B.push(value);
        }
    }

    /**
     * 出栈
     * @return
     */
    public void pop(){
        //Java中由于Stack中存储的是int的包装类Integer，
        // 因此需要使用equals()代替 == 来比较值是否相等。
        // == 如果在[-128,127]会被cache缓存，超过这个范围
        //则比较的是对象是否相同。
        if (A.pop().equals(B.peek())){
            B.pop();
        }
    }

    /**
     * 获取栈顶元素
     * @return
     */
    public int top(){
        return A.peek();
    }

    /**
     * 获取栈内最小元素
     * @return
     */
    public int min(){
        return B.peek();
    }
}
