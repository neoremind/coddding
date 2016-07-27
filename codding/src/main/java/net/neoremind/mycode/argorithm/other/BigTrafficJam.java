package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
public class BigTrafficJam {

    private Absent absent = Absent.create();

    public static final int LENGTH = 13;
    public static final int LOW_IDX = 0;
    public static final int HIGH_IDX = LENGTH - 1;

    public static final int MID_IDX = LENGTH / 2;
    private static int START_IDX_OF_A = MID_IDX - 1;
    private static int START_IDX_OF_B = MID_IDX + 1;

    public List<Id> generateList(int length) {
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

    @Test
    public void test() throws Exception {
        //List<Id> list = Lists.newArrayList(A.id(3), A.id(2), A.id(1), absent, B.id(1), B.id(2), B.id(3));
        //        List<Id> list = Lists.newArrayList(A.id(4), A.id(3), A.id(2), A.id(1), absent, B.id(1), B.id(2), B
        //                .id(3), B
        //                .id(4));
        List<Id> list = generateList(LENGTH);
        //List<Id> list = Lists.newArrayList(A.id(2), A.id(1), absent, B.id(1), B.id(2));
        System.out.println(list);
        System.out.println(MID_IDX);
        swap(list, MID_IDX, START_IDX_OF_B);
        START_IDX_OF_B--;
        while (!isFinish(list)) {
            moveA(list);
            moveB(list);
        }
    }

    private boolean isFinish(List<Id> list) {
        return list.get(MID_IDX - 1).equals(B.id(MID_IDX))
                && list.get(MID_IDX + 1).equals(A.id(MID_IDX))
                && list.get(LOW_IDX).equals(B.id(1))
                && list.get(HIGH_IDX).equals(A.id(1));
    }

    public void moveA(List<Id> list) {
        int idx = START_IDX_OF_A;
        boolean isSetStartIdxOfA = false;
        while (true) {
            //System.out.println("enter a " + idx);
            if (idx < LOW_IDX || idx > HIGH_IDX || list.get(idx) instanceof B) {
                break;
            }

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
                // 如果A前面是空，(不会发生)
            } else if (list.get(idx + 1) == absent && (idx + 1 == HIGH_IDX)) {
                swap(list, idx, idx + 1);
                // 如果A前面是空，并且再往前就是B了，则前移一步
            } else if (list.get(idx + 1) == absent && list.get(idx + 2) instanceof B) {
                swap(list, idx, idx + 1);
                // 如果A前面是空，并且再往前就是A了，也就是看见队友了，那么退出
            } else if (list.get(idx + 1) == absent && list.get(idx + 2) instanceof A) {
                if ((int) list.get(idx).getId() + (idx + 1) == LENGTH) {
                    swap(list, idx, idx + 1);
                } else {
                    break;
                }
            } else {
                throw new RuntimeException("Should not happen!");
            }
            if (--idx >= LOW_IDX && !(list.get(idx) instanceof A)) {
                idx--;
            }
        }
    }

    public void moveB(List<Id> list) {
        int idx = START_IDX_OF_B;
        boolean isSetStartIdxOfB = false;
        while (true) {
            //System.out.println("enter b " + idx);
            if (idx < LOW_IDX || idx > HIGH_IDX || (list.get(idx) instanceof A)) {
                break;
            }

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
                // 如果B前面是空，并且空就是起点，则交换
            } else if (list.get(idx - 1) == absent && (idx - 1 == LOW_IDX)) {
                swap(list, idx, idx - 1);
                // 如果B前面是空，并且再往前就是A了，则前移一步
            } else if (list.get(idx - 1) == absent && list.get(idx - 2) instanceof A) {
                swap(list, idx, idx - 1);
                // 如果B前面是空，并且再往前就是B了，也就是看见队友了，则前移一步
            } else if (list.get(idx - 1) == absent && list.get(idx - 2) instanceof B) {
                if ((int) list.get(idx).getId() - (idx - 1) == 1) {
                    swap(list, idx, idx - 1);
                } else {
                    break;
                }
            } else {
                throw new RuntimeException("Should not happen!");
            }
            if (++idx <= HIGH_IDX && !(list.get(idx) instanceof B)) {
                idx++;
            }
        }
    }

    public void swap(List<Id> list, int idx1, int idx2) {
        Id tmp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, tmp);
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        System.out.println(list);
    }

}

interface Id<T extends Number> {
    T getId();
}

class Absent implements Id<Integer> {
    @Override
    public Integer getId() {
        throw new RuntimeException("Absent doest not support getId operation!");
    }

    public static Absent create() {
        return new Absent();
    }

    @Override
    public String toString() {
        return " #";
    }
}

abstract class Alpha implements Id<Integer> {

    public Integer id;

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
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
}

class A extends Alpha {

    public A(Integer id) {
        this.id = id;
    }

    public static A id(Integer id) {
        return new A(id);
    }
}

class B extends Alpha {

    public B(Integer id) {
        this.id = id;
    }

    public static B id(Integer id) {
        return new B(id);
    }
}
