package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

/**
 * ClassName: RemoveElement <br/>
 * Function: Given an array and a value, remove all instances of that value in place and return the new length.
 * <p/>
 * The order of elements can be changed. It doesn't matter what you leave beyond the new length.
 * <p/>
 * Tips: use array and two pointers
 * <p>
 * 发现元素后，思路为直接从最后的tail补一个元素上来，一个指向tail的指针索引减1。
 * <p>
 * 类似的题目有move zeros，remove duplicates from sorted array。
 *
 * @author Zhang Xu
 */
public class RemoveElement {

    // 双指针
    public static int removeElement(int[] nums, int val) {
        int currIndex = 0;
        int tailIndex = nums.length - 1;
        while (currIndex <= tailIndex) {
            if (nums[currIndex] == val) {
                nums[currIndex] = nums[tailIndex--];
                continue;
            }
            currIndex++;
        }

        // 下面不是必须的逻辑
        int[] ret = new int[tailIndex + 1];
        System.arraycopy(nums, 0, ret, 0, tailIndex + 1);
        System.out.println(Arrays.toString(ret));
        return ret.length;
    }

    // 稳定的
    public static int removeElement2(int[] nums, int val) {
        int idx = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[idx++] = nums[i];
            }
        }
        return idx;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2, 5, 1, 3, 4};
        int len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(4));

        nums = new int[]{2, 5, 1, 3, 4};
        len = removeElement2(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(4));

        nums = new int[]{3, 5, 1, 3, 4};
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(3));

        nums = new int[]{3, 3, 3, 2, 3};
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(1));

        nums = new int[]{3, 3, 3, 3, 3};
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(0));

        nums = new int[]{2};
        len = removeElement(nums, 2);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(0));

        nums = new int[]{2, 5, 7};
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(3));

    }

}
