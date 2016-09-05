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
public class WordLadder_Dijkstra {

    @Test
    public void test() {
        Set<String> wordList = Sets.newHashSet("hot", "dot", "dog", "lot", "log");
        assertThat(ladderLength("hit", "cog", wordList), is(5));
        assertThat(ladderLength("hyy", "cog", wordList), is(0));
        assertThat(ladderLength("hit", "cxx", wordList), is(0));

        wordList = Sets.newHashSet("hot");
        assertThat(ladderLength("hot", "hot", wordList), is(0));

        wordList = Sets.newHashSet("hot", "ted");
        assertThat(ladderLength("hot", "ted", wordList), is(0));

        wordList = Sets.newHashSet("hot", "dot");
        assertThat(ladderLength("hot", "dot", wordList), is(2));

        long start = System.currentTimeMillis();
        wordList =
                Sets.newHashSet("dose", "ends", "dine", "jars", "prow", "soap", "guns", "hops", "cray", "hove", "ella",
                        "hour", "lens", "jive", "wiry", "earl", "mara", "part", "flue", "putt", "rory", "bull", "york",
                        "ruts", "lily", "vamp", "bask", "peer", "boat", "dens", "lyre", "jets", "wide", "rile", "boos",
                        "down", "path", "onyx", "mows", "toke", "soto", "dork", "nape", "mans", "loin", "jots", "male",
                        "sits", "minn", "sale", "pets", "hugo", "woke", "suds", "rugs", "vole", "warp", "mite", "pews",
                        "lips", "pals", "nigh", "sulk", "vice", "clod", "iowa", "gibe", "shad", "carl", "huns", "coot",
                        "sera", "mils", "rose", "orly", "ford", "void", "time", "eloy", "risk", "veep", "reps", "dolt",
                        "hens", "tray", "melt", "rung", "rich", "saga", "lust", "yews", "rode", "many", "cods", "rape",
                        "last", "tile", "nosy", "take", "nope", "toni", "bank", "jock", "jody", "diss", "nips", "bake",
                        "lima", "wore", "kins", "cult", "hart", "wuss", "tale", "sing", "lake", "bogy", "wigs", "kari",
                        "magi", "bass", "pent", "tost", "fops", "bags", "duns", "will", "tart", "drug", "gale", "mold",
                        "disk", "spay", "hows", "naps", "puss", "gina", "kara", "zorn", "boll", "cams", "boas", "rave",
                        "sets", "lego", "hays", "judy", "chap", "live", "bahs", "ohio", "nibs", "cuts", "pups", "data",
                        "kate", "rump", "hews", "mary", "stow", "fang", "bolt", "rues", "mesh", "mice", "rise", "rant",
                        "dune", "jell", "laws", "jove", "bode", "sung", "nils", "vila", "mode", "hued", "cell", "fies",
                        "swat", "wags", "nate", "wist", "honk", "goth", "told", "oise", "wail", "tels", "sore", "hunk",
                        "mate", "luke", "tore", "bond", "bast", "vows", "ripe", "fond", "benz", "firs", "zeds", "wary",
                        "baas", "wins", "pair", "tags", "cost", "woes", "buns", "lend", "bops", "code", "eddy", "siva",
                        "oops", "toed", "bale", "hutu", "jolt", "rife", "darn", "tape", "bold", "cope", "cake", "wisp",
                        "vats", "wave", "hems", "bill", "cord", "pert", "type", "kroc", "ucla", "albs", "yoko", "silt",
                        "pock", "drub", "puny", "fads", "mull", "pray", "mole", "talc", "east", "slay", "jamb", "mill",
                        "dung", "jack", "lynx", "nome", "leos", "lade", "sana", "tike", "cali", "toge", "pled", "mile",
                        "mass", "leon", "sloe", "lube", "kans", "cory", "burs", "race", "toss", "mild", "tops", "maze",
                        "city", "sadr", "bays", "poet", "volt", "laze", "gold", "zuni", "shea", "gags", "fist", "ping",
                        "pope", "cora", "yaks", "cosy", "foci", "plan", "colo", "hume", "yowl", "craw", "pied", "toga",
                        "lobs", "love", "lode", "duds", "bled", "juts", "gabs", "fink", "rock", "pant", "wipe", "pele",
                        "suez", "nina", "ring", "okra", "warm", "lyle", "gape", "bead", "lead", "jane", "oink", "ware",
                        "zibo", "inns", "mope", "hang", "made", "fobs", "gamy", "fort", "peak", "gill", "dino", "dina",
                        "tier");
        assertThat(ladderLength("nape", "mild", wordList), is(6));
        System.out.println((System.currentTimeMillis() - start));
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
                    neighborVertex.dist = vertex.dist + 1;
                    neighborVertex.prev = vertex;
                    queue.add(neighborVertex);
                }
            }
        }

        LinkedList<String> path = new LinkedList<>();
        if (wordAdjacencyList.get(endWord).prev == null) {
            return 0;
        }
        findPath(wordAdjacencyList.get(endWord), path);
        System.out.println(path);
        return path.size();
    }

    private void findPath(Vertex vertex, LinkedList<String> path) {
        path.addFirst(vertex.word);
        if (vertex.prev == null) {
            return;
        }
        findPath(vertex.prev, path);
    }

    private Map<String, Vertex> buildWordAdjacencyList(Set<String> wordList) {
        Map<String, Vertex> vertexMap = new HashMap<>(wordList.size());
        for (String word : wordList) {
            vertexMap.put(word, new Vertex(word));
        }
        for (Map.Entry<String, Vertex> word2VertexEntry : vertexMap.entrySet()) {
            String word = word2VertexEntry.getKey();
            Vertex vertex = word2VertexEntry.getValue();
            char[] chs = word.toCharArray();

            for (int i = 0; i < word.length(); i++) {
                char old = chs[i];
                for (char j = 'a'; j < 'z'; j++) {
                    chs[i] = j;
                    String newWord = String.valueOf(chs);
                    if (wordList.contains(newWord) && !newWord.equals(word)) {
                        vertex.neighbors.add(newWord);
                    }
                    chs[i] = old;
                }
            }
        }
        return vertexMap;
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
