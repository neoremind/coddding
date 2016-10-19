package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an array of words and a length L, format the text such that each line has exactly L characters and is fully
 * (left and right) justified.
 * <p>
 * You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra
 * spaces ' ' when necessary so that each line has exactly L characters.
 * <p>
 * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line do not
 * divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.
 * <p>
 * For the last line of text, it should be left justified and no extra space is inserted between words.
 * <p>
 * For example,
 * words: ["This", "is", "an", "example", "of", "text", "justification."]
 * L: 16.
 * <p>
 * Return the formatted lines as:
 * [
 * "This    is    an",
 * "example  of text",
 * "justification.  "
 * ]
 * Note: Each word is guaranteed not to exceed L in length.
 * <p>
 * click to show corner cases.
 * <p>
 * Corner Cases:
 * A line other than the last line might contain only one word. What should you do in this case?
 * In this case, that line should be left-justified.
 * <p>
 * <pre>
 *     1）遍历words，累计单词长度+空格数量 <= maxWidth就一直继续。
 *     2）超过了maxWidth，证明这几个单词可以构成一行，分两种情况
 *       2.1）只有一个单词，全部在左边，右边补空格。
 *       2.2）多个单词利用 {maxWidth - wordLen / wordNum - 1} 计算空格间隙
 *       2.2）多个单词利用 {maxWidth - wordLen % wordNum - 1} 计算多余的空格
 *     3）用sb加单词，最后一个单词没有空格。所以注意i = len - 1的情况
 *     4）同时记录一个last表示这一批结束的index。还原一个sb用sb.setLength(0)。
 *
 *     5）上面都技术了，那么就只剩下最后一行了，题目要求最后一行不用补空格，所以按规矩一个单词，一个空格添加
 *     6）使用maxWidht - sb.length来补其他空格。
 * </pre>
 * <p>
 * https://leetcode.com/problems/text-justification/
 *
 * @author zhangxu
 */
public class TextJustification {

    public List<String> fullJustify(String[] words, int maxWidth) {
        if (words == null || words.length == 0) {
            return Collections.emptyList();
        }
        List<String> res = new ArrayList<>();
        if (maxWidth == 0) {
            res.add("");
            return res;
        }

        int wordLen = 0;
        int wordNum = 0;
        int last = 0;
        List<String> subWords = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            String currWord = words[i];
            if (currWord.length() + wordLen + wordNum <= maxWidth) {
                subWords.add(currWord);
                wordLen += currWord.length();
                wordNum++;
                continue;
            }
            last = i;  //最后一行的一个起点
            StringBuilder sb = new StringBuilder();
            if (wordNum == 1) {
                sb.append(subWords.get(0));
                appendMultipleSpace(sb, maxWidth - wordLen);
            } else {
                int space = (maxWidth - wordLen) / (wordNum - 1);  //计算空格的空格数
                int extra = (maxWidth - wordLen) % (wordNum - 1);  //计算多余的留白，从第一个单词往后都多加一个space
                for (int j = 0; j < subWords.size(); j++) {
                    sb.append(subWords.get(j));
                    if (extra > 0) {
                        sb.append(" ");
                        extra--;
                    }
                    if (j != subWords.size() - 1) {
                        appendMultipleSpace(sb, space);
                    }
                }
            }
            i--;
            wordLen = 0;
            wordNum = 0;
            subWords.clear();
            res.add(sb.toString());
        }

        // 处理最后一行
        StringBuilder sb = new StringBuilder();
        for (int k = last; k < words.length; k++) {
            sb.append(words[k]);
            if (k != words.length - 1) {  //最后一个单词不要加空格，方式"a"这种情况，从而多加了一个。
                sb.append(" ");
            }
        }
        while (sb.length() < maxWidth) {
            sb.append(" ");
        }
        res.add(sb.toString());
        return res;
    }

    void appendMultipleSpace(StringBuilder sb, int num) {
        for (int i = 0; i < num; i++) {
            sb.append(" ");
        }
    }

    @Test
    public void test() {
        String[] words = new String[] {"This", "is", "an", "example", "of", "text", "justification."};
        List<String> res = fullJustify(words, 16);
        res.stream().forEach(System.out::println);

        words = new String[] {"What", "must", "be", "shall", "be."};
        res = fullJustify(words, 12);
        res.stream().forEach(System.out::println);

        words = new String[] {"a"};
        res = fullJustify(words, 1);
        res.stream().forEach(System.out::println);
    }

}
