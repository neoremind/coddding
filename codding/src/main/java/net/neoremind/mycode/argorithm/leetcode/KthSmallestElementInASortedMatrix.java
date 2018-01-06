package net.neoremind.mycode.argorithm.leetcode;

import java.util.PriorityQueue;

/**
 * Given a n x n matrix where each of the rows and columns are sorted in ascending order, find the kth smallest element in the matrix.
 * <p>
 * Note that it is the kth smallest element in the sorted order, not the kth distinct element.
 * <p>
 * Example:
 * <p>
 * matrix = [
 * [ 1,  5,  9],
 * [10, 11, 13],
 * [12, 13, 15]
 * ],
 * k = 8,
 * <p>
 * return 13.
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ n2.
 *
 * @author xu.zhang
 */
public class KthSmallestElementInASortedMatrix {

    public int kthSmallest(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }
        int m = matrix.length;
        int n = matrix[0].length;
        if (n == 0) {
            return 0;
        }
        PriorityQueue<Point> queue = new PriorityQueue<Point>((a, b) -> Integer.compare(a.val, b.val));
        for (int i = 0; i < m; i++) {
            queue.add(new Point(i, 0, matrix[i][0]));
        }
        for (int i = 0; i < k - 1; i++) {
            Point p = queue.poll();
            if (p.y == n - 1) {
                continue;
            }
            queue.add(new Point(p.x, p.y + 1, matrix[p.x][p.y + 1]));
        }
        return queue.peek().val;
    }

    class Point {
        int x;
        int y;
        int val;

        public Point(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }
    }

}
