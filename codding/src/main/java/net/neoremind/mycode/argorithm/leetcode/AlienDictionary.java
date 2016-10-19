package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * There is a new alien language which uses the latin alphabet. However, the order among letters are unknown to you.
 * You receive a list of words from the dictionary, where words are sorted lexicographically by the rules of this new
 * language. Derive the order of letters in this language.
 * <p>
 * For example, Given the following words in dictionary,
 * <p>
 * [
 * "wrt",
 * "wrf",
 * "er",
 * "ett",
 * "rftt"
 * ]
 * The correct order is: "wertf".
 * <p>
 * Note: You may assume all letters are in lowercase. If the order is invalid, return an empty string. There may be
 * multiple valid order of letters, return any one of them is fine.
 * <p>
 * PAID
 * <p>
 * I just realized that it's really the word "lexicographically" that should be pointed out, not the word "words". If
 * it were "words are sorted", it would be like "lists are sorted", and for that you'd probably think the other way,
 * that each list is sorted on its own. It's really the word "lexicographically" that makes the difference, as
 * sorting the letters inside an individual word has nothing to do with lexicographical sorting.
 * <p>
 * 注意这里是字典序，因此每个单词都需要从第一个字符比较，不一样则输出X->Y的一个边，构造两个顶点，然后必须立即break掉。构造一个邻接矩阵的图，然后用BFS，找拓扑排序。
 * <p>
 * https://discuss.leetcode.com/topic/28308/java-ac-solution-using-bfs/2
 *
 * @author zhangxu
 */
public class AlienDictionary {

    class Vertex {
        char ch;
        int indegree;
        ArrayList<Character> neighbour = new ArrayList<>();

        Vertex(char ch) {
            this.ch = ch;
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "ch=" + ch +
                    ", indegree=" + indegree +
                    ", neighbour=" + neighbour +
                    '}';
        }
    }

    public String alienOrder(String[] words) {
        Map<Character, Vertex> map = new HashMap<>();
        //Build the Graph
        for (int i = 0; i < words.length; i++) {
            if (i != words.length - 1) {
                for (int j = 0; j < Math.min(words[i].length(), words[i + 1].length()); j++) {
                    if (words[i].charAt(j) != words[i + 1].charAt(j)) {
                        char start = words[i].charAt(j);
                        char end = words[i + 1].charAt(j);
                        map.putIfAbsent(end, new Vertex(end));
                        map.putIfAbsent(start, new Vertex(start));
                        map.get(end).indegree++;
                        map.get(start).neighbour.add(end);
                        break;
                    }
                }
            }
        }
        map.entrySet().forEach(System.out::println);
        //Topological Sort
        Queue<Vertex> queue = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        int counter = 0;
        for (Map.Entry<Character, Vertex> e : map.entrySet()) {
            if (e.getValue().indegree == 0) {
                sb.append(e.getKey());
                queue.offer(e.getValue());
                counter++;
            }
        }
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            for (Character ch : v.neighbour) {
                if (--map.get(ch).indegree == 0) {
                    queue.offer(map.get(ch));
                    counter++;
                    sb.append(ch);
                }
            }
        }
        if (counter != map.size()) {
            throw new RuntimeException("Cyclic exists!");
        }
        return sb.toString();
    }

    /**
     * <pre>
     *             |---> r---------> t ----------->f
     *  w---->e----
     *
     * </pre>
     * adjacency list如下：
     * <pre>
     * r=Vertex{ch=r, indegree=1, neighbour=[t]}
     * t=Vertex{ch=t, indegree=1, neighbour=[f]}
     * e=Vertex{ch=e, indegree=1, neighbour=[r]}
     * f=Vertex{ch=f, indegree=1, neighbour=[]}
     * w=Vertex{ch=w, indegree=0, neighbour=[e]}
     * </pre>
     * <p>
     * 稍微修改下条件，构造一个环，仍然可以识别
     * <pre>
     *             |---> r---------> t ----------->f
     *  w---->e----                  |
     *       ^                       |
     *       |-----------------------
     *
     * </pre>
     * adjacency list如下：
     * <pre>
     * r=Vertex{ch=r, indegree=1, neighbour=[t]}
     * t=Vertex{ch=t, indegree=2, neighbour=[f, e]}
     * e=Vertex{ch=e, indegree=1, neighbour=[r]}
     * f=Vertex{ch=f, indegree=1, neighbour=[]}
     * w=Vertex{ch=w, indegree=0, neighbour=[t]}
     * </pre>
     */
    @Test
    public void test() {
        String[] words = new String[] {"wrt", "wrf", "er", "ett", "rftt"};
        assertThat(alienOrder(words), Matchers.is("wertf"));

        words = new String[] {"wrt", "wrf", "tf", "er", "ett", "rftt"};
        assertThat(alienOrder(words), Matchers.is("wertf"));
    }

}
