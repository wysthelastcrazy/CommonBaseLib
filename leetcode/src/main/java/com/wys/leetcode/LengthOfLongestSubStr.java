package com.wys.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyasheng
 * @date 2020/11/19
 * @Describe: 3.无重复字符的最长字串
 *
 * 给定一个字符串，请你找出其中不含有重复字符的最长子串的长度
 *
 * 示例 1:
 *
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 *
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 *
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
 */
class LengthOfLongestSubStr {
    /**
     * 滑动窗口思想
     * 找出从每一个字符开始的，不包含重复字符的最长子串，那么其中最长的那个字符串就是所求。
     */

    /**
     * 时间复杂度：O（N）
     * 空间复杂度：O（m）m为字符集的大小
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s){
        if (s == null) return 0;
        int maxSubLength = 0;
        //哈希集合，记录每个字符是否出现过
        Set<Character> set = new HashSet<>();
        //右指针，初始值为-1（如果只有一个字符时方便计算），即不重复字串的结束为止
        int rk = -1;
        for (int i = 0; i < s.length();i++){
            if (i !=0 ){
                //左指针向右移动一格，移除一个字符（最左侧的字符）
                set.remove(s.charAt(i-1));
            }
            while (rk+1 < s.length() && !set.contains(s.charAt(rk+1))){
                //不断的移动右指针
                set.add(s.charAt(rk + 1));
                ++rk;
            }
            //第i到rk个字符串是一个无重复的子串
            maxSubLength = Math.max(maxSubLength,rk - i +1);

            //如果此时，右指针已经到底做后一个字符，则左指针再移动时，子串只会变小
            if (rk == s.length() - 1){
                return maxSubLength;
            }
        }
        return maxSubLength;
    }

    /**
     * 改善：
     * 上边的方法中，有缺点，左指针并不需要一次递增，会多很多无谓的循环。
     * 在发现重复字符时，直接把左指针移动到重复字符的下一个位置即可。
     * 时间复杂度：O（N）
     * 空间复杂度：O（m）m为字符集的大小
     * @param s
     * @return
     */
    public static int lengthOfLongestSubString2(String s){
        int length = s.length();
        int max = 0;
        //哈希表，存储出现的字符和其位置
        Map<Character,Integer> map = new HashMap<>();
        for (int start = 0,end = 0; end < length ; end++){
            char element = s.charAt(end);
            if (map.containsKey(element)){
                start = Math.max(map.get(element)+1,start);
            }
            max = Math.max(max,end - start + 1);
            map.put(element,end);
        }
        return max;
    }
}
