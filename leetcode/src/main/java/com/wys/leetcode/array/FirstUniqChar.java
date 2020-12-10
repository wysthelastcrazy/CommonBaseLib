package com.wys.leetcode.array;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2020/11/30
 * @Describe: 剑指offer50. 第一个只出现一次的字符位置
 *
 * 在字符串 s 中找出第一个只出现一次的字符。如果没有，返回一个单空格。 s 只包含小写字母。
 *
 * 示例:
 * s = "abaccdeff"
 * 返回 "b"
 *
 * s = ""
 * 返回 " "
 *
 * 链接：https://leetcode-cn.com/problems/di-yi-ge-zhi-chu-xian-yi-ci-de-zi-fu-lcof
 */
class FirstUniqChar {

    /**
     * 方法一：哈希表
     * 时间复杂度 O(N)O(N) ： NN 为字符串 s 的长度；需遍历 s 两轮，使用 O(N)O(N)；
     * HashMap 查找操作的复杂度为 O(1)O(1) ；
     * 空间复杂度 O(1)O(1) ： 由于题目指出 s 只包含小写字母，因此最多有 26 个不同字符，
     * HashMap 存储需占用 O(26) = O(1)O(26)=O(1) 的额外空间。
     *
     * @param s
     * @return
     */
    public static char firstUniqChar(String s){
        Map<Character,Integer> dic = new HashMap<>();
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            dic.put(c, dic.get(c)!= null?dic.get(c)+1:1);
        }
        for (int i = 0; i < s.length(); i++){
            if (dic.get(s.charAt(i)) == 1)
                return s.charAt(i);
        }
        return ' ';
    }

    /**
     * 有序哈希表
     * 减少第二次循环次数
     * @param s
     * @return
     */
    public static char firstUniqChar2(String s){
        Map<Character, Boolean> dic = new LinkedHashMap<>();
        char[] sc = s.toCharArray();
        for(char c : sc)
            dic.put(c, !dic.containsKey(c));
        for(Map.Entry<Character, Boolean> d : dic.entrySet()){
            if(d.getValue()) return d.getKey();
        }
        return ' ';
    }
}
