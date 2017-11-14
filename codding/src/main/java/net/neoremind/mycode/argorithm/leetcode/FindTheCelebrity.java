package net.neoremind.mycode.argorithm.leetcode;

/**
 * Suppose you are at a party with n people (labeled from 0 to n - 1) and among them, there may exist one celebrity.
 * The definition of a celebrity is that all the other n - 1people know him/her but he/she does not know any of them.
 * <p>
 * Now you want to find out who the celebrity is or verify that there is not one. The only thing you are allowed to
 * do is to ask questions like: "Hi, A. Do you know B?" to get information of whether A knows B. You need to find out
 * the celebrity (or verify there is not one) by asking as few questions as possible (in the asymptotic sense).
 * <p>
 * You are given a helper function bool knows(a, b) which tells you whether A knows B. Implement a function int
 * findCelebrity(n), your function should minimize the number of calls to knows.
 * <p>
 * Note: There will be exactly one celebrity if he/she is in the party. Return the celebrity's label if there is a
 * celebrity in the party. If there is no celebrity, return -1.
 *
 * @author xu.zhang
 */
public class FindTheCelebrity {

    int findCelebrity(int n) {
        if (n <= 1) {
            return n;
        }
        int cel = 0;
        for (int i = 1; i < n; i++) {
            if (!knows(i, cel)) {
                cel = i;
            }
        }

        for (int i = 0; i < n; i++) {
            if (i == cel) {
                continue;
            } else {
                if (!knows(i, cel) || knows(cel, i)) {
                    return -1;
                }
            }
        }
        return cel;
    }

    boolean knows(int i, int cand) {
        return true;
    }
}
