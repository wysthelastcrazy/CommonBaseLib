package com.wys.leetcode;

import java.util.ArrayList;

/**
 * @author wangyasheng
 * @date 2020/11/23
 * @Describe:顺时针打印矩阵
 *
 * 输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字。
 *
 * 示例 1：
 *
 * 输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出：[1,2,3,6,9,8,7,4,5]
 * 示例 2：
 *
 * 输入：matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * 输出：[1,2,3,4,8,12,11,10,9,5,6,7]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shun-shi-zhen-da-yin-ju-zhen-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class SpiralOrder {

    /**
     * 解题思路
     * 一层一层从外到里打印，观察可知每一层打印都有相同的处理步骤，
     * 唯一不同的是上下左右的边界不同了。
     * 因此使用四个变量 r1, r2, c1, c2 分别存储上下左右边界值，从而定义当前最外层。
     * 打印当前最外层的顺序：从左到右打印最上一行->从上到下打印最右一行->从右到左打印最下一行
     * ->从下到上打印最左一行。
     * 应当注意只有在 r1 != r2 时才打印最下一行，
     * 也就是在当前最外层的行数大于 1 时才打印最下一行，
     * 这是因为当前最外层只有一行时，继续打印最下一行，会导致重复打印。打印最左一行也要做同样处理。
     *
     * 时间复杂度：O(mn)，其中 mm 和 nn 分别是输入矩阵的行数和列数。矩阵中的每个元素都要被访问一次。
     *
     * 空间复杂度：O(1)。除了输出数组以外，空间复杂度是常数
     *
     * @param matrix
     * @return
     */
    public static int[] spiralOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[0];
        }
        int[] order = new int[matrix.length*matrix[0].length];
        int r1 = 0, r2 = matrix.length - 1, c1 = 0, c2 = matrix[0].length - 1, index = 0;
        while (r1 <= r2 && c1 <= c2) {
            // 上
            for (int i = c1; i <= c2; i++) {
                order[index++] = matrix[r1][i];
            }
            // 右
            for (int i = r1 + 1; i <= r2; i++) {
                order[index++] = matrix[i][c2];
            }
            if (r1 != r2)
                // 下
                for (int i = c2 - 1; i >= c1; i--) {
                    order[index++] = matrix[r2][i];
                }
            if (c1 != c2)
                // 左
                for (int i = r2 - 1; i > r1; i--) {
                    order[index++] = matrix[i][c1];
                }
            r1++; r2--; c1++; c2--;
        }
        return order;
    }
}
