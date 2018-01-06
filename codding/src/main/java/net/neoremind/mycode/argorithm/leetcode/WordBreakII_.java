package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xu.zhang
 */
public class WordBreakII_ {

    public List<String> wordBreak(String s, List<String> wordDict) {
        List<String> res = new ArrayList<>();
        boolean[] dp = new boolean[s.length() + 1];
        MultiStep[] steps = new MultiStep[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    if (steps[i] == null) {
                        steps[i] = new MultiStep();
                    }
                    steps[i].list.add(new Step(j, s.substring(j, i)));
                    // 没有break了！！！
                }
            }
        }
        if (!dp[s.length()]) {
            return res;
        }
        helper(steps[s.length()], res, new ArrayList<>(), steps);
        return res;
    }

    void helper(MultiStep ms, List<String> res, List<String> temp, MultiStep[] steps) {
        if (ms == null) {
            List<String> copy = new ArrayList<>(temp);
            Collections.reverse(copy);
            res.add(copy.stream().collect(Collectors.joining(" ")));
            return;
        }
        for (int i = 0; i < ms.list.size(); i++) {
            Step step = ms.list.get(i);
            temp.add(step.s);
            helper(steps[step.prev], res, temp, steps);
            temp.remove(temp.size() - 1);
        }
    }

    class MultiStep {
        List<Step> list = new ArrayList<>();
    }

    class Step {
        int prev;
        String s;

        Step(int prev, String s) {
            this.prev = prev;
            this.s = s;
        }
    }

}
