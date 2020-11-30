package com.wys.leetcode;

import java.util.Stack;

/**
 * @author wangyasheng
 * @date 2020/11/30
 * @Describe: 剑指offer 9. 用两个栈实现队列
 * 用两个栈来实现一个队列，完成队列的 Push 和 Pop 操作。
 *
 * 解题思路：
 * in 栈用来处理入栈（push）操作，out 栈用来处理出栈（pop）操作。
 * 一个元素进入 in 栈之后，出栈的顺序被反转。当元素要出栈时，需要先进入 out 栈，
 * 此时元素出栈顺序再一次被反转，因此出栈顺序就和最开始入栈顺序是相同的，
 * 先进入的元素先退出，这就是队列的顺序。
 */
class CQueue {
    Stack<Integer> in = new Stack<>();
    Stack<Integer> out = new Stack<>();

    public void push(int value){
        in.push(value);
    }

    /**
     * 时间复杂度：O（1） 每个元素都只操作了一次入栈和出栈
     *
     * @return
     */
    public int pop(){
        if (out.isEmpty()){
            while (!in.isEmpty()){
                out.push(in.pop());
            }
        }
        if (out.isEmpty()){
            return -1;
        }
        return out.pop();
    }
}
