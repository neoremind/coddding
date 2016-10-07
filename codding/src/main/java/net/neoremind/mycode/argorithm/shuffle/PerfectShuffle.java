package net.neoremind.mycode.argorithm.shuffle;

import java.util.Arrays;

import org.junit.Test;

import net.neoremind.mycode.argorithm.sort.ArrayHelper;

/**
 * 完美洗牌问题
 * <p>
 * 有个长度为2n的数组{a1,a2,a3,...,an,b1,b2,b3,...,bn}，希望排序后{a1,b1,a2,b2,....,an,bn}，请考虑有无时间复杂度o(n)，空间复杂度0(1)的解法。
 * <p>
 * https://github.com/julycoding/The-Art-Of-Programming-By-July/blob/master/ebook/zh/02.09.md
 *
 * @author zhangxu
 */
public class PerfectShuffle {

    /**
     * 解法一、蛮力变换
     * <p>
     * 题目要我们怎么变换，咱们就怎么变换。此题@陈利人也分析过，在此，引用他的思路进行说明。为了便于分析，我们取n=4，那么题目要求我们把
     * <p>
     * a1，a2，a3，a4，b1，b2，b3，b4
     * <p>
     * 变成
     * <p>
     * a1，b1，a2，b2，a3，b3，a4，b4
     * <p>
     * 1.1、步步前移
     * <p>
     * 仔细观察变换前后两个序列的特点，我们可做如下一系列操作：
     * <p>
     * 第①步、确定b1的位置，即让b1跟它前面的a2，a3，a4交换：
     * <p>
     * a1，b1，a2，a3，a4，b2，b3，b4
     * <p>
     * 第②步、接着确定b2的位置，即让b2跟它前面的a3，a4交换：
     * <p>
     * a1，b1，a2，b2，a3，a4，b3，b4
     * <p>
     * 第③步、b3跟它前面的a4交换位置：
     * <p>
     * a1，b1，a2，b2，a3，b3，a4，b4
     * <p>
     * b4已在最后的位置，不需要再交换。如此，经过上述3个步骤后，得到我们最后想要的序列。但此方法的时间复杂度为O（N^2），我们得继续寻找其它方法，看看有无办法能达到题目所预期的O（N）的时间复杂度。
     */
    public void shuffleBruteForce1(int[] nums) {

    }

    /**
     * 1.2、中间交换
     * <p>
     * 当然，除了如上面所述的让b1，b2，b3，b4步步前移跟它们各自前面的元素进行交换外，我们还可以每次让序列中最中间的元素进行交换达到目的。还是用上面的例子，针对a1，a2，a3，a4，b1，b2，b3，b4
     * <p>
     * 第①步：交换最中间的两个元素a4，b1，序列变成（待交换的元素用粗体表示）：
     * <p>
     * a1，a2，a3，b1，a4，b2，b3，b4
     * <p>
     * 第②步，让最中间的两对元素各自交换：
     * <p>
     * a1，a2，b1，a3，b2，a4，b3，b4
     * <p>
     * 第③步，交换最中间的三对元素，序列变成：
     * <p>
     * a1，b1，a2，b2，a3，b3，a4，b4
     * <p>
     * 同样，此法同解法1.1、步步前移一样，时间复杂度依然为O（N^2），我们得下点力气了。
     */
    public void shuffleBruteForce2(int[] nums) {

    }

    /**
     * 如果这副牌用a1 a2 a3 a4 b1 b2 b3 b4表示（为简化问题，假设这副牌只有8张牌），然后一分为二之后，左手上的牌可能是a1 a2 a3 a4，右手上的牌是b1 b2 b3
     * b4，那么在如上图那样的洗牌之后，得到的牌就可能是b1 a1 b2 a2 b3 a3 b4 a4。
     * <p>
     * 技术来源于生活，2004年，microsoft的Peiyush Jain在他发表一篇名为：“A Simple In-Place Algorithm for In-Shuffle”的论文中提出了完美洗牌算法。
     * <p>
     * 这个算法解决一个什么问题呢？跟本题有什么联系呢？
     * <p>
     * Yeah，顾名思义，完美洗牌算法解决的就是一个完美洗牌问题。什么是完美洗牌问题呢？即给定一个数组a1,a2,a3,...an,b1,b2,b3..bn,最终把它置换成b1,a1,b2,a2,...bn,
     * an。读者可以看到，这个完美洗牌问题本质上与本题完全一致，只要在完美洗牌问题的基础上对它最后的序列swap两两相邻元素即可。
     * <p>
     * 即：
     * <p>
     * a1,a2,a3,...an,b1,b2,b3..bn
     * 通过完美洗牌问题，得到：
     * <p>
     * b1,a1,b2,a2,b3,a3...  bn,an
     * 再让上面相邻的元素两两swap，即可达到本题的要求：
     * <p>
     * a1,b1,a2,b2,a3,b3....,an,bn
     * 也就是说，如果我们能通过完美洗牌算法（时间复杂度O(N)，空间复杂度O(1)）解决了完美洗牌问题，也就间接解决了本题。
     * <p>
     * 虽然网上已有不少文章对上篇论文或翻译或做解释说明，但对于初学者来说，理解难度实在太大，再者，若直接翻译原文，根本无法看出这个算法怎么一步步得来的，故下文将从完美洗牌算法的最基本的原型开始说起，以让读者能对此算法一目了然。
     * <p>
     * 2.1、位置置换pefect_shuffle1算法
     * <p>
     * 为方便讨论，我们设定数组的下标从1开始，下标范围是[1..2n]。 还是通过之前n=4的例子，来看下每个元素最终去了什么地方。
     * <p>
     * 起始序列：a1 a2 a3 a4 b1 b2 b3 b4 数组下标：1 2 3 4 5 6 7 8 最终序列：b1 a1 b2 a2 b3 a3 b4 a4
     * <p>
     * 从上面的例子我们能看到，前n个元素中，
     * <p>
     * 第1个元素a1到了原第2个元素a2的位置，即1->2；
     * <p>
     * 第2个元素a2到了原第4个元素a4的位置，即2->4；
     * <p>
     * 第3个元素a3到了原第6个元素b2的位置，即3->6；
     * <p>
     * 第4个元素a4到了原第8个元素b4的位置，即4->8；
     * 那么推广到一般情况即是：前n个元素中，第i个元素去了 第（2 * i）的位置。
     * <p>
     * 上面是针对前n个元素，那么针对后n个元素，可以看出：
     * <p>
     * 第5个元素b1到了原第1个元素a1的位置，即5->1；
     * <p>
     * 第6个元素b2到了原第3个元素a3的位置，即6->3；
     * <p>
     * 第7个元素b3到了原第5个元素b1的位置，即7->5；
     * <p>
     * 第8个元素b4到了原第7个元素b3的位置，即8->7；
     * 推广到一般情况是，后n个元素，第i个元素去了第 (2 * (i - n) ) - 1 = 2 * i - (2 * n + 1) = (2 * i) % (2 * n + 1) 个位置。
     * <p>
     * 再综合到任意情况，任意的第i个元素，我们最终换到了 (2 * i) % (2 * n + 1)的位置。为何呢？因为：
     * <p>
     * 当0 < i < n时， 原式= (2i) % (2 * n + 1) = 2i；
     * <p>
     * 当i > n时，原式(2 * i) % (2 * n + 1)保持不变。
     * 因此，如果题目允许我们再用一个数组的话，我们直接把每个元素放到该放得位置就好了。也就产生了最简单的方法pefect_shuffle1，参考代码如下：
     * <p>
     * 时间O(n)，空间O(n) 数组下标从1开始
     *
     * 另外还有一个走圈的算法可以做到O(n)和O(1) //TODO
     */
    public void shuffle(int[] nums) {
        //TODO no need?
        if (nums.length % 2 != 0) {
            throw new IllegalArgumentException("Odd length expected");
        }
        int[] temp = new int[nums.length + 1];
        for (int i = 1; i <= nums.length; i++) {
            temp[(i * 2) % (nums.length + 1)] = nums[i - 1];
        }
        for (int i = 1; i <= nums.length; i++) {
            nums[i - 1] = temp[i];
        }
    }

    @Test
    public void test() {
        int[] nums = ArrayHelper.getShuffledArray(10);
        System.out.println(Arrays.toString(nums));
        shuffle(nums);
        System.out.println(Arrays.toString(nums));
    }

}
