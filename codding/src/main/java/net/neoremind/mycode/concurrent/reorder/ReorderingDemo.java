package net.neoremind.mycode.concurrent.reorder;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;

public class ReorderingDemo {

    static int x = 0, y = 0, a = 0, b = 0;

    public static void main(String[] args) throws Exception {
        Bag bag = new HashBag();
        for (int i = 0; i < 10000; i++) {
            x = y = a = b = 0;
            Thread one = new Thread() {
                public void run() {
                    a = 1;
                    x = b;
                }
            };
            Thread two = new Thread() {
                public void run() {
                    b = 1;
                    y = a;
                }
            };
            one.start();
            two.start();
            one.join();
            two.join();
            bag.add(x + "_" + y);
        }
        System.out.println(bag.getCount("0_1"));
        System.out.println(bag.getCount("1_0"));
        System.out.println(bag.getCount("1_1"));
        System.out.println(bag.getCount("0_0"));
        // 结果是如下的或者其他情况，证明可能发生指令重排序
        //        9999
        //        1
        //        0
        //        0

        //        9998
        //        2
        //        0
        //        0
    }
}
