package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * You are playing the following Flip Game with your friend: Given a string that contains only these two characters:
 * + and -, you and your friend take turns to flip two consecutive "++" into "--". The game ends when a person can no
 * longer make a move and therefore the other person will be the winner.
 * <p>
 * Write a function to compute all possible states of the string after one valid move.
 * <p>
 * For example, given s = "++++", after one move, it may become one of the following states:
 * <p>
 * [
 * "--++",
 * "+--+",
 * "++--"
 * ]
 * If there is no valid move, return an empty list [].
 * <p>
 * 若是连着的"++", 就把这段用"--"替代放到res中.
 * <p>
 * Time Complexity: O(n). Space: O(1) regardless res.
 *
 * @author xu.zhang
 */
public class FlipGame {

    public List<String> generatePossibleNextMoves(String s) {
        List<String> res = new ArrayList<String>();
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '+' && s.charAt(i - 1) == '+') {
                res.add(s.substring(0, i - 1) + "--" + s.substring(i + 1));
            }
        }
        return res;
    }
}
