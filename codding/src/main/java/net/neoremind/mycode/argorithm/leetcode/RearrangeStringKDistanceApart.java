package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a non-empty string str and an integer k, rearrange the string such that the same characters are at least
 * distance k from each other.
 * <p>
 * All input strings are given in lowercase letters. If it is not possible to rearrange the string, return an empty
 * string "".
 * <p>
 * Example 1:
 * str = "aabbcc", k = 3
 * <p>
 * Result: "abcabc"
 * <p>
 * The same letters are at least distance 3 from each other.
 * Example 2:
 * str = "aaabc", k = 3
 * <p>
 * Answer: ""
 * <p>
 * It is not possible to rearrange the string.
 * Example 3:
 * str = "aaadbbcc", k = 2
 * <p>
 * Answer: "abacabcd"
 * <p>
 * Another possible answer is: "abcabcda"
 * <p>
 * The same letters are at least distance 2 from each other.
 * <p>
 * https://leetcode.com/problems/rearrange-string-k-distance-apart/
 *
 * @author zhangxu
 */
public class RearrangeStringKDistanceApart {

    //    public String rearrangeString(String str, int k) {
    //        if (k <= 0) {
    //            return str;
    //        }
    //        int[] freq = new int[26];
    //        char[] sa = str.toCharArray();
    //        for (char c : sa) {
    //            freq[c - 'a']++;
    //        }
    //        int interval = sa.length / k;
    //        int reminder = sa.length % k;
    //        int c = 0;
    //        for (int ch : freq) {
    //            if (ch - interval > 1) {
    //                return "";
    //            }
    //            if (ch - interval == 1) {
    //                c++;
    //            }
    //        }
    //        if (c > reminder) {
    //            return "";
    //        }
    //        Integer[] pos = new Integer[26];
    //        for (int i = 0; i < pos.length; i++) {
    //            pos[i] = i;
    //        }
    //        Arrays.sort(pos, (i1, i2) -> freq[pos[i2]] - freq[pos[i1]]);
    //        char[] result = new char[sa.length];
    //        for (int i = 0, j = 0, p = 0; i < sa.length; i++) {
    //            result[j] = (char) (pos[p] + 'a');
    //            if (--freq[pos[p]] == 0) {
    //                p++;
    //            }
    //            j += k;
    //            if (j >= sa.length) {
    //                j %= k;
    //                j++;
    //            }
    //        }
    //        return new String(result);
    //    }

    public String rearrangeString(String str, int k) {
        Map<Character, Integer> char2Freq = new HashMap<>();
        for (char ch : str.toCharArray()) {
            char2Freq.put(ch, char2Freq.getOrDefault(ch, 0) + 1);
        }
        PriorityQueue<Map.Entry<Character, Integer>> queue =
                new PriorityQueue<>((o1, o2) -> o2.getValue() - o1.getValue());
        Queue<Map.Entry<Character, Integer>> pending = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        queue.addAll(char2Freq.entrySet());
        while (!queue.isEmpty()) {
            Map.Entry<Character, Integer> e = queue.poll();
            sb.append(e.getKey());
            pending.add(e);
            if (pending.size() < k) {
                continue;
            }
            Map.Entry<Character, Integer> firstRelease = pending.poll();
            firstRelease.setValue(firstRelease.getValue() - 1);
            if (firstRelease.getValue() > 0) {
                queue.add(firstRelease);
            }
        }
        return sb.length() == str.length() ? sb.toString() : "";
    }

    @Test
    public void test() {
        String res = rearrangeString("aabbcc", 2);
        assertThat(res, Matchers.is("acbacb"));

        res = rearrangeString("aabbcc", 3);
        assertThat(res, Matchers.is("acbacb"));

        res = rearrangeString("aabbcc", 4);
        assertThat(res, Matchers.is(""));
    }
}
