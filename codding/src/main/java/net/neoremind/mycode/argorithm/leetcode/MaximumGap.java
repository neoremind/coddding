package net.neoremind.mycode.argorithm.leetcode;

import java.util.*;

/**
 * Given an unsorted array, find the maximum difference between the successive elements in its sorted form.
 *
 * Return 0 if the array contains less than 2 elements.
 *
 * Example 1:
 *
 * Input: [3,6,9,1]
 * Output: 3
 * Explanation: The sorted form of the array is [1,3,6,9], either
 *              (3,6) or (6,9) has the maximum difference 3.
 * Example 2:
 *
 * Input: [10]
 * Output: 0
 * Explanation: The array contains less than 2 elements, therefore return 0.
 * Note:
 *
 * You may assume all elements in the array are non-negative integers and fit in the 32-bit signed integer range.
 * Try to solve it in linear time/space.
 *
 * radix sort
 *
 * @author xu.zx
 */
public class MaximumGap {

    /**
     * my solution: MSB Radix sort
     *
     * @see {@link net.neoremind.mycode.argorithm.sort.RadixSortMSB}
     */
    public int maximumGap(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        // Arrays.sort(nums);
        // int max = Integer.MIN_VALUE;
        // for (int i = 1; i < nums.length; i++) {
        //     if (nums[i] - nums[i - 1] > max) {
        //         max = nums[i] - nums[i - 1];
        //     }
        // }
        // return max;

        int max = nums[0];
        for (int i = 0; i < nums.length; i++) {
            max = Math.max(nums[i], max);
        }

        // 记住求数的最大整数的10倍！！
        int maxNumWithZeros = 1;
        while (max / 10 != 0) {
            max /= 10;
            maxNumWithZeros *= 10;
        }

        radixSort(nums, 0, nums.length - 1, maxNumWithZeros);

        // System.out.println(Arrays.toString(nums));

        int result = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] - nums[i + 1] > result) {
                result = nums[i] - nums[i + 1];
            }
        }
        return result;
    }

    void radixSort(int[] n, int i, int j, int div) {
        if (div == 0) {
            return;
        }
        //System.out.println(div);
        int idx = i;
        // bucket -> elements
        Map<Integer, List<Integer>> bucket = new HashMap<>();
        while (idx <= j) {
            int val = (n[idx] / div) % 10;
            if (bucket.get(val) == null) {
                bucket.put(val, new ArrayList<>());
            }
            bucket.get(val).add(n[idx]);
            idx++;
        }
        int seq = i;
        for (int k = 9; k >=0 ; k--) {
            List<Integer> elems = bucket.get(k);
            if (elems == null) {
                continue;
            }
            int count = elems.size();
            if (count > 1) {
                for (int t = 0; t < count; t++) {
                    n[seq + t] = elems.get(t);
                }
                radixSort(n, seq, seq + count - 1, div / 10);
                seq += count;
            } else if (count == 1) {
                n[seq++] = elems.get(0);
            }
        }
    }

    /**
     * LSB
     */
    public int maximumGap2(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        // 找最大值
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            max = Math.max(max, nums[i]);
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
            for (int i = 0; i < nums.length; i++) {
                int digit = (nums[i] / sub) % 10;
                bucket.get(digit).add(nums[i]);
            }
            int idx = 0;
            for (List<Integer> b : bucket) {
                // System.out.println(idx + " " + b);
                if (!b.isEmpty()) {
                    for (Integer elem : b) {
                        nums[idx++] = elem;
                    }
                }
                b.clear();
            }
            exp++;
            sub *= 10;
        }

        int result = Integer.MIN_VALUE;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] - nums[i - 1] > result) {
                result = nums[i] - nums[i - 1];
            }
        }
        return result;


    }

}
