package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/redundant-connection/
 *
 * @author xu.zx
 */
public class RedundantConnection {

    public int[] findRedundantConnection(int[][] edges) {
        UF uf = new UF(edges.length);
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            int[] edge = edges[i];
            int p = edge[0];
            int q = edge[1];
            // System.out.println(p + " " + q);
            if (!uf.union(p, q)) {
                //System.out.println("add " + p + " " + q);
                res.add(edge);
            }
        }
        return res.isEmpty() ? null : res.get(res.size() - 1);
    }

    class UF {
        int[] id;
        int count;

        UF(int n) {
            id = new int[n + 1];
            for (int i = 0; i <= n; i++)
                id[i] = i;
        }

        int find(int p) {
            while (p != id[p]) {
                p = id[p];
            }
            return p;
        }

        boolean union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            //System.out.println("try union " + rootP + " " + rootQ);
            if (rootP == rootQ) return false;
            id[rootQ] = rootP;
            count--;
            //System.out.println(Arrays.toString(id));
            return true;
        }
    }

}
