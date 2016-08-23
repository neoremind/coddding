package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.
 * <p>
 * If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending
 * order).
 * <p>
 * The replacement must be in-place, do not allocate extra memory.
 * <p>
 * Here are some examples. Inputs are in the left-hand column and its corresponding outputs are in the right-hand
 * column.
 * 1,2,3 → 1,3,2
 * 3,2,1 → 1,2,3
 * 1,1,5 → 1,5,1
 * <p>
 * 注意和{@link Permutation}生成的顺序不一样
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/next-permutation/
 */
public class NextPermutation {

    public void nextPermutation(int[] num) {
        //1.找到最后一个升序位置pos
        int pos = -1;
        for (int i = num.length - 1; i > 0; i--) {
            if (num[i] > num[i - 1]) {
                pos = i - 1;
                break;
            }
        }

        //2.如果不存在升序，即这个数是最大的，那么反排这个数组
        if (pos < 0) {
            reverse(num, 0, num.length - 1);
            return;
        }

        //3.存在升序，那么找到pos之后最后一个比它大的位置
        for (int i = num.length - 1; i > pos; i--) {
            if (num[i] > num[pos]) {
                int tmp = num[i];
                num[i] = num[pos];
                num[pos] = tmp;
                break;
            }
        }

        //4.反排pos之后的数
        reverse(num, pos + 1, num.length - 1);
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
        int[] nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        new Permutation().permute(nums); //和Permutation的顺序不一样！！！
        int[] p = new int[] {1, 2, 7, 3, 4, 6, 5};
        nextPermutation(p);
        assertThat(p, Matchers.is(new int[] {1, 2, 7, 3, 5, 4, 6}));

        p = new int[] {1, 2, 7, 4, 6, 3, 5};
        nextPermutation(p);
        assertThat(p, Matchers.is(new int[] {1, 2, 7, 4, 3, 6, 5}));
    }
}
