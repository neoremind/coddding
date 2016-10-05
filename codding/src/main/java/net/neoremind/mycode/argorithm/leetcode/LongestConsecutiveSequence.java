package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an unsorted array of integers, find the length of the longest consecutive elements sequence.
 * <p>
 * For example,
 * Given [100, 4, 200, 1, 3, 2],
 * The longest consecutive elements sequence is [1, 2, 3, 4]. Return its length: 4.
 * <p>
 * Your algorithm should run in O(n) complexity.
 * <p>
 * https://leetcode.com/problems/longest-consecutive-sequence/
 *
 * @author zhangxu
 */
public class LongestConsecutiveSequence {

    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }
        Arrays.sort(nums);  // use Arrays, not Collections
        int maxLen = 1;
        int len = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {  // handle duplications like 0,1,1,2
                continue;
            } else if (nums[i] == nums[i - 1] + 1) {
                len++;
            } else {
                maxLen = Math.max(maxLen, len);
                len = 1;  //put this line after Math.max(..)
            }
        }
        return Math.max(maxLen, len);
    }

    public int longestConsecutive2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        UF uf = new UF(nums.length);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (map.containsKey(num)) {
                continue;
            }
            if (map.containsKey(num + 1)) {
                uf.union(i, map.get(num + 1));
            }
            if (map.containsKey(num - 1)) {
                uf.union(i, map.get(num - 1));
            }
            map.put(num, i);
        }
        System.out.println("count=" + uf.count);
        System.out.println("maxWeight=" + uf.maxWeight());
        System.out.println("s[]=" + Arrays.toString(uf.s));
        System.out.println("weight[]=" + Arrays.toString(uf.weight));
        return uf.maxWeight();
    }

    class UF {
        int[] s;
        int[] weight;
        int count;

        UF(int size) {
            s = new int[size];
            weight = new int[size];
            count = size;
            for (int i = 0; i < size; i++) {
                s[i] = i;
                weight[i] = 1;
            }
        }

        int find(int p) {
            while (p != s[p]) {
                p = s[p];
            }
            return p;
        }

        void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) {
                return;
            }
            if (weight[rootP] < weight[rootQ]) {
                s[rootP] = rootQ;
                weight[rootQ] += weight[rootP];
            } else {
                s[rootQ] = rootP;
                weight[rootP] += weight[rootQ];
            }
            count--;
        }

        int maxWeight() {
            int max = 1;
            for (int w : weight) {
                max = Math.max(w, max);
            }
            return max;
        }
    }

    @Test
    public void test() {
        int[] nums = new int[] {};
        assertThat(longestConsecutive(nums), Matchers.is(0));
        assertThat(longestConsecutive2(nums), Matchers.is(0));

        nums = new int[] {1};
        assertThat(longestConsecutive(nums), Matchers.is(1));
        assertThat(longestConsecutive2(nums), Matchers.is(1));

        nums = new int[] {1, 2, 3};
        assertThat(longestConsecutive(nums), Matchers.is(3));
        assertThat(longestConsecutive2(nums), Matchers.is(3));

        nums = new int[] {-1, 1, 0};
        assertThat(longestConsecutive(nums), Matchers.is(3));
        assertThat(longestConsecutive2(nums), Matchers.is(3));

        nums = new int[] {7, 8, 1, 2, 3, 6, 9};
        assertThat(longestConsecutive(nums), Matchers.is(4));
        assertThat(longestConsecutive2(nums), Matchers.is(4));

        nums = new int[] {1, 6, 8, 2, 8, 0, 9, 3, 7};
        assertThat(longestConsecutive(nums), Matchers.is(4));
        assertThat(longestConsecutive2(nums), Matchers.is(4));
    }

}
