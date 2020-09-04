package com.wys.interview.leetcode

import java.lang.Exception
import kotlin.math.min

/**
 * @author wangyasheng
 * @date 2020/9/3
 * @Describe:最小栈（O(1)时间复杂度获取栈中最小的元素
 *           使用链表实现
 */
class MinStack {
    private var head:Node? = null
    fun push(value:Int){
        head = Node(value, min(value,head?.min?:value),head)
    }
    fun pop(){
        if (head == null){
            throw Exception("stack is empty can not pop")
        }
        head = head?.next
    }
    fun top():Int{
        return head?.value?:throw Exception("stack is empty")
    }
    fun getMin():Int{
        return head?.min?:throw Exception("stack is empty")
    }
    private class Node(val value:Int,val min:Int,var next:Node?) {
        constructor(value: Int,min: Int):this(value,min,null)
    }
}