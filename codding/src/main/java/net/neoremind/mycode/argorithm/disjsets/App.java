package net.neoremind.mycode.argorithm.disjsets;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class App {

    @Test
    public void test() {
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(10);
        uf.union(1, 2);
        uf.dump();
        uf.union(2, 3);
        uf.dump();
        uf.union(5, 6);
        uf.dump();
        uf.union(7, 8);
        uf.dump();
        uf.union(9, 0);
        uf.dump();
        uf.union(8, 9);
        uf.dump();
        uf.union(5, 7);
        uf.dump();
        uf.union(2, 8);
        uf.dump();

        QuickUnionUF uf2 = new QuickUnionUF(10);
        uf2.union(1, 2);
        uf2.dump();
        uf2.union(2, 3);
        uf2.dump();
        uf2.union(5, 6);
        uf2.dump();
        uf2.union(7, 8);
        uf2.dump();
        uf2.union(9, 0);
        uf2.dump();
        uf2.union(8, 9);
        uf2.dump();
        uf2.union(5, 7);
        uf2.dump();
        uf2.union(2, 8);
        uf2.dump();
    }

}
