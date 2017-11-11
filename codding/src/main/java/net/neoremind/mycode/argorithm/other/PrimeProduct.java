package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给一个质数数组（无重复），输出数组元素所有可能的乘积
 *
 * @author xu.zhang
 */
//TODO
public class PrimeProduct {

    public List<Integer> allPrimeProduct(int[] nums) {
        List<Integer> res = new ArrayList<>();
        Arrays.sort(nums);
        helper(nums, 0, res);
        return res;
    }

    private void helper(int[] nums, int i, List<Integer> res) {
        if (i == nums.length) {
            return;
        } else {
            res.add(nums[i]);
            for (int j = 0; j < nums.length; j++) {

            }
        }
    }

    @Test
    public void test() {
        int[] nums = new int[]{};
        System.out.println(allPrimeProduct(nums));
    }

}
