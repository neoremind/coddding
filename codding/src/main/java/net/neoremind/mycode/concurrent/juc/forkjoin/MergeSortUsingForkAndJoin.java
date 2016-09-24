package net.neoremind.mycode.concurrent.juc.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import org.junit.Test;

import net.neoremind.mycode.argorithm.sort.ArrayHelper;

/**
 * SingleThread Arrays.sort: 2400.512132 [msec]
 * ForkAndJoin: 1649.536186 [msec]
 *
 * @author zhangxu
 */
public class MergeSortUsingForkAndJoin {

    @Test
    public void testForkAndJoin() {
        int[] array = ArrayHelper.getShuffledArray(20000000);
        ForkJoinPool pool = new ForkJoinPool();
        long start = System.nanoTime();
        pool.submit(new MergeSortTask(array, 0, array.length)).join();
        System.out.println(String.format("ForkAndJoin: %f [msec]", (System.nanoTime() - start) / 1000000.0));
    }

    @Test
    public void testSingleThread() {
        int[] array = ArrayHelper.getShuffledArray(20000000);
        long start = System.nanoTime();
        Arrays.sort(array);
        System.out
                .println(String.format("SingleThread Arrays.sort: %f [msec]", (System.nanoTime() - start) / 1000000.0));
    }

    class MergeSortTask extends RecursiveAction {

        private final int[] array;
        private final int low;
        private final int high;

        MergeSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        public void compute() {
            int size = high - low;
            if (size <= 8) {
                Arrays.sort(array, low, high);
            } else {
                int middle = low + (size >> 1);
                invokeAll(new MergeSortTask(array, low, middle), new MergeSortTask(array, middle, high));
                merge(middle);
            }
        }

        private void merge(int middle) {
            if (array[middle - 1] < array[middle]) {
                return;
            }
            int copySize = high - low;
            int copyMiddle = middle - low;
            int[] copy = new int[copySize];
            System.arraycopy(array, low, copy, 0, copy.length);
            int p = 0;
            int q = copyMiddle;
            for (int i = low; i < high; ++i) {
                if (q >= copySize || (p < copyMiddle && copy[p] < copy[q])) {
                    array[i] = copy[p++];
                } else {
                    array[i] = copy[q++];
                }
            }
        }
    }

}
