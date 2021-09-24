package net.neoremind.mycode.argorithm.leetcode;

/**
 * There are N gas stations along a circular route, where the amount of gas at station i is gas[i].
 * <p>
 * You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from station i to its next station
 * (i+1). You begin the journey with an empty tank at one of the gas stations.
 * <p>
 * Return the starting gas station's index if you can travel around the circuit once, otherwise return -1.
 * <p>
 * https://discuss.leetcode.com/topic/25289/straightforward-java-linear-solution-with-o-1-space-explanation-and-math
 * -proof
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/gas-station/
 */
public class GasStation {

    /**
     * 先检查总加油和总消耗能不能满足跑完一圈。
     * <pre>
     *     15,5    180,20
     *     A <---- E <--------- D 1,2
     *     |                    |
     *     |                    |
     *     |                    |
     *     ------>B ----------->C
     *           5,10         20,100
     * </pre>
     * 从A开始走，可以走到C就不行就，这时候start可以是D，不用再从B返回走了，因为既然从A都能空邮箱走过来，
     * 那么肯定A之后的E,D是有可能的起点，因为要过C，必须要有足够的储备。
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int tank = 0;
        for (int i = 0; i < gas.length; i++) {
            tank += gas[i] - cost[i];
        }
        if (tank < 0) {
            return -1;
        }

        int start = 0;
        int accumulate = 0;
        for (int i = 0; i < gas.length; i++) {
            int curGain = gas[i] - cost[i];
            if (accumulate + curGain < 0) {
                start = i + 1;
                accumulate = 0;
            } else {
                accumulate += curGain;
            }
        }

        return start;
    }

    public int canCompleteCircuit2(int[] gas, int[] cost) {
        int len = gas.length;
        for (int i = 0; i < len; i++) {
            int rest = gas[i] - cost[i];
            int index = (i + 1) % len;
            while (rest > 0 && index != i) {
                rest += gas[index] - cost[index];
                index = (index + 1) % len;
            }
            if (rest >= 0 && index == i) {
                return i;
            }
        }
        return -1;
    }

}
