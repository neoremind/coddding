package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest transformation
 * sequence from beginWord to endWord, such that:
 * <p>
 * Only one letter can be changed at a time
 * Each intermediate word must exist in the word list
 * For example,
 * <p>
 * Given:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
 * return its length 5.
 * <p>
 * Note:
 * 这个类是第一次解题时候完成的，完全套用了{@link net.neoremind.mycode.argorithm.graph.Dijkstra}算法，只不过权为1，
 * 这个死板的做法却歪打正着了，找到了非字典序的最短的路径。例如上面的例子中的解变为了
 * "hit" -> "lot" -> "log" -> "cog"，length为4.
 *
 * @author zhangxu
 * @see WordLadder
 */
public class WordLadderShortestPathPossible {

    @Test
    public void test() {
        Set<String> wordList = Sets.newHashSet("hot", "dot", "dog", "lot", "log");
        assertThat(ladderLength("hit", "cog", wordList), is(4));
        assertThat(ladderLength("hyy", "cog", wordList), is(0));
        assertThat(ladderLength("hit", "cxx", wordList), is(0));

        wordList = Sets.newHashSet("hot");
        assertThat(ladderLength("hot", "hot", wordList), is(0));

        wordList = Sets.newHashSet("hot", "ted");
        assertThat(ladderLength("hot", "ted", wordList), is(0));

        wordList = Sets.newHashSet("hot", "dot");
        assertThat(ladderLength("hot", "dot", wordList), is(1));
    }

    public int ladderLength(String beginWord, String endWord, Set<String> wordList) {
        wordList.add(beginWord);
        wordList.add(endWord);

        Map<String, Vertex> wordAdjacencyList = buildWordAdjacencyList(wordList);
        System.out.println(wordAdjacencyList);
        wordAdjacencyList.get(beginWord).dist = 0;

        Queue<Vertex> queue = new LinkedList<>();
        Vertex beginVertex = wordAdjacencyList.get(beginWord);
        queue.add(beginVertex);

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            if (vertex.dist == Integer.MAX_VALUE) {
                break;
            }

            for (String neighbor : vertex.neighbors) {
                int newDist = vertex.dist + 1;
                Vertex neighborVertex = wordAdjacencyList.get(neighbor);
                if (neighborVertex.dist > newDist) {
                    queue.remove(neighborVertex);
                    neighborVertex.dist = vertex.dist + 1;
                    neighborVertex.prev = vertex;
                    queue.add(neighborVertex);
                }
            }
        }

        LinkedList<String> path = new LinkedList<>();
        findPath(wordAdjacencyList.get(endWord), path);
        System.out.println(path);
        return path.size();
    }

    private void findPath(Vertex vertex, LinkedList<String> path) {
        if (vertex.prev == null) {
            return;
        }
        path.addFirst(vertex.word);
        findPath(vertex.prev, path);
    }

    private Map<String, Vertex> buildWordAdjacencyList(Set<String> wordList) {
        Map<String, Vertex> vertexMap = new HashMap<>();
        for (String word : wordList) {
            vertexMap.put(word, new Vertex(word));
        }
        for (Map.Entry<String, Vertex> word2VertexEntry : vertexMap.entrySet()) {
            String word = word2VertexEntry.getKey();
            Vertex vertex = word2VertexEntry.getValue();
            Iterator<String> iter = wordList.iterator();
            while (iter.hasNext()) {
                String s = iter.next();
                if (word.equals(s)) {
                    continue;
                }
                if (canTransform(word, s)) {
                    vertex.neighbors.add(s);
                }
            }
        }
        return vertexMap;
    }

    private boolean canTransform(String fromWord, String toWord) {
        int len = fromWord.length();
        int diffCount = 0;
        for (int i = 0; i < len; i++) {
            if (fromWord.charAt(i) != toWord.charAt(i)) {
                if (diffCount++ > 1) {
                    return false;
                }
            }
        }
        return diffCount == 1;
    }

    class Vertex {
        String word;
        List<String> neighbors = new ArrayList<>();
        Vertex prev;
        int dist = Integer.MAX_VALUE;

        public Vertex(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return word + "|" + neighbors;
        }
    }

}
