package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement regular expression matching with support for '.' and '*'.
 * <p>
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * <p>
 * The matching should cover the entire input string (not partial).
 * <p>
 * The function prototype should be:
 * bool isMatch(const char *s, const char *p)
 * <p>
 * Some examples:
 * isMatch("aa","a") → false
 * isMatch("aa","aa") → true
 * isMatch("aaa","aa") → false
 * isMatch("aa", "a*") → true
 * isMatch("aa", ".*") → true
 * isMatch("ab", ".*") → true
 * isMatch("aab", "c*a*b") → true
 * <p>
 * 首先要理解题意:
 * "a"对应"a", 这种匹配不解释了
 * 任意字母对应".", 这也是正则常见
 * 0到多个相同字符x,对应"x*", 比起普通正则,这个地方多出来一个前缀x. x代表的是 相同的字符中取一个,比如"aaaab"对应是"a*b"
 * "*"还有一个易于疏忽的地方就是它的"贪婪性"要有一个限度.比如"aaa"对应"a*a", 代码逻辑不能一路贪婪到底
 * 正则表达式如果期望着一个字符一个字符的匹配,是非常不现实的.而"匹配"这个问题,非 常容易转换成"匹配了一部分",整个匹配不匹配,要看"剩下的匹配"情况.这就很好的把 一个大的问题转换成了规模较小的问题:递归
 * 确定了递归以后,使用java来实现这个问题,会遇到很多和c不一样的地方,因为java对字符 的控制不像c语言指针那么灵活charAt一定要确定某个位置存在才可以使用.
 * 如果pattern是"x*"类型的话,那么pattern每次要两个两个的减少.否则,就是一个一个 的减少. 无论怎样减少,都要保证pattern有那么多个.比如s.substring(n), 其中n 最大也就是s.length()
 * <p>
 * (p.charAt(1) == '*')这个条件下需要分两种情况，p直接去掉前两个匹配，
 * 1）s不去掉，例如s=abc，p=a*bc，下一个递归比较s=abc, p=bc，不OK
 * 2）s一个个去掉，先去掉a，下一个递归比较s=bc, p=bc，OK了就行
 *
 * @author zhangxu
 */
public class RegularExpressionMatching {

    public boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        if (p.length() == 0) {
            return s.length() == 0;
        } else if (p.length() == 1) {
            if (s.length() < 1) {
                return false;
            }
            if (s.charAt(0) != p.charAt(0) && p.charAt(0) != '.') {
                return false;
            }
            return isMatch(s.substring(1), p.substring(1)); //避免aa,a的情况
        } else if (p.charAt(1) == '*') {
            if (isMatch(s, p.substring(2))) {
                return true;
            }
            int i = 0;
            while (i < s.length() && (s.charAt(i) == p.charAt(0) || p.charAt(0) == '.')) {
                if (isMatch(s.substring(i + 1), p.substring(2))) {
                    return true;
                }
                i++;
            }
            return false;
        } else {
            if (s.length() < 1) {
                return false;
            }
            if (s.charAt(0) != p.charAt(0) && p.charAt(0) != '.') {
                return false;
            }
            return isMatch(s.substring(1), p.substring(1));
        }
    }

    @Test
    public void test() {
        assertThat(isMatch("aa", "a"), Matchers.is(false));
        assertThat(isMatch("aa", "aa"), Matchers.is(true));
        assertThat(isMatch("aaa", "aa"), Matchers.is(false));
        assertThat(isMatch("aa", "a*"), Matchers.is(true));
        assertThat(isMatch("aa", ".*"), Matchers.is(true));
        assertThat(isMatch("ab", ".*"), Matchers.is(true));
        assertThat(isMatch("aab", "c*a*b"), Matchers.is(true));
        assertThat(isMatch("a", ".*..a*"), Matchers.is(false));
    }
}
