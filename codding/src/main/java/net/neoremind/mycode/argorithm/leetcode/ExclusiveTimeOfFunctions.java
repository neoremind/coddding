package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertThat;

/**
 * Given the running logs of n functions that are executed in a nonpreemptive single threaded CPU, find the exclusive
 * time of these functions.
 * <p>
 * Each function has a unique id, start from 0 to n-1. A function may be called recursively or by another function.
 * <p>
 * A log is a string has this format : function_id:start_or_end:timestamp. For example, "0:start:0" means function 0
 * starts from the very beginning of time 0. "0:end:0" means function 0 ends to the very end of time 0.
 * <p>
 * Exclusive time of a function is defined as the time spent within this function, the time spent by calling other
 * functions should not be considered as this function's exclusive time. You should return the exclusive time of each
 * function sorted by their function id.
 * <p>
 * Example 1:
 * Input:
 * n = 2
 * logs =
 * ["0:start:0",
 * "1:start:2",
 * "1:end:5",
 * "0:end:6"]
 * Output:[3, 4]
 * Explanation:
 * Function 0 starts at time 0, then it executes 2 units of time and reaches the end of time 1.
 * Now function 0 calls function 1, function 1 starts at time 2, executes 4 units of time and end at time 5.
 * Function 0 is running again at time 6, and also end at the time 6, thus executes 1 unit of time.
 * So function 0 totally execute 2 + 1 = 3 units of time, and function 1 totally execute 4 units of time.
 * Note:
 * Input logs will be sorted by timestamp, NOT log id.
 * Your output should be sorted by function id, which means the 0th element of your output corresponds to the
 * exclusive time of function 0.
 * Two functions won't start or end at the same time.
 * Functions could be called recursively, and will always end.
 * 1 <= n <= 100
 *
 * @author xu.zhang
 */
public class ExclusiveTimeOfFunctions {

    public int[] exclusiveTime(int n, List<String> logs) {
        if (n == 0 || logs == null || logs.size() == 0) {
            return new int[]{};
        }
        Stack<Integer> stack = new Stack<>();
        int[] res = new int[n];
        int prevTime = 0;
        for (String s : logs) {
            String[] fields = s.split(":");
            int funcId = Integer.parseInt(fields[0]);
            String action = fields[1];
            int time = Integer.parseInt(fields[2]);
            if (!stack.isEmpty()) {
                res[stack.peek()] += time - prevTime;
            }
            prevTime = time;
            if (action.equals("start")) {
                stack.push(funcId);
            } else {
                res[stack.pop()]++;
                prevTime++;
            }
        }
        return res;
    }

    @Test
    public void test() {
        List<String> logs = Lists.newArrayList(
                "0:start:0",
                "1:start:2",
                "1:end:5",
                "0:end:6"
        );
        assertThat(exclusiveTime(2, logs), Matchers.is(new int[]{3, 4}));

        logs = Lists.newArrayList(
                "0:start:0",
                "0:end:8"
        );
        assertThat(exclusiveTime(1, logs), Matchers.is(new int[]{9}));
    }

}
