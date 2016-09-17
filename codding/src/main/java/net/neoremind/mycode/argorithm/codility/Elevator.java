package net.neoremind.mycode.argorithm.codility;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class Elevator {

    public int solution(int[] A, int[] B, int M, int X, int Y) {
        if (A == null || B == null || A.length == 0 || B.length == 0 || X < 1 || Y < 1) {
            return 0;
        }
        int res = 0;
        Queue<Task> taskQueue = new LinkedList<>();
        for (int i = 0; i < A.length; i++) {
            if (A[i] > Y) {
                throw new RuntimeException("This should not happen!");
            }
            taskQueue.add(new Task(A[i], B[i]));
        }
        while (!taskQueue.isEmpty()) {
            res += helper(taskQueue, M, X, Y);
        }
        return res;
    }

    /**
     * Process as much tasks as possible and then remove tasks from task queue.
     * <p>
     * Note: Here we use <code>bitmap</code> for enhance performance, while <code>set</code> is also an alternative.
     *
     * @param taskQueue task queue
     * @param M         floor range
     * @param X         max persons
     * @param Y         max weigth
     *
     * @return stops
     */
    int helper(Queue<Task> taskQueue, int M, int X, int Y) {
        BitSet floors = new BitSet(M);
        while (!taskQueue.isEmpty() && X > 0 && (Y - taskQueue.peek().weight) >= 0) {
            Task task = taskQueue.poll();
            X -= 1;
            Y -= task.weight;
            floors.set(task.floor);
        }
        return floors.cardinality() + 1;
    }

    /**
     * Task
     * <p>
     * For simplicity, here I do not let it be a POJO, just expose field publicly.
     */
    class Task {
        int weight;
        int floor;

        public Task(int weight, int floor) {
            this.weight = weight;
            this.floor = floor;
        }
    }

    @Test
    public void test() {
        int[] A = new int[] {60, 80, 40};
        int[] B = new int[] {2, 3, 5};
        int M = 5;
        int X = 2;
        int Y = 200;
        assertThat(solution(A, B, M, X, Y), Matchers.is(5));

        A = new int[] {40, 40, 100, 80, 20};
        B = new int[] {3, 3, 2, 2, 3};
        M = 3;
        X = 5;
        Y = 200;
        assertThat(solution(A, B, M, X, Y), Matchers.is(6));

        A = new int[] {40, 40, 100, 200, 20};
        B = new int[] {3, 3, 2, 2, 3};
        M = 3;
        X = 5;
        Y = 200;
        assertThat(solution(A, B, M, X, Y), Matchers.is(7));

        A = new int[] {40, 40, 100, 200, 20};
        B = new int[] {3, 3, 2, 2, 3};
        M = 3;
        X = 1;
        Y = 200;
        assertThat(solution(A, B, M, X, Y), Matchers.is(10));
    }
}
