package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * You are playing the following Flip Game with your friend: Given a string that contains only these two characters:
 * + and -, you and your friend take turns to flip two consecutive "++" into "--". The game ends when a person can no
 * longer make a move and therefore the other person will be the winner.
 * <p>
 * Write a function to determine if the starting player can guarantee a win.
 * <p>
 * For example, given s = "++++", return true. The starting player can guarantee a win by flipping the middle "++" to
 * become "+--+".
 * <p>
 * Follow up:
 * Derive your algorithm's runtime complexity.
 * <p>
 * T(N) = (N-2) * T(N-2) = (N-2) * (N-4) * T(N-4) ... = (N-2) * (N-4) * (N-6) * ... ~ O(N!!)
 *
 * @author xu.zhang
 */
//TODO memoization to improve
public class FlipGameII {

    public boolean canWin(String s) {
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '+' && s.charAt(i - 1) == '+' &&
                    !canWin(s.substring(0, i - 1) + "--" + s.substring(i + 1))) {
                return true;
            }
        }
        return false;
    }
}
