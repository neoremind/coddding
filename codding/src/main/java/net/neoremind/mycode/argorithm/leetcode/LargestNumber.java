package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 179. Largest Number My Submissions QuestionEditorial Solution
 * Total Accepted: 45603 Total Submissions: 237838 Difficulty: Medium
 * Given a list of non negative integers, arrange them such that they form the largest number.
 * <p>
 * For example, given [3, 30, 34, 5, 9], the largest formed number is 9534330.
 * <p>
 * Note: The result may be very large, so you need to return a string instead of an integer.
 * <p>
 * Credits:
 * Special thanks to @ts for adding this problem and creating all test cases.
 * <p>
 * Subscribe to see which companies asked this question
 * <p>
 * Hide Tags Sort
 * <p>
 * 一开始的想法就是排序，大思路首先正确，就是需要实现自己的排序策略。可以使用各种排序算法，比如选择、快排等。
 * <p>
 * 但是排序的策略，应该利用int转为str来比较的思路。
 * <p>
 * 一直陷入，两个int，应该如何从高位比较到低位，当时的想法是比较高位，直到发现不同的位，则比较不同的位。
 * 例如，384 > 3125
 * 但是这里有一个坑，就是384 > 3842，因为
 * 1）有公共最长前缀384，
 * 2）公共最长前缀已经到了末尾
 * 3）另外一个数字末尾这个2比首位3小
 * 这三个条件极其难写算法去实现，会有很多边界条件和额外的计算量。
 * 因此不要陷入数字计算的陷阱里，而是想着用字符串比较或者使用jdk的函数式编程思想（例如{@link #largestNumber2(int[])}）
 *
 * @author zhangxu
 */
public class LargestNumber {

    @Test
    public void testLargestNumber() {
        String[] tests = {"34", "3"};
        // 343 > 334 因此compareTo返回1，那么就说明s1比s2大，也就是34>3，因此排序后数组是[3, 34]，但是实际我们要的是34，3，这样组成的数字更大，所以先比较s2+s1
        Arrays.sort(tests, (String s1, String s2) -> (s1 + s2).compareTo(s2 + s1));
        System.out.println(Arrays.toString(tests));

        int[] nums = new int[] {34, 30};
        String str = largestNumber(nums);
        System.out.println(str);
        assertThat(str, Matchers.is("3430"));

        nums = new int[] {0, 0};
        str = largestNumber(nums);
        System.out.println(str);
        assertThat(str, Matchers.is("0"));

        nums = new int[] {3, 30, 34, 5, 9};
        str = largestNumber2(nums);
        System.out.println(str);
        assertThat(str, Matchers.is("9534330"));
    }

    public String largestNumber2(int[] num) {
        String[] array = Arrays.stream(num).mapToObj(String::valueOf).toArray(String[]::new);
        Arrays.sort(array, (String s1, String s2) -> (s2 + s1).compareTo(s1 + s2));
        return Arrays.stream(array).reduce((x, y) -> x.equals("0") ? y : x + y).get();
        // Arrays.stream(array).reduce("", (x, y) -> x.equals("0") ? y : x + y);
    }

    public String largestNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        String[] numsStr = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            numsStr[i] = nums[i] + "";
        }
        Arrays.sort(numsStr, (o1, o2) -> (o2 + o1).compareTo(o1 + o2));
        StringBuilder sb = new StringBuilder();
        for (String str : numsStr) {
            sb.append(str);
        }
        if (sb.charAt(0) == '0') {
            return "0";
        }
        return sb.toString();
    }

}
