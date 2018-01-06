package net.neoremind.mycode.argorithm.leetcode;

import java.util.*;

/**
 * Given a non-empty array of integers, return the k most frequent elements.
 * <p>
 * For example,
 * Given [1,1,1,2,2,3] and k = 2, return [1,2].
 * <p>
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
 * Your algorithm's time complexity must be better than O(n log n), where n is the array's size.
 *
 * @author zhangxu
 *         https://leetcode.com/problems/top-k-frequent-elements/
 */
public class TopKFrequentElements {

    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> num2freq = new HashMap<>(nums.length / 3 * 4);
        for (int num : nums) {
            num2freq.put(num, num2freq.getOrDefault(num, 0) + 1);
        }

        ArrayList<Integer>[] freq2numList = new ArrayList[nums.length + 1];
        for (Map.Entry<Integer, Integer> e : num2freq.entrySet()) {
            if (freq2numList[e.getValue()] == null) {
                freq2numList[e.getValue()] = new ArrayList<>();
            }
            freq2numList[e.getValue()].add(e.getKey());
        }

        List<Integer> res = new ArrayList<>(k);
        for (int i = nums.length; i >= 0; i--) {
            if (freq2numList[i] != null) {
                for (int num : freq2numList[i]) {
                    if (res.size() < k) {
                        res.add(num);
                    }
                }
            }
        }
        return res;
    }

    public List<Integer> topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        PriorityQueue<Map.Entry<Integer, Integer>> maxHeap =
                new PriorityQueue<>((a, b) -> (b.getValue() - a.getValue()));
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            maxHeap.add(entry);
        }

        List<Integer> res = new ArrayList<>();
        while (res.size() < k) {
            Map.Entry<Integer, Integer> entry = maxHeap.poll();
            res.add(entry.getKey());
        }
        return res;
    }

}
