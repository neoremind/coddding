package net.neoremind.mycode.argorithm.leetcode;

import java.util.*;

/**
 * https://leetcode.com/problems/network-delay-time/
 *
 * @author xu.zx
 */
public class NetworkDelayTime {

    public int networkDelayTime(int[][] times, int n, int k) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        Map<Integer, List<int[]>> map = new HashMap<>();
        for (int[] edge: times) {
            int from = edge[0];
            int to = edge[1];
            int w = edge[2];
            if (!map.containsKey(from)) {
                map.put(from, new ArrayList<>());
            }
            map.get(from).add(new int[]{to, w});
        }
        dist[k] = 0;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{k, 0});
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int[] point = q.poll();
                int prevDist = point[1];
                List<int[]> out = map.get(point[0]);
                if (out != null) {
                    for (int[] neighbor: out) {
                        if (prevDist + neighbor[1] < dist[neighbor[0]]) {
                            dist[neighbor[0]] = prevDist + neighbor[1];
                            q.add(new int[]{neighbor[0], dist[neighbor[0]]});
                        }
                    }
                }
            }
        }

        int max = 0;
        for (int val: dist) {
            if (val == Integer.MAX_VALUE) {
                return -1;
            }
            max = Math.max(max, val);
        }
        return max;
    }
}
