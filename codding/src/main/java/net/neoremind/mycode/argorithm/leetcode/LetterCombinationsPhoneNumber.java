package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Given a digit string, return all possible letter combinations that the number could represent.
 * <p>
 * A mapping of digit to letters (just like on the telephone buttons) is given below.
 * <p>
 * Input:Digit string "23"
 * Output: ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
 * <p>
 * 回溯法 + DFS，类似于笛卡尔乘积的方式
 *
 * @author zhangxu
 */
public class LetterCombinationsPhoneNumber {

    final char[][] charMap = {
            {}, //0
            {}, //1
            {'a', 'b', 'c'}, //2
            {'d', 'e', 'f'}, //3
            {'g', 'h', 'i'}, //4
            {'j', 'k', 'l'}, //5
            {'m', 'n', 'o'}, //6
            {'p', 'q', 'r', 's'}, //7
            {'t', 'u', 'v'}, //8
            {'w', 'x', 'y', 'z'} //9
    };

    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return Collections.emptyList();
        }
        List<String> res = new ArrayList<>();
        char[] combination = new char[digits.length()];
        find(res, combination, 0, digits);
        return res;
    }

    public void find(List<String> res, char[] combination, int index, String digits) {
        if (index == digits.length()) {
            res.add(new String(combination));
        } else {
            int digit = digits.charAt(index) - '0';
            for (char c : charMap[digit]) {
                combination[index] = c;
                find(res, combination, index + 1, digits);
            }
        }
    }

    @Test
    public void testPowerOf3() {
        assertThat(letterCombinations("23"),
                Matchers.is(Lists.newArrayList("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf")));
        assertThat(letterCombinations(""),
                Matchers.is(Lists.newArrayList()));
    }
}
