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
 * 
 * @author Zhang Xu
 */
public class RemoveElement {

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

    public static void main(String[] args) {
        int[] nums = new int[] { 2, 5, 1, 3, 4 };
        int len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(4));

        nums = new int[] { 3, 5, 1, 3, 4 };
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(3));

        nums = new int[] { 3, 3, 3, 2, 3 };
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(1));

        nums = new int[] { 3, 3, 3, 3, 3 };
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(0));

        nums = new int[] { 2 };
        len = removeElement(nums, 2);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(0));

        nums = new int[] { 2, 5, 7 };
        len = removeElement(nums, 3);
        System.out.println("before:" + nums.length + ", after:" + len);
        assertThat(len, is(3));

    }

}
