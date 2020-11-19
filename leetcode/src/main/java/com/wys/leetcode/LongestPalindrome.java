package com.wys.leetcode;

/**
 * @author wangyasheng
 * @date 2020/11/19
 * @Describe: 5.最长回文子串
 *
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 *
 * 示例 1：
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 *
 * 示例 2：
 * 输入: "cbbd"
 * 输出: "bb"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 */
class LongestPalindrome {
    //https://www.cxyxiaowu.com/2869.html
    /**
     * 方法一 ：动态规划
     * @param s
     * @return
     */
    public static String longestPalindrome(String s){
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        String ans = "";
        for (int l = 0; l < n; l++){
            for (int i = 0; i+l < n;i++){
                int j = i + l;
                if (l == 0){
                    dp[i][j] = true;
                }else if (l == 1){
                    dp[i][j] = (s.charAt(i) == s.charAt(j));
                }else{
                    dp[i][j] = (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]);
                }

                if (dp[i][j]&& l+1 > ans.length()){
                    ans = s.substring(i,i+l+1);
                }
            }
        }
        return ans;
    }
}
