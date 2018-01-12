package net.neoremind.mycode.argorithm.other;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertThat;

/**
 * @author xu.zhang
 */
public class FindKClosestPoints {

    /**
     * Solution 1： MAX Heap
     * Time: O(nlogk)    Space: O(k)
     */
    public Point[] findKClosestPoints(Point[] points, int k, Point target) {
        if (points == null || points.length == 0 || k < 1 || k > points.length) return points;
        Queue<Point> pq = new PriorityQueue<>(k, (p1, p2) -> {
            int d1 = (p1.x - target.x) * (p1.x - target.x) + (p1.y - target.y) * (p1.y - target.y);
            int d2 = (p2.x - target.x) * (p2.x - target.x) + (p2.y - target.y) * (p2.y - target.y);
            return d2 - d1;
        });
        for (Point p : points) {
            pq.offer(p);
            if (pq.size() > k)
                pq.poll();
        }
        Point[] res = new Point[k];
        for (int i = k - 1; i >= 0; i--)
            res[i] = pq.poll();
        return res;
    }


    public Point[] findKClosestPoints3(Point[] points, int k, Point target) {
        int left = 0;
        int right = points.length - 1;
        Point[] res = new Point[k];
        while (left <= right) {
            int mid = partition(points, left, right, target);
            if (mid == k - 1) {
                break;
            } else if (mid < k - 1) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        for (int i = 0; i < k; i++) {
            res[i] = points[i];
        }
        return res;
    }

    private int partition(Point[] points, int lo, int hi, Point target) {
        int i = lo;
        int j = hi + 1;
        Point pivot = points[i];
        int pDist = getDistance(pivot, target);
        while (true) {
            while (getDistance(points[++i], target) < pDist) {
                if (i == hi) {
                    break;
                }
            }
            while (getDistance(points[--j], target) > pDist) {
                if (j == lo) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            swap(points, i, j);
        }
        swap(points, lo, j);
        return j;
    }

    private int getDistance(Point p, Point target) {
        return (p.x - target.x) * (p.x - target.x) + (p.y - target.y) * (p.y - target.y);
    }

    void swap(Point[] points, int left, int right) {
        Point temp = points[left];
        points[left] = points[right];
        points[right] = temp;
    }

    /**
     * Solution 1： MIN Heap
     * Time: O(nlogk)    Space: O(k)
     * 错误，想清楚，不能用小顶堆！！！！！！！！
     */
    public Point[] findKClosestPoints2(Point[] points, int k, Point target) {
        if (points == null || points.length == 0 || k < 1 || k > points.length) return points;
        Queue<Point> pq = new PriorityQueue<>(k, (p1, p2) -> {
            int d1 = (p1.x - target.x) * (p1.x - target.x) + (p1.y - target.y) * (p1.y - target.y);
            int d2 = (p2.x - target.x) * (p2.x - target.x) + (p2.y - target.y) * (p2.y - target.y);
            return Integer.compare(d1, d2);
        });
        for (Point p : points) {
            pq.offer(p);
            if (pq.size() > k)
                pq.poll();
        }
        Point[] res = new Point[k];
        for (int i = 0; i <= k - 1; i++)
            res[i] = pq.poll();
        return res;
    }

    @Test
    public void test() {
        Point[] points = new Point[4];
        points[0] = new Point(-1, 2);
        points[1] = new Point(4, -3);
        points[2] = new Point(2, 10);
        points[3] = new Point(5, 3);
        System.out.println(Arrays.toString(findKClosestPoints(points, 2, new Point(0, 0))));
        System.out.println(Arrays.toString(findKClosestPoints2(points, 2, new Point(0, 0)))); //wrong!!!!!
        System.out.println(Arrays.toString(findKClosestPoints3(points, 2, new Point(0, 0))));
    }

}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

