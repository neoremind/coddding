package net.neoremind.mycode.argorithm.leetcode;

/**
 * Given n nodes labeled from 0 to n - 1 and a list of undirected edges (each edge is a pair of nodes), write a
 * function to check whether these edges make up a valid tree.
 * <p>
 * For example:
 * <p>
 * Given n = 5 and edges = [[0, 1], [0, 2], [0, 3], [1, 4]], return true.
 * <p>
 * Given n = 5 and edges = [[0, 1], [1, 2], [2, 3], [1, 3], [1, 4]], return false.
 * <p>
 * Hint:
 * <p>
 * Given n = 5 and edges = [[0, 1], [1, 2], [3, 4]], what should your return? Is this case a valid tree? Show More
 * Hint Note: you can assume that no duplicate edges will appear in edges. Since all edges are undirected, [0, 1] is
 * the same as [1, 0] and thus will not appear together in edges.
 * <p>
 * 判断输入的边是否能构成一个树，我们需要确定两件事：
 * <p>
 * 这些边是否构成环路，如果有环则不能构成树
 * 这些边是否能将所有节点连通，如果有不能连通的节点则不能构成树
 *
 * @author xu.zhang
 */
public class GraphValidTree {
    public static void main(String[] args) {
        int[][] nodes = new int[][]{
                {0, 1}, {0, 2}, {0, 3}, {1, 4}
        };
        System.out.println(validTree(5, nodes)); // true

        nodes = new int[][]{
                {0, 1}, {0, 2}, {0, 3}, {1, 4}, {2, 4}
        };
        System.out.println(validTree(5, nodes)); // false

        nodes = new int[][]{
                {0, 1}, {0, 2}, {0, 3}, {1, 4}, {0, 4}
        };
        System.out.println(validTree(5, nodes)); // false
    }

    static boolean validTree(int n, int[][] nodes) {
        UF uf = new UF(n);
        for (int i = 0; i < nodes.length; i++) {
            int x = uf.find(nodes[i][0]);
            int y = uf.find(nodes[i][1]);
            if (x == y) {
                return false;
            }
            uf.union(nodes[i][0], nodes[i][1]);
        }
        return uf.cnt == 0;
    }

    static class UF {
        int[] w;
        int[] a;
        int cnt = 0;

        public UF(int n) {
            w = new int[n];
            a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = i;
                w[i] = 1;
            }
            cnt = n - 1;
        }

        int find(int p) {
            while (p != a[p]) {
                p = a[p];
            }
            return p;
        }

        void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) {
                return;
            } else {
                if (w[rootP] < w[rootQ]) {
                    a[rootP] = rootQ;
                    w[rootQ] += w[rootP];
                } else {
                    a[rootQ] = rootP;
                    w[rootP] += w[rootQ];
                }
            }
            cnt--;
        }
    }

}
