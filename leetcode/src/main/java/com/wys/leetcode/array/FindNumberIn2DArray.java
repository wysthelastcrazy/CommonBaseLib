package com.wys.leetcode.array;

/**
 * @author wangyasheng
 * @date 2020/11/23
 * @Describe: 二维数组中的查找
 *
 * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 *
 * 示例:
 * 现有矩阵 matrix 如下：
 *
 * [
 *   [1,   4,  7, 11, 15],
 *   [2,   5,  8, 12, 19],
 *   [3,   6,  9, 16, 22],
 *   [10, 13, 14, 17, 24],
 *   [18, 21, 23, 26, 30]
 * ]
 * 给定 target = 5，返回 true。
 * 给定 target = 20，返回 false。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof
 */
class FindNumberIn2DArray {

    /**
     * 该二维数组中的一个数，小于它的数一定在其左边，大于它的数一定在其下边。
     * 因此，从右上角开始查找，就可以根据 target 和当前元素的大小关系来快速地缩小查找区间，
     * 每次减少一行或者一列的元素。当前元素的查找区间为左下角的所有元素。
     *
     * 时间复杂度：O（m+n）
     * 空间复杂度：O（1）
     * @param matrix
     * @param target
     * @return
     */
    public boolean findNumberIn2DArray(int[][]matrix,int target){
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        //获取行数和列数
        int rows = matrix.length, columns = matrix[0].length;
        //设置起始位置为而为数组的右上角位置
        int curRow = 0,curCol = columns - 1;
        while (curRow < rows && curCol >= 0){
            if (matrix[curRow][curCol] == target){
                return true;
            }else if (matrix[curRow][curCol] > target){
                //如果当前值大于目标值，则目标值在当前值的左侧，左移，列数减1
                curCol--;
            }else if (matrix[curRow][curCol] < target){
                //如果当前值小于目标值，则目标值在当前值的下侧，下移，行数加1
                curRow++;
            }
        }
        return false;
    }
}
