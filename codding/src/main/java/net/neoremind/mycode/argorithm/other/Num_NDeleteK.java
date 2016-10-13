package net.neoremind.mycode.argorithm.other;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 题目：一个n位的数，去掉其中的k位，问怎样去掉使得留下来的那个（n-k）位的数最小或者最大？
 * <p>
 * 分析：（删数问题，可用贪心算法求解），方法就是从简单入手，慢慢复杂。从n=1开始推导就会发现规律，
 * <p>
 * 现在假设有一个数，124682385，
 * <p>
 * 假如k = 1，则结果为12462385,k = 2，结果为1242385……
 * <p>
 * 最优解是删除出现的第一个左边>右边的数，因为删除之后高位减小，很容易想...那全局最优解也就是这个了，因为删除S个数字就相当于执行了S次删除一个数，因为留下的数总是当前最优解...
 *
 * @author zhangxu
 */
//TODO
public class Num_NDeleteK {

    int max(int n, int k) {
        String s = n + "";
        char[] str = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        sb.append(str[0]);
        for (int i = 1; i < str.length; i++) {
            if (counter == k) {
                sb.append(str[i]);
                continue;
            }
            if (str[i] > str[i - 1]) {
                sb.deleteCharAt(sb.length() - 1);
                counter++;
            }
            sb.append(str[i]);
        }
        return Integer.parseInt(sb.toString());
    }

    int min(int n, int k) {
        String s = n + "";
        char[] str = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        sb.append(str[0]);
        boolean isAsc = true;
        for (int i = 1; i < str.length; i++) {
            if (counter == k) {
                sb.append(str[i]);
                continue;
            }
            if (str[i] < str[i - 1]) {
                sb.deleteCharAt(sb.length() - 1);
                counter++;
                isAsc = false;
            }
            sb.append(str[i]);
        }
        if (isAsc) {
            return Integer.parseInt(sb.substring(0, str.length - k));
        }
        if (counter != k) {
            return min(Integer.parseInt(sb.toString()), k - counter);
        }
        return Integer.parseInt(sb.toString());
    }

    void allCombination(int n, int m) {
        List<Integer> p = permutation(n, (n + "").length() - m);
        System.out.println("n=" + n + ", m=" + m);
        System.out.println(p.stream().mapToInt(i -> i).max().getAsInt());
        System.out.println(p.stream().mapToInt(i -> i).min().getAsInt());
        System.out.println("================");
    }

    List<Integer> permutation(int n, int m) {
        String s = n + "";
        List<Integer> res = new ArrayList<>();
        backtrack(res, "", s.toCharArray(), 0, m);
        return res;
    }

    void backtrack(List<Integer> res, String temp, char[] str, int idx, int m) {
        if (temp.length() == m) {
            res.add(Integer.parseInt(temp));
        } else {
            for (int i = idx; i < str.length; i++) {
                temp += str[i];
                backtrack(res, temp, str, i + 1, m);
                temp = temp.substring(0, temp.length() - 1);
            }
        }
    }

    void swap(int i, int j, char[] array) {
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void test() {
        assertThat(max(51342, 2), Matchers.is(542));
        assertThat(max(12462385, 2), Matchers.is(462385));
        assertThat(max(12462385, 3), Matchers.is(62385));
        assertThat(max(12462385, 4), Matchers.is(6385));

        assertThat(min(51342, 2), Matchers.is(132));
        assertThat(min(12462385, 2), Matchers.is(124235));
        assertThat(min(12462385, 3), Matchers.is(12235));
        assertThat(min(12462385, 4), Matchers.is(1223));

        allCombination(12462385, 2);
    }

}
