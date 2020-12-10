package com.wys.leetcode.array;

/**
 * @author wangyasheng
 * @date 2020/11/23
 * @Describe:数组中重复的数字
 *
 * 找出数组中重复的数字。
 *
 * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。
 * 数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。
 * 请找出数组中任意一个重复的数字。
 *
 * 示例 1：
 *
 * 输入：
 * [2, 3, 1, 0, 2, 5, 3]
 * 输出：2 或 3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof
 */
class FindRepeatNumber {
    /**
     * 对于这种数组元素在 [0, n-1] 范围内的问题，
     * 可以将值为 i 的元素调整到第 i 个位置上进行求解。
     * 在调整过程中，如果第 i 位置上已经有一个值为 i 的元素，就可以知道 i 值重复。
     * 时间复杂度：O（N）
     * 空间复杂度：O（1）
     * @param nums
     * @return
     */
    public static int findRepeatNumber(int[] nums){
        for(int i=0;i<nums.length;i++){
            //如果坐标为i的元素值不等于i，则把该值放到其对应的位置即nums[i]位置
            //如果放置时在该位置已存在相等值，则返回该值，是为所求
            while (nums[i]!=i){
                if(nums[i]==nums[nums[i]]){
                    return nums[i];
                }
                swap(nums,i,nums[i]);
            }
        }
        return -1;
    }
    private static void swap(int[] nums,int i,int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
}
