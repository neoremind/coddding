package net.neoremind.mycode.argorithm.leetcode;

/**
 * 给了一个现成的api rand7()，这个接口能产生 [1,7] 区间的随机数。根据这个api，写一个 rand10() 的算法生成 [1, 10] 区间随机数。
 * <p>
 * 题解：这个题《程序员代码面试指南》上讲了这题。我粗浅的描述一下产生过程：
 * <p>
 * （1）rand7() 等概率的产生 1，2， 3， 4， 5， 6，7.
 * <p>
 * （2）rand7()-1 等概率的产生 [0, 6]
 * <p>
 * （3）(rand7() - 1) * 7 等概率的产生 0, 7, 14, 21, 28, 35, 42
 * <p>
 * （4）(rand7() - 1) * 7 + (rand7() - 1)等概率的产生 [0, 48] 这49个数字
 * <p>
 * （5）如果步骤4的结果大于等于40，那么就重复步骤4，直到产生的数小于40.
 * <p>
 * （6）把步骤5的结果mod 10再加1，就会等概率的随机生成[1, 10].
 *
 * @author xu.zx
 */
public class ImplementRand10UsingRand7 {

  private int rand7() {return 0;}

  public int rand10() {
    int num;
    do {
      num = (rand7() - 1) * 7 + (rand7() - 1);
    } while (num >= 40);
    return num % 10 + 1;
  }
}
