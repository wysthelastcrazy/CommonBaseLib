package com.wys.leetcode.linkedList;

/**
 * @author wangyasheng
 * @date 2021/1/8
 * @Describe: 剑指 Offer 18. 删除链表的节点
 *
 * 给定单向链表的头指针和一个要删除的节点的值，定义一个函数删除该节点。
 *
 * 返回删除后的链表的头节点。
 *
 * 注意：此题对比原题有改动
 *
 * 示例 1:
 *
 * 输入: head = [4,5,1,9], val = 5
 * 输出: [4,1,9]
 * 解释: 给定你链表中值为 5 的第二个节点，那么在调用了你的函数之后，该链表应变为 4 -> 1 -> 9.
 * 示例 2:
 *
 * 输入: head = [4,5,1,9], val = 1
 * 输出: [4,5,9]
 * 解释: 给定你链表中值为 1 的第三个节点，那么在调用了你的函数之后，该链表应变为 4 -> 5 -> 9.
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shan-chu-lian-biao-de-jie-dian-lcof
 */
class DeleteNode {

    /**
     * 双指针求解法
     * @param head
     * @param val
     * @return
     */
    public ListNode deleteNode(ListNode head,int val){
        if (head == null)
            return null;
        //如果要删除的节点为头节点，则直接返回head.next即可
        if (head.val == val){
            return head.next;
        }
        ListNode pre = head;
        ListNode cur = pre.next;
        while (cur!=null){
            //题目中设定链表的节点值互不相同
            //则找到一个相等即可退出循环
            if (cur.val == val){
                pre.next = cur.next;
                break;
            }
            //如果没有找到，pre指针和cur指针同时往后移动
            pre = cur;
            cur = cur.next;
        }
        return head;
    }

    /**
     * 递归求解
     * @param head
     * @param val
     * @return
     */
    public ListNode deleteNode2(ListNode head,int val){
        if (head == null){
            return null;
        }
        if (head.val == val){
            return head.next;
        }
        head.next = deleteNode2(head.next,val);
        return head;
    }
}
