package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * https://leetcode.com/problems/course-schedule/
 * <p>
 * There are a total of n courses you have to take, labeled from 0 to n - 1.
 * <p>
 * Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is
 * expressed as a pair: [0,1]
 * <p>
 * Given the total number of courses and a list of prerequisite pairs, is it possible for you to finish all courses?
 * <p>
 * For example:
 * <p>
 * 2, [[1,0]]
 * There are a total of 2 courses to take. To take course 1 you should have finished course 0. So it is possible.
 * <p>
 * 2, [[1,0],[0,1]]
 * There are a total of 2 courses to take. To take course 1 you should have finished course 0, and to take course 0
 * you should also have finished course 1. So it is impossible.
 * <p>
 * Note:
 * The input prerequisites is a graph represented by a list of edges, not adjacency matrices. Read more about how a
 * graph is represented.
 * <p>
 * click to show more hints.
 * <p>
 * Hints:
 * This problem is equivalent to finding if a cycle exists in a directed graph. If a cycle exists, no topological
 * ordering exists and therefore it will be impossible to take all courses.
 * Topological Sort via DFS - A great video tutorial (21 minutes) on Coursera explaining the basic concepts of
 * Topological Sort.
 * Topological sort could also be done via BFS.
 * <p>
 * 三种表示图的方法：https://www.khanacademy.org/computing/computer-science/algorithms/graph-representation/a/representing-graphs
 * <ul>
 * <li>1）Edge lists</li>
 * <li>2）Adjacency matrices 邻接矩阵</li>
 * <li>3）Adjacency lists 邻接表</li>
 * </ul>
 * 本题使用的是第一种Edge lists。
 * <p>
 * 这道题的解法和有向图中找环是类似的。如果有环，则不可能完成拓扑排序（topological sort）。
 *
 * @author zhangxu
 */
public class CourseSchedule {

    /**
     * 顶点
     */
    static class Vertex {

        /**
         * 课程号
         */
        int id;

        Vertex(int id) {
            this.id = id;
        }

        /**
         * 所谓入的顶点
         */
        Set<Integer> in = new HashSet<>();

        /**
         * 所谓出的顶点
         */
        Set<Integer> out = new HashSet<>();

        /**
         * 是否为入度 in degree=0
         *
         * @return
         */
        boolean isSink() {
            return in.isEmpty();
        }
    }

    Vertex fillAdjacencyList(Vertex[] G, int id) {
        if (G[id] == null) {
            G[id] = new Vertex(id);
        }

        return G[id];
    }

    /**
     * 解法1。使用一种非标准的邻接表，比较耗时，性能在leetcode排名最后
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Vertex[] G = new Vertex[numCourses];

        for (int[] p : prerequisites) {
            fillAdjacencyList(G, p[0]).in.add(p[1]);  //先完成p[1]，才可以学p[0]，因此p[1]是起点，p[0]是终点
            fillAdjacencyList(G, p[1]).out.add(p[0]);
        }

        Set<Vertex> S = Arrays.stream(G)
                .filter(v -> v != null)
                .collect(Collectors.toSet());

        /**  时间复杂度O(V)，每次都遍历集合S，去发现入度为0的顶点，移除它，和他邻接的顶点的in都要去掉它，
         * 一旦不能再发现入度为0的顶点了，说明存在环，退出即可 */
        loop:
        while (!S.isEmpty()) {
            for (Vertex v : S) {
                if (v.isSink()) {
                    S.remove(v);
                    for (int i : v.out) {
                        G[i].in.remove(v.id);
                    }
                    continue loop;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 解法2。标准教科书般的邻接表，广度优先搜索解法。
     * <p>
     * BFS 广度优先搜索
     * <p>
     * https://discuss.leetcode.com/topic/15762/java-dfs-and-bfs-solution
     */
    public boolean canFinishBFS(int numCourses, int[][] prerequisites) {
        // 每门课的依赖
        ArrayList[] graph = new ArrayList[numCourses];

        // 所有课程的度
        int[] degree = new int[numCourses];

        Queue queue = new LinkedList();
        int count = 0;

        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList();
        }

        // 第一步，总之就是先初始化邻接表
        for (int i = 0; i < prerequisites.length; i++) {
            degree[prerequisites[i][0]]++;
            graph[prerequisites[i][1]].add(prerequisites[i][0]);
        }

        int counter = 0;
        for (ArrayList arrayList : graph) {
            System.out.println(counter + "=" + arrayList);
            counter++;
        }
        System.out.println(Arrays.toString(degree));

        // 第二步，找度0的，放入队列
        for (int i = 0; i < degree.length; i++) {
            if (degree[i] == 0) {
                queue.add(i);
                count++;
            }
        }

        // 第三步，不断的出队列，把所有邻接的兄弟的，也就是依赖于自己的全部度-1，如果发现度=0的，则入队列，否则就有环了
        while (queue.size() != 0) {
            int course = (int) queue.poll();
            for (int i = 0; i < graph[course].size(); i++) {
                int pointer = (int) graph[course].get(i);
                degree[pointer]--;
                if (degree[pointer] == 0) {
                    queue.add(pointer);
                    count++;
                }
            }
        }
        if (count == numCourses) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解法3。不断探测，理论上，应该会收殓到所有的终点，如果发现探测过程中有已经访问过的，则证明有了环
     * <p>
     * DFS 深度优先搜索
     * <p>
     * https://discuss.leetcode.com/topic/15762/java-dfs-and-bfs-solution
     */
    public boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        ArrayList[] graph = new ArrayList[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList();
        }

        boolean[] visited = new boolean[numCourses];
        for (int i = 0; i < prerequisites.length; i++) {
            graph[prerequisites[i][1]].add(prerequisites[i][0]);
        }

        for (int i = 0; i < numCourses; i++) {
            if (!dfs(graph, visited, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(ArrayList[] graph, boolean[] visited, int course) {
        if (visited[course]) {
            return false;
        }

        visited[course] = true;
        for (int i = 0; i < graph[course].size(); i++) {
            if (!dfs(graph, visited, (int) graph[course].get(i))) {
                return false;
            }
        }
        visited[course] = false;
        return true;
    }

    /**
     * 第二个测试case：
     * <pre>
     *       ------> 2 ----------
     *      |        |          |
     *      |        \/         \/
     * 1 ---         5 -------> 4 -------> 3 ------> 6
     *      |                  /\ |       /\        /|\
     *      |                   | -------------------
     *      ---------------------         |
     *      |_____________________________|
     * </pre>
     * <p>
     * visit - 1, queue = 2
     * <pre>
     *               2 ----------
     *               |          |
     *               \/         \/
     *               5 -------> 4 -------> 3 ------> 6
     *                          |                  /|\
     *                           -------------------
     * </pre>
     * visit - 2, queue = 5
     * <pre>
     *
     *               5 -------> 4 -------> 3 ------> 6
     *                          |                  /|\
     *                           -------------------
     * </pre>
     * visit - 5, queue = 4
     * <pre>
     *
     *                          4 -------> 3 ------> 6
     *                          |                  /|\
     *                           -------------------
     * </pre>
     * visit - 4, queue = 3
     * <pre>
     *
     *                           3 ------> 6
     * </pre>
     * visit - 3, queue = 6
     * <pre>
     *
     *                           6
     * </pre>
     * visit - 6, queue = null
     */
    @Test
    public void test() {
        int numCourses = 8;
        int[][] prerequisites = new int[][]{
                {2, 1}, {3, 1}, {3, 4}, {4, 1}, {4, 2}, {4, 5}, {5, 2}, {6, 3}, {6, 4}, {6, 7}, {7, 4}, {7, 5}
        };
        assertThat(canFinish(numCourses, prerequisites), Matchers.is(true));
        assertThat(canFinishBFS(numCourses, prerequisites), Matchers.is(true));
        assertThat(canFinishDFS(numCourses, prerequisites), Matchers.is(true));

        numCourses = 7;
        prerequisites = new int[][]{
                {2, 1}, {3, 1}, {3, 4}, {4, 1}, {4, 2}, {4, 5}, {5, 2}, {6, 3}, {6, 4}
        };
        assertThat(canFinish(numCourses, prerequisites), Matchers.is(true));
        assertThat(canFinishBFS(numCourses, prerequisites), Matchers.is(true));
        assertThat(canFinishDFS(numCourses, prerequisites), Matchers.is(true));
    }

}
