package com.wys.leetcode.array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2020/11/19
 * @Describe: 1.两数之和
 *
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，
 * 并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum
 */
class TwoSum {

    /**
     * 暴力枚举法
     * 时间复杂度：O（N^2）
     * 空间复杂度：O（1）
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum (int[] nums,int target){
        for (int i = 0;i<nums.length; i++){
            for (int j = i+1; j<nums.length; j++){
                if (nums[i]+nums[j] == target){
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }

    /**
     * 哈希表：
     * 利用哈希表存取元素时间复制度为O（1）的特性，来实现值的比较
     * 时间复杂度：O（N）
     * 空间复杂度：O（N）
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum2(int[] nums,int target){
        Map <Integer,Integer> map  = new HashMap();
        for (int i = 0;i < nums.length ; i++){
            if (map.containsKey(target - nums[i])){
                return new int[]{map.get(target-nums[i]),i};
            }
            map.put(nums[i],i);
        }
        return null;
    }
}
