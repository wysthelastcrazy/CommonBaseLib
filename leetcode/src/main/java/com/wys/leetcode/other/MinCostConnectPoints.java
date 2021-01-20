package com.wys.leetcode.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author wangyasheng
 * @date 2021/1/19
 * @Describe: leetCode1584:链接所有点的最小费用（最小生成树）
 *
 * 给你一个points 数组，表示 2D 平面上的一些点，其中 points[i] = [xi, yi] 。
 *
 * 连接点 [xi, yi] 和点 [xj, yj] 的费用为它们之间的 曼哈顿距离 ：|xi - xj| + |yi - yj| ，其中 |val| 表示 val 的绝对值。
 *
 * 请你返回将所有点连接的最小总费用。只有任意两点之间 有且仅有 一条简单路径时，才认为所有点都已连接。
 *
 * 示例 1：
 * 输入：points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
 * 输出：20
 * 解释：
 * 我们可以按照上图所示连接所有点得到最小总费用，总费用为 20 。
 * 注意到任意两个点之间只有唯一一条路径互相到达。
 * 示例 2：
 *
 * 输入：points = [[3,12],[-2,5],[-4,1]]
 * 输出：18
 * 示例 3：
 *
 * 输入：points = [[0,0],[1,1],[1,0],[-1,1]]
 * 输出：4
 * 示例 4：
 *
 * 输入：points = [[-1000000,-1000000],[1000000,1000000]]
 * 输出：4000000
 * 示例 5：
 *
 * 输入：points = [[0,0]]
 * 输出：0
 *  
 *
 * 提示：
 *
 * 1 <= points.length <= 1000
 * -106 <= xi, yi <= 106
 * 所有点 (xi, yi) 两两不同。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/min-cost-to-connect-all-points
 */
public class MinCostConnectPoints {

    /**
     * 方法一：Kruskal算法（克鲁斯卡算法、加边法）适合稀疏图
     * 时间复杂度：用并查集优化后为O(mlogm+mα(n))，α(n)是一次并查集的复杂度。
     *
     * 算法思想：Kruskal算法是一种贪心算法，它是将边按权值排序，每次从剩下的边
     * 集中选择权值最小且两个端点不在同一个集合的边加入生成树中，反复操作，直到加
     * 入了n-1条边。
     *
     * 算法步骤：
     * 1、将G中的边按权值从小到大排序；
     * 2、按照权值从小到大依次选边，若当前选取的边两端点都在生成树中，则舍弃，
     *    否则将边两个端点所在的集合(树)合并，并计数；
     * 3、重复2的操作，直到生成树中包含n-1条边。如果遍历完所有边之后，
     *    选取不到n-1条边，表示最小生成树不存在。
     *
     * @param points
     * @return
     */
    public int minCostConnectPoints(int[][] points){
        int n = points.length;
        //并查集
        DisjointSetUnion dsu = new DisjointSetUnion(n);
        List<Edge> edges = new ArrayList<>();
        //计算所有的边信息（即所有两点之间的距离），并存储在edges中
        for (int i = 0; i < n ; i++){
            for (int j = i+1;j < n; j++){
                edges.add(new Edge(dist(points,i,j),i,j));
            }
        }
        //将所有边按从小到大排序
        Collections.sort(edges, new Comparator<Edge>() {
            @Override
            public int compare(Edge edge1, Edge edge2) {
                return edge1.len - edge2.len;
            }
        });
        int ret = 0,num = 1;
        for (Edge edge : edges){
            int len = edge.len, x = edge.x, y = edge.y;
            if (dsu.unionSet(x,y)){
                ret += len;
                num ++;
                //合并
                dsu.merge(x,y);
                if (num == n){
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 计算曼哈顿距离
     * @param points
     * @param x
     * @param y
     * @return
     */
    public int dist(int[][] points, int x,int y){
        return Math.abs(points[x][0] - points[y][0])
                +Math.abs(points[x][1] - points[y][1]);
    }
    class DisjointSetUnion{
        int[] f;     //存储父节点下标值
        int[] rank;  //存储以对应下标节点为根节点的子树的深度，用于合并
        int n;
        public DisjointSetUnion(int n){
            this.n = n;
            this.rank = new int[n];
            Arrays.fill(this.rank,1);
            this.f = new int[n];
            for (int i = 0;i < n; i++){
                this.f[i] = i;
            }
        }
        public int find(int x){
            if (x == f[x]){
                return x;
            }else{
                //父节点设为根节点
                //此操作可以实现路径压缩优化，可以降低并查集的时间复杂度
                f[x] = find(f[x]);
                //返回父节点
                return f[x];
            }
            //以上代码可以简写为一行
//            return f[x] == x ? x : (f[x] = find(f[x]));
        }

        /**
         * 判断是否在不同树中
         * @param x
         * @param y
         * @return
         */
        public boolean unionSet(int x,int y){
            int fx = find(x),fy = find(y);
            if (fx == fy){
                return false;
            }
            return true;
        }

        /**
         * 合并x、y两个节点所在的树
         * @param x
         * @param y
         */
        public void merge(int x,int y){
            //先找到两个根节点
            int fx = find(x), fy = find(y);
            if (rank[fx] <= rank[fy]){
                //如果x对应的树深度小于y对应的树深度，
                // 则把x所在树合并到y对所在树的根节点上
                f[fx] = fy;
            }else{
                //如果x对应的树深度大于y对应的树深度，
                // 则把y所在树合并到x对所在树的根节点上
                f[fy] = fx;
            }
            if (rank[fx] == rank[fy]&& fx!=fy){
                //如果深度相同且根节点不同，
                //则新的根节点深度+1（+1是因为深度相同是，合并之后总深度只增加了一层，
                // 如果被合并的树深度小，则合并后的树深度不变）
                //此处rank[fy]++是因为深度相同时，fx合并到fy上
                rank[fy]++;
            }
        }
    }

    /**
     * 边信息（x，y为边的两个端点下表，len为边的长度）
     * len为x、y两点之间的距离
     */
    class Edge{
        int len,x,y;
        public Edge(int len,int x,int y){
            this.len = len;
            this.x = x;
            this.y = y;
        }
    }
}
