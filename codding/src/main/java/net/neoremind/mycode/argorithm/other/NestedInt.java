package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author zhangxu
 */
public class NestedInt {
    private static class NestedIntList {
        private int value;
        private boolean isNumber;
        private List<NestedIntList> intList;

        // Constructor to construct a number
        public NestedIntList(int value) {
            this.value = value;
            isNumber = true;
        }

        // Constructor to construct a list
        public NestedIntList() {
            intList = new ArrayList<>();
            isNumber = false;
        }

        public void add(NestedIntList num) {
            this.intList.add(num);
        }

        public NestedIntList miniParser(String s) {
            if (s == null || s.length() == 0) {
                return null;
            }

            // Corner case "123"
            if (s.charAt(0) != '[') {
                int num = Integer.parseInt(s);
                return new NestedIntList(num);
            }

            int i = 0;
            int left = 1;
            Stack<NestedIntList> stack = new Stack<>();
            NestedIntList result = null;

            while (i < s.length()) {
                char c = s.charAt(i);
                if (c == '[') {
                    NestedIntList num = new NestedIntList();
                    if (!stack.isEmpty()) {
                        stack.peek().add(num);
                    }
                    stack.push(num);
                    left = i + 1;
                } else if (c == ',' || c == ']') {
                    if (left != i) {
                        int value = Integer.parseInt(s.substring(left, i));
                        NestedIntList num = new NestedIntList(value);
                        stack.peek().add(num);
                    }
                    left = i + 1;

                    if (c == ']') {
                        result = stack.pop();
                    }
                }

                i++;
            }

            return result;
        }

        public String toString() {
            if (this.isNumber) {
                return this.value + "";
            } else {
                return this.intList.toString();
            }
        }
    }

    public static void main(String[] args) {
        NestedIntList nestedIntList = new NestedIntList();
        String input = "[123,456,[788,799,833],[[]],10,[]]";
        NestedIntList result = nestedIntList.miniParser(input);
        System.out.println(result.toString());
    }
}
