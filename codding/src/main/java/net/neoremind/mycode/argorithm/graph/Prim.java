package net.neoremind.mycode.argorithm.graph;

/**
 * 最小生成树
 * <p>
 * 1、一些定义：
 * 树定义：
 * 1）无向、无环、连通图
 * 2）N个节点有N-1条边
 * <p>
 * 生成树定义：（Spanning Tree）
 * 1）节点：连通的全部节点
 * 2）边：原来边的子集
 * <p>
 * 最小生成树定义：（Minimum Spanning Tree）
 * 1）所有生成树中边权值的和最小的树
 * <p>
 * 2、算法描述
 * O(N^2)
 * 贪心算法，难点在于算法的证明。
 * 始终保持一棵树，维护一个集合V={}，一个边集合E={}，初始加入一个节点，然后选择和这个节点邻接的权最小的一个节点加入。
 * 描述如下，循环N-1次，选择一条边，满足
 * 1）使v1在集合V中，v2不在
 * 2）（v1，v2）权最小
 * 3）E = E + (v1, v2)
 * 4）V = V + v2
 * <p>
 * http://www.geeksforgeeks.org/greedy-algorithms-set-5-prims-minimum-spanning-tree-mst-2/
 *
 * @author zhangxu
 */
// TODO
public class Prim {

}
