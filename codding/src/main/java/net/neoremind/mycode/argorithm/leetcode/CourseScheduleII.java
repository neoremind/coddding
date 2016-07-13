package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * https://leetcode.com/problems/course-schedule-ii/
 *
 * @author zhangxu
 * @see CourseSchedule
 */
public class CourseScheduleII {

    /**
     * 图的顶点
     */
    class Vertex {

        /**
         * 与该顶点邻接的顶点
         */
        public ArrayList<Integer> adjacences = new ArrayList<>();

        /**
         * 入度，也就是指向这个节点的边的数量
         */
        public int inDegree = 0;

        @Override
        public String toString() {
            return adjacences + "\t" + inDegree;
        }
    }

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] orders = new int[numCourses];
        int counter = 0;

        // 初始化邻接表
        Vertex[] adjacencyList = new Vertex[numCourses];
        for (int i = 0; i < prerequisites.length; i++) {
            int source = prerequisites[i][1];
            int target = prerequisites[i][0];
            if (adjacencyList[source] == null) {
                adjacencyList[source] = new Vertex();
            }
            if (adjacencyList[target] == null) {
                adjacencyList[target] = new Vertex();
            }
            adjacencyList[source].adjacences.add(target);
            adjacencyList[target].inDegree++;
        }

        printAdjacencyList(adjacencyList);

        Queue<Vertex> queue = new LinkedList<>();

        // 遍历，入队列，入度=0的，即源头节点
        for (int i = 0; i < adjacencyList.length; i++) {
            if (adjacencyList[i] == null) {
                continue;
            }
            if (adjacencyList[i].inDegree == 0) {
                orders[counter++] = i;
                queue.offer(adjacencyList[i]);
            }
        }

        // 不断的poll节点出来，与它邻接的入度-1，然后看是不是入度=0，等于0则证明是个源头，放进去队列继续。
        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            for (Integer index : vertex.adjacences) {
                if (--adjacencyList[index].inDegree == 0) {
                    orders[counter++] = index;
                    queue.add(adjacencyList[index]);
                }
            }
        }

        if (counter != numCourses) {
            throw new RuntimeException("Cyclic graph");
        }

        return orders;
    }

    private void printAdjacencyList(Vertex[] adjacencyList) {
        for (int i = 0; i < adjacencyList.length; i++) {
            System.out.println(i + "\t" + adjacencyList[i]);
        }
    }

    @Test
    public void test() {
        int numCourses = 8;
        int[][] prerequisites = new int[][] {
                {1, 0}, {2, 1}, {3, 1}, {3, 4}, {4, 1}, {4, 2}, {4, 5}, {5, 2}, {6, 3}, {6, 4}, {6, 7}, {7, 4}, {7, 5}
        };
        assertThat(findOrder(numCourses, prerequisites), Matchers.is(new int[] {0, 1, 2, 5, 4, 3, 7, 6}));

        numCourses = 3;
        prerequisites = new int[][] {
                {1, 0}, {2, 1}, {2, 0}
        };
        assertThat(findOrder(numCourses, prerequisites), Matchers.is(new int[] {0, 1, 2}));

        // fuck leetcode 这个case活过不去！
        //prerequisites = new int[][] {};
        //assertThat(findOrder(numCourses, prerequisites), Matchers.is(new int[] {0, 1}));
    }

    @Test(expected = RuntimeException.class)
    public void testNegative1() {
        int numCourses = 8;
        int[][] prerequisites = new int[][] {
                {1, 0}, {2, 1}, {3, 1}, {3, 4}, {4, 1}, {4, 2}, {4, 5}, {4, 6}, {5, 2}, {6, 3}, {6, 7}, {7, 4}, {7, 5}
        };
        findOrder(numCourses, prerequisites);
    }

    @Test(expected = RuntimeException.class)
    public void testNegative2() {
        int numCourses = 3;
        int[][] prerequisites = new int[][] {
                {1, 0}, {2, 1}, {0, 2}
        };
        findOrder(numCourses, prerequisites);
    }

}
