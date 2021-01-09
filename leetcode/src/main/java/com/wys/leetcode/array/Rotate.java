package com.wys.leetcode.array;

/**
 * @author wangyasheng
 * @date 2021/1/8
 * @Describe: 旋转数组
 *
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 *
 * 示例 :
 *
 * 输入: [1,2,3,4,5,6,7] 和 k = 3
 * 输出: [5,6,7,1,2,3,4]
 * 解释:
 * 向右旋转 1 步: [7,1,2,3,4,5,6]
 * 向右旋转 2 步: [6,7,1,2,3,4,5]
 * 向右旋转 3 步: [5,6,7,1,2,3,4]
 *
 * 说明:
 *
 * 尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
 * 要求使用空间复杂度为 O(1) 的 原地 算法。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/rotate-array
 */
class Rotate {

    public void rotate(int[] nums, int k) {
        if (nums == null || nums.length <= 0){
            return;

        }
        int size = nums.length;
        //如果需要移动的位置大于数组大小时，实际移动
        //位置计算
        k  = k % size;
        for (int j = 0; j < k; j++) {
            int temp = nums[size - 1];
            for (int i = size - 1; i > 0; i--) {
                nums[i] = nums[i - 1];
            }
            nums[0] = temp;
        }
    }

    /**
     * 方法1：数组翻转
     *
     * 该方法基于如下的事实：当我们将数组的元素向右移动k次后，
     * 尾部k mod n个元素会移动至数组头部，其余元素向后移动 k mod n个位置。
     *
     * 该方法为数组的翻转：我们可以先将所有元素翻转，这样尾部的
     * k mod n个元素就被移至数组头部，然后我们再翻转[0,k mode n -1]区间元素
     * 和[k mod n,n-1]区间的元素就可以得到答案。
     *
     * 时间复杂度：每个元素被翻转两次总时间复杂度为O(2n) = O(n)
     * 空间复杂度：O（1）
     * @param nums
     * @param k
     */
    public void rotate1(int[] nums,int k){
        k %= nums.length;
        reverse(nums,0,nums.length -1);
        reverse(nums,0,k -1);
        reverse(nums,k,nums.length - 1);
    }

    /**
     * 翻转数组指定部分
     * @param nums
     * @param start
     * @param end
     */
    private void reverse(int nums[],int start,int end){
        while (start < end){
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start += 1;
            end -= 1;
        }
    }
}

