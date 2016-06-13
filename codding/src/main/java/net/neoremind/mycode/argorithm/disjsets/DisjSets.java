package net.neoremind.mycode.argorithm.disjsets;

/**
 * 不相交集
 *
 * @author zhangxu
 */
public class DisjSets {

    private int[] s;

    public DisjSets(int numElements) {
        s = new int[numElements];
        for (int i = 0; i < numElements; i++) {
            s[i] = -1;
        }
    }

    public void union(int root1, int root2) {
        s[root2] = root1;
        // unionByHeight(root1, root2); // 使用按高度求并的技巧
    }

    public void unionByHeight(int root1, int root2) {
        if (s[root2] < s[root1]) {
            s[root1] = root2;
        } else {
            if (s[root1] == s[root2]) {
                s[root1]--;
            }
            s[root2] = root1;
        }
    }

    public int find(int x) {
        if (s[x] < 0) {
            return x;
        } else {
            // return find(s[x]); 不使用路径压缩
            return s[x] = find(s[x]);
        }
    }

}
