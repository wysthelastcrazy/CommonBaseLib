package com.wys.leetcode.linkedList;

import java.util.Stack;

/**
 * @author wangyasheng
 * @date 2020/12/15
 * @Describe:剑指offer 06 - 从尾到头打印链表
 *
 * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
 *
 * 示例 1：
 *
 * 输入：head = [1,3,2]
 * 输出：[2,3,1]
 */
class ReversePrint {

    /**
     * 方法一：栈
     * 栈的特点是后进先出，即最后入栈的元素最先弹出。可以
     * 使用用栈将链表元素的顺序倒置。从链表头节点开始依次
     * 进栈，在把栈中元素依次弹出并存储到数组中。
     * 时间复杂度：O(N)：正向遍历一遍链表，然后从栈弹出等于反向再遍历依次链表
     * 空间复杂度O(N)：额外使用一个栈存储链表中的每个元素
     * @param head
     * @return
     */
    public int[] reversePrint1(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        //使用临时变量，是为了保证原链表结构不变
        ListNode cur = head;
        while (cur!=null){
            stack.push(cur.val);
            cur = cur.next;
        }
        int size = stack.size();
        int[] print = new int[size];
        for (int i = 0;i<size;i++){
            print[i] = stack.pop();
        }
        return print;
    }

    /**
     * 方法二：双循环
     * 时间复杂度O(N):需要遍历两次链表
     * 空间复杂度O(1):除了返回的数组，只有常量的局部变量
     * @param head
     * @return
     */
    public int[] reversePrint2(ListNode head){
        ListNode cur = head;
        int size = 0;
        while (cur!=null){
            size ++;
            cur = cur.next;
        }
        int[] print = new int[size];
        cur = head;
        while (cur!=null){
            print[size - 1] = cur.val;
            size --;
            cur = cur.next;
        }
        return print;
    }
}
