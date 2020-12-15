package com.wys.leetcode.linkedList;

/**
 * @author wangyasheng
 * @date 2020/12/15
 * @Describe:反转链表
 */
class ReverseList {
    /**
     * 方法一：迭代（双指针）头插法
     * 时间复杂度O(N)：遍历链表使用线性大小时间
     * 空间复杂度O(1)：变量pre使用常数大小额外空间
     * @param head
     * @return
     */
    public ListNode reverseList1(ListNode head) {
        ListNode pre = null;
        while (head!=null){
            //暂存后继节点
            ListNode next = head.next;
            //修改next引用指向
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    /**
     * 方法二：递归
     * 时间复杂度O(N)：遍历链表使用线性大小时间
     * 空间复杂度O(N)：遍历链表的递归深度达到N，系统使用O(N)大小额外空间
     * @param head
     * @return
     */
    public ListNode reverseList2(ListNode head){
        if (head == null || head.next == null){
            return head;
        }
        ListNode next = head.next;
        head.next = null;
        ListNode newHead = reverseList2(next);
        next.next = head;
        return newHead;
    }
}
