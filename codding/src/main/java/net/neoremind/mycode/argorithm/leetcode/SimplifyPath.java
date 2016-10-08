package net.neoremind.mycode.argorithm.leetcode;

import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Given an absolute path for a file (Unix-style), simplify it.
 * <p>
 * For example,
 * path = "/home/", => "/home"
 * path = "/a/./b/../../c/", => "/c"
 * click to show corner cases.
 * <p>
 * Corner Cases:
 * Did you consider the case where path = "/../"?
 * In this case, you should return "/".
 * Another corner case is the path might contain multiple slashes '/' together, such as "/home//foo/".
 * In this case, you should ignore redundant slashes and return "/home/foo".
 * <p>
 * https://leetcode.com/problems/simplify-path/
 *
 * @author zhangxu
 */
public class SimplifyPath {

    public String simplifyPath(String path) {
        if (path == null || path.length() == 0) {
            return path;
        }
        String[] strs = path.split("/");
        Stack<String> stack = new Stack<String>();
        for (String str : strs) {
            if (str.equals("") || str.equals(".")) {
                continue;
            } else if (str.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(str);
            }
        }
        String[] res = new String[stack.size()];
        for (int i = stack.size() - 1; i >= 0; i--) {
            res[i] = stack.pop();
        }
        return Arrays.stream(res).collect(Collectors.joining("/", "/", ""));
    }

}
