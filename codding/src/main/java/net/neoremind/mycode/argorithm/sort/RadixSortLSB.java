package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 基数排序是通过“分配”和“收集”过程来实现排序。
 *
 * 1)分配，先从个位开始，根据位值(0-9)分别放到0~9号桶中（比如53,个位为3，则放入3号桶中）
 *
 * 2)收集，再将放置在0~9号桶中的数据按顺序放到数组中
 *
 * 重复(1)(2)过程，从个位到最高位（比如32位无符号整形最大数4294967296，最高位10位）
 *
 * https://blog.csdn.net/xgf415/article/details/76595887
 *
 *  https://blog.csdn.net/u011948899/article/details/78027838
 *
 * // TODO 字节实现的基于LSB思想的基数排序
 */
public class RadixSortLSB extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        // 找最大值
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }

        // 找最高位，比如10就是2位
        int digitNum = 1;
        while (max / 10 > 0) {
            max /= 10;
            digitNum++;
        }

        // 初始化桶
        List<List<Integer>> bucket = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bucket.add(new ArrayList<>());
        }

        int exp = 1; // digit num从最低位开始到最高位digitNum
        int sub = 1; // 本轮迭代的值需要除以的数，用于取某个位的值
        while (exp <= digitNum) {
            while (sub < exp) {
                sub *= 10;
            }
            for (int i = 0; i < array.length; i++) {
                int digit = (array[i] / sub) % 10;
                bucket.get(digit).add(array[i]);
            }
            int idx = 0;
            for (List<Integer> b : bucket) {
                if (!b.isEmpty()) {
                    for (Integer elem : b) {
                        array[idx++] = elem;
                    }
                }
                b.clear();
            }
            exp++;
        }
    }

}
