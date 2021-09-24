package net.neoremind.mycode.argorithm.leetcode;

import java.util.Stack;

/**
 * @author xu.zx
 */
public class EvaluateReversePolishNotation {

  public int evalRPN(String[] tokens) {
    if (tokens == null || tokens.length == 0) return 0;
    Stack<Integer> s = new Stack<>();
    for (int i = 0; i < tokens.length; i++) {
      String t = tokens[i];
      if (isNumber(t)) {
        s.push(Integer.parseInt(t));
      } else {
        int a = s.pop();
        int b = s.pop();
        if (t.equals("+")) {
          s.push(b + a);
        } else if (t.equals("-")) {
          s.push(b - a);
        } else if (t.equals("*")) {
          s.push(b * a);
        } else if (t.equals("/")) {
          s.push(b / a);
        } else {
          throw new RuntimeException("invalid");
        }
      }
    }
    if (!s.isEmpty() && s.size() == 1) {
      return s.pop();
    }
    throw new RuntimeException("should not happen");
  }

  private boolean isNumber(String s) {
    return (s.charAt(0) >= '0' && s.charAt(0) <= '9') || (s.charAt(0) == '-' && s.length() > 1);
  }
}
