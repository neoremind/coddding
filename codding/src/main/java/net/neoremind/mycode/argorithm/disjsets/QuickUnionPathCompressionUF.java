package net.neoremind.mycode.argorithm.disjsets;

/**
 * @author zhangxu
 */
public class QuickUnionPathCompressionUF {

    private int[] id;    // id[i] = parent of i
    private int count;   // number of components

    /**
     * Initializes an empty union–find data structure with n isolated components 0 through n-1.
     *
     * @param n the number of sites
     *
     * @throws java.lang.IllegalArgumentException if n < 0
     */
    public QuickUnionPathCompressionUF(int n) {
        count = n;
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int count() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one object
     *
     * @return the component identifier for the component containing site {@code p}
     *
     * @throws IndexOutOfBoundsException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        int root = p;
        while (root != id[root]) {
            root = id[root];
        }
        while (p != root) {
            int newp = id[p];
            id[p] = root;
            p = newp;
        }
        return root;
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     *
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     *
     * @throws IndexOutOfBoundsException unless
     *                                   both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     *
     * @throws IndexOutOfBoundsException unless
     *                                   both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) {
            return;
        }
        id[rootP] = rootQ;
        count--;
    }

}
