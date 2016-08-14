package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: TwoSum <br/>
 * Function: Given an array of integers, find two numbers such that they add up to a specific target number.
 * <p>
 * The function twoSum should return indices of the two numbers such that they add up to the target, where index1 must
 * be less than index2. Please note that your returned answers (both index1 and index2) are not zero-based.
 * <p>
 * You may assume that each input would have exactly one solution.
 * <p>
 * <pre>
 * Input: numbers={2, 7, 11, 15}, target=9
 * Output: index1=1, index2=2
 * </pre>
 * <p>
 * Solution: O(n) runtime, O(n) space – Hash table: We could reduce the runtime complexity of looking up a value to O(1)
 * using a hash map that maps a value to its index.
 *
 * @author Zhang Xu
 */
public class TwoSum {

    /**
     * Use hashmap to get the two nums.
     * <p>
     * The solution works like that, starting from the beginner of the array, we can walk through every element, taking
     * each of them as a sentinel, we can figure out the "pal" aka. the number that is equal to target minus the
     * sentinel, then put the two number and their indices into the hashmap. If there is an number which is among the
     * left match any key in the map, then we can tell we figure out.
     *
     * @param nums
     * @param target
     *
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            throw new RuntimeException("nums array is null, which should never happen!");
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(nums.length * 4 / 3);
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[] {map.get(target - nums[i]), i + 1};
            }
            map.put(nums[i], i + 1);
        }
        throw new RuntimeException("There is no answer, which should never happen!");
    }

    public static void main(String[] args) {
        TwoSum t = new TwoSum();
        int[] indices = t.twoSum(new int[] {2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {1, 2}));

        indices = t.twoSum(new int[] {8, 6, 7, 2, 15, 4}, 10);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {1, 4}));

        indices = t.twoSum(new int[] {3, 1, 0, 1}, 2);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {2, 4}));

        indices = t.twoSum(new int[] {-3, 4, 3, 90}, 0);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {1, 3}));

        indices = t.twoSum(new int[] {2, 9, 9, -2}, 7);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {3, 4}));

        indices = t.twoSum(new int[] {3, 2, 4}, 6);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {2, 3}));

        int[] complex = new int[] {230, 863, 916, 585, 981, 404, 316, 785, 88, 12, 70, 435, 384,
                778, 887, 755, 740, 337, 86, 92, 325, 422, 815, 650, 920, 125, 277, 336, 221, 847,
                168, 23, 677, 61, 400, 136, 874, 363, 394, 199, 863, 997, 794, 587, 124, 321, 212,
                957, 764, 173, 314, 422, 927, 783, 930, 282, 306, 506, 44, 926, 691, 568, 68, 730,
                933, 737, 531, 180, 414, 751, 28, 546, 60, 371, 493, 370, 527, 387, 43, 541, 13,
                457, 328, 227, 652, 365, 430, 803, 59, 858, 538, 427, 583, 368, 375, 173, 809, 896,
                370, 789};
        indices = t.twoSum(complex, 542);
        System.out.println(Arrays.toString(indices));
        assertThat(indices, is(new int[] {29, 46}));
    }

}
