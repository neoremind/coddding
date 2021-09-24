//package net.neoremind.mycode;
//
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class Main {
//
//    public static final int SIZE = 1000;
//
//    public static final int CONCURRENCY = 4;
//
//    public static void main(String[] args) {
//        int[] array = new int[SIZE];
//        int parrallelLevel = 4;
//        int[][] filter = new int[][]{{0, 9}, {90, 99}};
//        new Main().master(array, filter, parrallelLevel);
//    }
//
//    /**
//     * case 1:
//     * array of 10 elements,
//     * parrellevel 2
//     * elementNumToHandlePerTask = 5
//     * 0, 5
//     * 5, 10
//     * <p>
//     * case 2:
//     * array of 10 elements,
//     * parralevel 3
//     * elementNumToHandlePerTask = 3
//     * 0, 3
//     * 3, 6
//     * 6, 10
//     */
//    private void master(int[] array, int[][] filterArray, int parrallelLevel) {
//        // split array into many parts
//        // 1000 -> 100
//        // 150-250,910-930,960-990
//        // 0-100, 100-200 .......
//        // pruning data part skipping strategy
//        // 100-200 200-300 900-1000
//        // how elements I should = 300
//        // issue 10 paralelevel among the three ranges
//
//        // 0-0,2-2,4-4 ... 998-998
//        // how filters I have
//        // pareallevel
//        // data skew
//        // count how many elements I have to filter
//        // spilt the filter into parts
//        // 10 processors
//        int elementNumToHandlePerTask = array.length / parrallelLevel;
//
//        // do multi thread way
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        List<Future<Integer>> results = new ArrayList<>();
//        for (int i = 0; i < parrallelLevel; i++) {
//            Future<Integer> future = executorService.submit(new SubTask(array,
//                    i * elementNumToHandlePerTask,
//                    i == parrallelLevel - 1 ? array.length : i * elementNumToHandlePerTask + elementNumToHandlePerTask));
//            results.add(future);
//        }
//
//        // get result
//        int max = Integer.MIN_VALUE;
//        for (Future<Integer> result : results) {
//            max = Math.max(max, result.get());
//        }
//
//        // find the max among the result
//        System.out.println(max);
//    }
//
//    private class SubTask implements Callable<Integer> {
//
//        final int[] array;
//
//        final int from;
//
//        final int to;
//
//        public SubTask(int[] array, int from, int to) {
//            this.array = array;
//            this.from = from;
//            this.to = to;
//        }
//
//        @Override
//        public Integer call() throws Exception {
//            return worker(array, from, to);
//        }
//    }
//
//    private int worker(int[] array, int from, int to) {
//        if (from > to) {
//            throw new IllegalArgumentException();
//        }
//        int max = array[from];
//        for (int i = from; i < to; i++) {
//            for (int[] predicate: predicates) {
//                if (array[i] < predicate[0] && array[i] > predicate[1]) {
//                    continue;
//                }
//            }
//            max = Math.max(max, array[i]);
//        }
//        return max;
//    }
//
//}
