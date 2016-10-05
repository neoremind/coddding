package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * All DNA is composed of a series of nucleotides abbreviated as A, C, G, and T, for example: "ACGAATTCCG". When
 * studying DNA, it is sometimes useful to identify repeated sequences within the DNA.
 * <p>
 * Write a function to find all the 10-letter-long sequences (substrings) that occur more than once in a DNA molecule.
 * <p>
 * For example,
 * <p>
 * Given s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT",
 * <p>
 * Return:
 * ["AAAAACCCCC", "CCCCCAAAAA"].
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/repeated-dna-sequences/
 */
public class RepeatedDNASequences {

    public List<String> findRepeatedDnaSequences(String s) {
        Map<Long, Integer> map = new HashMap<>();
        Set<String> res = new HashSet<>();
        char[] str = s.toCharArray();
        for (int i = 0; i <= str.length - 10; i++) {
            long sign = getSign(str, i);
            map.put(sign, map.getOrDefault(sign, 0) + 1);
            if (map.get(sign) > 1) {
                res.add(new String(str, i, 10));
            }
        }
        return new ArrayList<>(res);
    }

    private long getSign(char[] str, int start) {
        long sign = 0;
        for (int i = start; i < start + 10; i++) {
            sign |= (str[i] - 'A');
            sign <<= 5;
        }
        return sign;
    }

    @Test
    public void test() {
        List<String> res = findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT");
        System.out.println(res);
        assertThat(res, Matchers.is(Lists.newArrayList("AAAAACCCCC", "CCCCCAAAAA")));
    }

}
