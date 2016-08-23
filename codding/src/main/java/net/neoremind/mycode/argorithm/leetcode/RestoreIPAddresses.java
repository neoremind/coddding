package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a string containing only digits, restore it by returning all possible valid IP address combinations.
 * <p>
 * For example:
 * Given "25525511135",
 * <p>
 * return ["255.255.11.135", "255.255.111.35"]. (Order does not matter)
 * <p>
 * 流泪了，第一次完成回溯的一道题目，循环的时候差一点点写对。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/restore-ip-addresses/
 */
public class RestoreIPAddresses {

    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), s, 0, s.length(), 0);
        return result;
    }

    public void backtrack(List<String> result, List<Integer> tempList, String s,
                          int start, int length, int maskNum) {
        if (maskNum == 4 && start == length) {
            StringBuilder builder = new StringBuilder();
            for (Integer num : tempList) {
                builder.append(num);
                builder.append(".");
            }
            String str = builder.toString();
            result.add(str.substring(0, str.length() - 1));
            return;
        }
        if (maskNum > 4) {
            return;
        }
        for (int j = 1; j < 4 && start + j <= length; j++) {
            String num = s.substring(start, start + j);
            if (isValid(num)) {
                tempList.add(Integer.parseInt(num));
                backtrack(result, tempList, s, start + j, length, maskNum + 1);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    public boolean isValid(String num) {
        if (num.startsWith("0") && num.length() > 1) {
            return false;
        }
        Integer n = Integer.parseInt(num);
        return n >= 0 && n < 256;
    }

    @Test
    public void test() {
        String s = "1234";
        List<String> result = restoreIpAddresses(s);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(1));

        s = "25525511135";
        result = restoreIpAddresses(s);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(2));

        s = "010010";
        result = restoreIpAddresses(s);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(2));
    }
}
