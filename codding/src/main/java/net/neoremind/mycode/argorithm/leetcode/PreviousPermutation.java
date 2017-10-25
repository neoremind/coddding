package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertThat;

/**
 * 和{@link NextPermutation}相反找前一个序列
 *
 * @author xu.zhang
 */
public class PreviousPermutation {

    public void previousPermutation(int[] num) {
        //1.找到最后一个降序位置pos
        int pos = -1;
        for (int i = num.length - 1; i > 0; i--) {
            if (num[i] < num[i - 1]) {
                pos = i - 1;
                break;
            }
        }

        //2.如果不存在序，即这个数是最小的，那么反排这个数组
        if (pos < 0) {
            reverse(num, 0, num.length - 1);
            return;
        }

        //3.存在降序，那么找到pos之后的序列反排
        reverse(num, pos + 1, num.length - 1);

        //4. 后面的序列找比pos小的交换。
        for (int i = num.length - 1; i > pos; i--) {
            if (num[i] < num[pos]) {
                int tmp = num[i];
                num[i] = num[pos];
                num[pos] = tmp;
                break;
            }
        }
    }

    public void reverse(int[] num, int begin, int end) {
        int l = begin, r = end;
        while (l < r) {
            int tmp = num[l];
            num[l] = num[r];
            num[r] = tmp;
            l++;
            r--;
        }
    }

    @Test
    public void test() {
        int[] p = new int[]{1, 2, 3, 4};
        previousPermutation(p);
        assertThat(p, Matchers.is(new int[]{4, 3, 2, 1}));

        p = new int[]{1, 2, 4, 3};
        previousPermutation(p);
        assertThat(p, Matchers.is(new int[]{1, 2, 3, 4}));

        p = new int[]{1, 3, 2, 4};
        previousPermutation(p);
        assertThat(p, Matchers.is(new int[]{1, 2, 4, 3}));

        System.out.println("======");
        int[] x = new int[]{1, 2, 3, 4};
        System.out.println(Arrays.toString(x));
        for (int i = 0; i < 24; i++) {
            previousPermutation(x);
            System.out.println(Arrays.toString(x));
        }
    }

}
