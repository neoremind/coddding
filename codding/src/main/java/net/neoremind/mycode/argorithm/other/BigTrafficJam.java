package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangxu
 */
public class BigTrafficJam {

    /**
     * 打印列表的时间间隔，单位毫秒，默认为不停顿
     */
    private int printOutIntervalMs = 0;

    /**
     * 是否打印debug中间状态信息，默认为true
     */
    public boolean debug = true;

    /**
     * 列表长度，条件为:
     * <ul>
     * <li>1) 奇数</li>
     * <li>2) 区间在[3, infinite)</li>
     * </ul>
     */
    private static int LENGTH;

    /**
     * 最低索引
     */
    private static int LOW_IDX;

    /**
     * 最高索引
     */
    private static int HIGH_IDX;

    /**
     * 中间索引，初始就是空座位
     */
    private static int MID_IDX;

    /**
     * 随着算法执行而变化的A组头A1的索引
     */
    private static int START_IDX_OF_A;

    /**
     * 随着算法执行而变化的B组头B1的索引
     */
    private static int START_IDX_OF_B;

    /**
     * 空座位
     */
    private static final Absent absent = Absent.create();

    /**
     * 构造函数
     *
     * @param length 列表长度
     */
    public BigTrafficJam(int length) {
        if (length < 3 || (length & 1) == 0) {
            throw new IllegalArgumentException("Length should be greater or equal to 3 and should not be a odd number");
        }
        LENGTH = length;
        LOW_IDX = 0;
        HIGH_IDX = LENGTH - 1;
        MID_IDX = LENGTH / 2;
        START_IDX_OF_A = MID_IDX - 1;
        START_IDX_OF_B = MID_IDX + 1;
    }

    /**
     * 执行入口，用于启动解决方法
     *
     * @return 不可变结果
     */
    public List<Id> solve() {
        List<Id> list = generateList(LENGTH);
        System.out.println("Start to solve " + list);
        swap(list, MID_IDX, START_IDX_OF_B);
        START_IDX_OF_B--;
        while (!isFinished(list)) {
            moveA(list);
            moveB(list);
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 退出条件：A组已全部移至高位，B组已全部移至低位。简单起见，只检查头和尾。
     *
     * @param list 待排列表
     *
     * @return 可以退出返回true，否则返回false
     */
    private boolean isFinished(List<Id> list) {
        return list.get(MID_IDX - 1).equals(B.id(MID_IDX))
                && list.get(MID_IDX + 1).equals(A.id(MID_IDX))
                && list.get(LOW_IDX).equals(B.id(1))
                && list.get(HIGH_IDX).equals(A.id(1));
    }

    /**
     * 生成初始列表。<br/>
     * 例如输入参数为<code>13</code>，
     * 则生成
     * <code>[A6, A5, A4, A3, A2, A1,  #, B1, B2, B3, B4, B5, B6]</code>
     *
     * @param length 座位数
     *
     * @return 初始列表
     */
    public static List<Id> generateList(int length) {
        List<Id> result = new ArrayList<>(length);
        for (int i = MID_IDX; i > 0; i--) {
            result.add(A.id(i));
        }
        result.add(absent);
        for (int i = 1; i <= MID_IDX; i++) {
            result.add(B.id(i));
        }
        return result;
    }

    /**
     * 生成初始列表。<br/>
     * 例如输入参数为<code>13</code>，
     * 则生成
     * <code>[A6, A5, A4, A3, A2, A1,  #, B1, B2, B3, B4, B5, B6]</code>
     *
     * @param length 座位数
     *
     * @return 初始列表
     */
    public static List<Id> generateAnsweredList(int length) {
        List<Id> result = new ArrayList<>(length);
        for (int i = 1; i <= MID_IDX; i++) {
            result.add(B.id(i));
        }
        result.add(absent);
        for (int i = MID_IDX; i > 0; i--) {
            result.add(A.id(i));
        }
        return result;
    }

    /**
     * 移动A组
     *
     * @param list 待排列表
     */
    private void moveA(List<Id> list) {
        int idx = START_IDX_OF_A;
        boolean isSetStartIdxOfA = false;
        while (true) {
            // 如果数组越界或者当前是B组的元素，则退出
            if (idx < LOW_IDX || idx > HIGH_IDX || list.get(idx) instanceof B) {
                break;
            }

            // 如果当前为空，或者已经是正确的位置了，则前后走，直到下一个应该被移动的A元素。
            // 这个条件适用于已经有A元素，例如A1,A2,A3...安排在合适的地方的情况下
            if (list.get(idx) == absent || (int) (list.get(idx).getId()) + idx == LENGTH) {
                idx--;
                continue;
            }

            // 如果A前面是B，并且再前面是空格，则往前跳一步，并且记录第一次跳的位置，代表A的头
            if (list.get(idx + 1) instanceof B
                    && list.get(idx + 2) == absent) {
                swap(list, idx, idx + 2);
                if (!isSetStartIdxOfA) {
                    if ((int) list.get(idx + 2).getId() + (idx + 2) != LENGTH) {
                        START_IDX_OF_A = idx + 2;
                    }
                    isSetStartIdxOfA = true;
                }
                // 当A1到了倒数第二个位置，需要再前进一步，这个分支专门处理这种情况
            } else if (list.get(idx + 1) == absent && (idx + 1 == HIGH_IDX)) {
                swap(list, idx, idx + 1);
                // 如果A前面是空，并且再往前就是B了，则前移一步
            } else if (list.get(idx + 1) == absent && list.get(idx + 2) instanceof B) {
                swap(list, idx, idx + 1);
                // 如果A前面是空，并且再往前就是A了，也就是看见队友了，那么分两种情况：
                // 1) 如果再往前一步就是正确的位置，则走，
                // 2) 否则退出。
            } else if (list.get(idx + 1) == absent && list.get(idx + 2) instanceof A) {
                if ((int) list.get(idx).getId() + (idx + 1) == LENGTH) {
                    swap(list, idx, idx + 1);
                } else {
                    break;
                }
            } else {
                throw new RuntimeException("Should not happen!");
            }
            // 移动到下一个待走或者跳的A
            if (--idx >= LOW_IDX && !(list.get(idx) instanceof A)) {
                idx--;
            }
        }
    }

    /**
     * 移动B组
     *
     * @param list 待排列表
     */
    private void moveB(List<Id> list) {
        int idx = START_IDX_OF_B;
        boolean isSetStartIdxOfB = false;
        while (true) {
            // 如果数组越界或者当前是A组的元素，则退出
            if (idx < LOW_IDX || idx > HIGH_IDX || (list.get(idx) instanceof A)) {
                break;
            }

            // 如果当前为空，或者已经是正确的位置了，则往前走，直到碰到下一个应该被移动的B元素。
            // 这个条件适用于已经有B元素，例如B1,B2,B3...安排在合适的地方的情况下
            if (list.get(idx) == absent || (int) (list.get(idx).getId()) - idx == 1) {
                idx++;
                continue;
            }

            // 如果B前面是A，并且再前面是空格，则往前跳一步，并且记录第一次跳的位置，代表B的头
            if (list.get(idx - 1) instanceof A
                    && list.get(idx - 2) == absent) {
                swap(list, idx, idx - 2);
                if (!isSetStartIdxOfB) {
                    START_IDX_OF_B = idx - 2;
                    isSetStartIdxOfB = true;
                }
                // 当B到了正数第二个位置，需要再前进一步，这个分支专门处理这种情况
            } else if (list.get(idx - 1) == absent && (idx - 1 == LOW_IDX)) {
                swap(list, idx, idx - 1);
                // 如果B前面是空，并且再往前就是A了，则前移一步
            } else if (list.get(idx - 1) == absent && list.get(idx - 2) instanceof A) {
                swap(list, idx, idx - 1);
                // 如果B前面是空，并且再往前就是B了，也就是看见队友了，那么分两种情况：
                // 1) 如果再往前一步就是正确的位置，则走，
                // 2) 否则退出。
            } else if (list.get(idx - 1) == absent && list.get(idx - 2) instanceof B) {
                if ((int) list.get(idx).getId() - (idx - 1) == 1) {
                    swap(list, idx, idx - 1);
                } else {
                    break;
                }
            } else {
                throw new RuntimeException("Should not happen!");
            }
            // 移动到下一个待走或者跳的B
            if (++idx <= HIGH_IDX && !(list.get(idx) instanceof B)) {
                idx++;
            }
        }
    }

    /**
     * 交换list中的指定索引元素，并且打印交换后的结果
     *
     * @param list 待排列表
     * @param idx1 索引1
     * @param idx2 索引2
     */
    private void swap(List<Id> list, int idx1, int idx2) {
        printout(list, idx1, idx2);
        Id tmp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, tmp);
        sleep();
        printout(list, idx1, idx2);
        sleep();
    }

    /**
     * 打印debug中间状态信息，并且打印这一步做了什么操作
     *
     * @param list
     * @param idx1
     * @param idx2
     */
    private void printout(List<Id> list, int idx1, int idx2) {
        if (debug) {
            Id toMoveElement = list.get(idx2);
            if (toMoveElement == absent) {
                toMoveElement = list.get(idx1);
            }
            ((Boldable) toMoveElement).setBold(true);
            System.out.println(
                    list + "    \033[1m" + toMoveElement + "\033[0m " + (Math.abs(idx1 - idx2) == 1 ? "move" : "jump"));
            ((Boldable) toMoveElement).setBold(false);
        }
    }

    /**
     * 停顿
     */
    private void sleep() {
        if (printOutIntervalMs > 0) {
            try {
                Thread.sleep(printOutIntervalMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BigTrafficJam setPrintOutIntervalMs(int printOutIntervalMs) {
        this.printOutIntervalMs = printOutIntervalMs;
        return this;
    }

    public BigTrafficJam setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }
}

/**
 * 可识别Id序号的接口
 *
 * @param <T> 泛型类型
 */
interface Id<T extends Number> {
    /**
     * 获取Id序号
     *
     * @return Id序号
     */
    T getId();
}

/**
 * 是否输出可加粗
 */
interface Boldable {

    void setBold(boolean bold);

    boolean isBold();
}

/**
 * 空座位，用符号<code>#</code>代表
 */
class Absent implements Id<Integer> {
    @Override
    public Integer getId() {
        throw new RuntimeException("Absent doest not support getId operation!");
    }

    /**
     * 新建工厂方法
     *
     * @return 空座位
     */
    public static Absent create() {
        return new Absent();
    }

    @Override
    public String toString() {
        return " #";
    }
}

/**
 * 字母序号
 */
abstract class Alpha implements Id<Integer>, Boldable {

    /**
     * 序号
     */
    protected Integer id;

    /**
     * 是否输出加粗
     */
    protected boolean bold;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        if (isBold()) {
            return "\033[1m" + this.getClass().getSimpleName() + id + "\033[0m";
        }
        return this.getClass().getSimpleName() + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Alpha alpha = (Alpha) o;

        return !(id != null ? !id.equals(alpha.id) : alpha.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isBold() {
        return bold;
    }

    @Override
    public void setBold(boolean bold) {
        this.bold = bold;
    }
}

/**
 * 字母A，也就是A组
 */
class A extends Alpha {

    public A(Integer id) {
        this.id = id;
    }

    public static A id(Integer id) {
        return new A(id);
    }
}

/**
 * 字母B，也就是B组
 */
class B extends Alpha {

    public B(Integer id) {
        this.id = id;
    }

    public static B id(Integer id) {
        return new B(id);
    }
}