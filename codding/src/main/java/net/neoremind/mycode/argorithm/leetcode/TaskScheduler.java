package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertThat;

/**
 * Given a char array representing tasks CPU need to do. It contains capital letters A to Z where different letters
 * represent different tasks.Tasks could be done without original order. Each task could be done in one interval. For
 * each interval, CPU could finish one task or just be idle.
 * <p>
 * However, there is a non-negative cooling interval n that means between two same tasks, there must be at least n
 * intervals that CPU are doing different tasks or just be idle.
 * <p>
 * You need to return the least number of intervals the CPU will take to finish all the given tasks.
 * <p>
 * Example 1:
 * Input: tasks = ["A","A","A","B","B","B"], n = 2
 * Output: 8
 * Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.
 * Note:
 * The number of tasks is in the range [1, 10000].
 * The integer n is in the range [0, 100].
 * <p>
 * Array, queue, greedy
 *
 * @author xu.zhang
 */
public class TaskScheduler {

    public int leastInterval(char[] tasks, int n) {
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        Map<Character, Integer> task2cnt = new HashMap<>(tasks.length * 4 / 3);
        for (char task : tasks) {
            task2cnt.put(task, task2cnt.getOrDefault(task, 0) + 1);
        }
        Queue<Map.Entry<Character, Integer>> q = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue()
        );
        q.addAll(task2cnt.entrySet());
        int totalSlot = 0;
        while (!q.isEmpty()) {
            int k = n + 1;
            List<Map.Entry<Character, Integer>> temp = new ArrayList<>();
            while (k > 0 && !q.isEmpty()) {
                temp.add(q.poll());
                k--;
            }
            for (Map.Entry<Character, Integer> e : temp) {
                e.setValue(e.getValue() - 1);
                if (e.getValue() != 0) {
                    q.add(e);
                }
            }
            if (!q.isEmpty()) {
                totalSlot += n + 1;
            } else {
                totalSlot += temp.size();
            }
        }
        return totalSlot;
    }

    /**
     * return string
     */
    public String leastInterval2(char[] tasks, int n) {
        if (tasks == null || tasks.length == 0) {
            return "";
        }
        Map<Character, Integer> task2cnt = new HashMap<>(tasks.length * 4 / 3);
        for (char task : tasks) {
            task2cnt.put(task, task2cnt.getOrDefault(task, 0) + 1);
        }
        Queue<Map.Entry<Character, Integer>> q = new PriorityQueue<>(
                (a, b) -> b.getValue() - a.getValue()
        );
        q.addAll(task2cnt.entrySet());
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            int k = n + 1;
            List<Map.Entry<Character, Integer>> temp = new ArrayList<>();
            while (k > 0 && !q.isEmpty()) {
                temp.add(q.poll());
                k--;
            }
            for (Map.Entry<Character, Integer> e : temp) {
                e.setValue(e.getValue() - 1);
                if (e.getValue() != 0) {
                    q.add(e);
                }
            }
            if (!q.isEmpty()) {
                for (Map.Entry<Character, Integer> e : temp) {
                    sb.append(e.getKey());
                }
                for (int i = 0; i < (n + 1) - temp.size(); i++) {
                    sb.append("#");
                }
            } else {
                for (Map.Entry<Character, Integer> e : temp) {
                    sb.append(e.getKey());
                }
            }
        }
        return sb.toString();
    }

    @Test
    public void test() {
        char[] tasks = new char[]{'A', 'A', 'A', 'B', 'B', 'B'};
        int result = leastInterval(tasks, 2);
        // AB#AB#AB
        System.out.println(leastInterval2(tasks, 2));
        assertThat(result, Matchers.is(8));

        tasks = new char[]{'A', 'B', 'C', 'D'};
        result = leastInterval(tasks, 2);
        // ADCB
        System.out.println(leastInterval2(tasks, 2));
        assertThat(result, Matchers.is(4));

        tasks = new char[]{'A', 'B', 'A', 'X', 'B'};
        result = leastInterval(tasks, 3);
        // ABX#AB
        System.out.println(leastInterval2(tasks, 3));
        assertThat(result, Matchers.is(6));

        tasks = new char[]{'A', 'A', 'A', 'A', 'B', 'B', 'B', 'E', 'E', 'F', 'F'};
        result = leastInterval(tasks, 2);
        // -              //A-4,B-3,E-2,F-2
        // ABE            //A-3,B-2,E-1,F-2
        // ABE|AFB        //A-2,B-1,E-1,F-1
        // ABE|AFB|ABF    //A-1,B-0,E-1,F-0
        // ABE|AFB|ABF|EA //A-0,B-0,E-0,F-0
        System.out.println(leastInterval2(tasks, 2));
        assertThat(result, Matchers.is(11));
    }
}
