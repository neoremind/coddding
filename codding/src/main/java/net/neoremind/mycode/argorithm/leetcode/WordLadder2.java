package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
 * Return 0 if there is no such transformation sequence.
 * All words have the same length.
 * All words contain only lowercase alphabetic characters.
 *
 * @author zhangxu
 * @see <a href="https://leetcode.com/problems/word-ladder/">word-ladder</a>
 */
// may have problem?
public class WordLadder2 {

    @Test
    public void test() {
        List<String> wordList = Lists.newArrayList("hot", "dot", "dog", "lot", "log");
        assertThat(ladderLength("hit", "cog", wordList), is(5));
        assertThat(ladderLength("hyy", "cog", wordList), is(0));
        assertThat(ladderLength("hit", "cxx", wordList), is(0));

        wordList = Lists.newArrayList("hot");
        assertThat(ladderLength("hot", "hot", wordList), is(0));

        wordList = Lists.newArrayList("hot", "ted");
        assertThat(ladderLength("hot", "ted", wordList), is(0));

        wordList = Lists.newArrayList("hot", "dot");
        assertThat(ladderLength("hot", "dot", wordList), is(2));

        long start = System.currentTimeMillis();
        wordList =
                Lists.newArrayList("dose", "ends", "dine", "jars", "prow", "soap", "guns", "hops", "cray", "hove", "ella",
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


    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> words = new HashSet<>(wordList);
        Queue<String> q = new LinkedList<>();
        q.add(beginWord);
        int len = 0;
        while (!q.isEmpty()) {
            len++;
            int size = q.size();
            for (int k = 0; k < size; k++) {
                String s = q.poll();
                if (s.equals(endWord)) {
                    return len;
                }

                char[] l = s.toCharArray();
                for (int i = 0; i < s.length(); i++) {
                    char o = l[i];
                    for (int j = 0; j < 26; j++) {
                        l[i] = (char) ('a' + j);
                        if (words.contains(new String(l))) {
                            words.remove(new String(l));
                            q.add(new String(l));
                        }
                    }
                    l[i] = o;
                }
            }
        }
        return 0;
    }

}
