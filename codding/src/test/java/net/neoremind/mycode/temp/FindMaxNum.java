package net.neoremind.mycode.temp;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class FindMaxNum {

    private static final int SIZE_OF_INT = 4;

    @Test
    public void testFunctionality() {
        int totalElementNum = 102;
        long address = generateOffHeapRandomIntArray(new Random(), totalElementNum, 200);
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < totalElementNum; i++) {
            int element = Uns.getInt(address, (long) i * SIZE_OF_INT);
            System.out.println(element);
            max = Math.max(max, Uns.getInt(address, (long) i * SIZE_OF_INT));
        }
        int actual = new FindMaxNum().master(address, totalElementNum, null, 4, 8);
        System.out.println(max + " " + actual);
        Assert.assertEquals(max, actual);
    }

    @Test
    public void testPerformance() {
        long totalElementNum = 1_000_000_000;
        int partitionNum = 1;
        int concurrency = 1;
        long address = generateOffHeapRandomIntArray(new Random(), totalElementNum);
        int actual = new FindMaxNum().master(address, totalElementNum, null, partitionNum, concurrency);
        System.out.println(actual);
    }

    @Test
    public void testFunctionalityFilterArray() {
        int totalElementNum = 10000;
        int partitionNum = 1;
        int concurrency = 1;
        long address = generateOffHeapRandomIntArray(new Random(), totalElementNum, 1000000);
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < totalElementNum; i++) {
            int element = Uns.getInt(address, (long) i * SIZE_OF_INT);
//            System.out.println(element);
            if (i % 2 == 0) {
                max = Math.max(max, Uns.getInt(address, (long) i * SIZE_OF_INT));
            }
        }
        int[][] filterArray = new int[(int) totalElementNum / 2][2];
        for (int i = 0; i < filterArray.length; i++) {
            filterArray[i][0] = i * 2;
            filterArray[i][1] = i * 2;
//            System.out.println(Arrays.toString(filterArray[i]));
        }
        int actual = new FindMaxNum().master(address, totalElementNum, filterArray, partitionNum, concurrency);
        System.out.println(max + " " + actual);
        Assert.assertEquals(max, actual);
    }

    @Test
    public void testPerformanceFilterArray() {
        long totalElementNum = 1_000_000_000;
        int partitionNum = 8;
        int concurrency = 8;
        long address = generateOffHeapRandomIntArray(new Random(), totalElementNum);
        int[][] filterArray = new int[(int) totalElementNum / 100][2];
        for (int i = 0; i < filterArray.length / 2; i++) {
            filterArray[i][0] = i * 2;
            filterArray[i][1] = i * 2;
        }
        int actual = new FindMaxNum().master(address, totalElementNum, filterArray, partitionNum, concurrency);
        System.out.println(actual);
    }

    private int master(long address, long totalElementNum, int[][] filterArray, int partitionNum, int concurrency) {
        int numOfElementPerTask = (int) (totalElementNum / partitionNum);

        ExecutorService executorService = Executors.newFixedThreadPool(concurrency);
        System.out.println("starts to run");
        long start = System.currentTimeMillis();
        List<Future<Integer>> results = new ArrayList<>();
        for (int i = 0; i < partitionNum; i++) {
            Future<Integer> future = executorService.submit(new SubTask(
                    address + (long) i * numOfElementPerTask * SIZE_OF_INT,
                    i == partitionNum - 1 ? (int) (totalElementNum - (i * numOfElementPerTask)) : numOfElementPerTask,
                    filterArray));
            results.add(future);
        }

        int max = Integer.MIN_VALUE;
        for (Future<Integer> result : results) {
            try {
                max = Math.max(max, result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("use %dms%n", System.currentTimeMillis() - start);
        executorService.shutdown();
        return max;
    }

    private int worker(long address, int numOfElement) {
        int max = Uns.getInt(address, 0);
        for (long i = 1; i < numOfElement; i++) {
            max = Math.max(max, Uns.getInt(address, i * SIZE_OF_INT));
        }
        return max;
    }

    private int workerWithCpuPipeline(long address, int numOfElement) {
        int max = Uns.getInt(address, 0);
        for (long i = 1; i < numOfElement - 8; i += 8) {
            max = Math.max(max, Uns.getInt(address, i * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 1) * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 2) * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 3) * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 4) * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 5) * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 6) * SIZE_OF_INT));
            max = Math.max(max, Uns.getInt(address, (i + 7) * SIZE_OF_INT));
        }
        for (long i = numOfElement - 8; i < numOfElement; i++) {
            max = Math.max(max, Uns.getInt(address, i * SIZE_OF_INT));
        }
        return max;
    }

    private int worker(long address, int[][] filterArray, int numOfElement) {
        int max = Uns.getInt(address, 0);
        for (int i = 0; i < filterArray.length; i++) {
            for (long j = filterArray[i][0]; j <= filterArray[i][1]; j++) {
                max = Math.max(max, Uns.getInt(address, j * SIZE_OF_INT));
            }
        }
        return max;
    }

    private int workerOptimized(long address, int[][] filterArray, int numOfElement) {
        long numOfHitElement = 0L;
        for (int i = 0; i < filterArray.length; i++) {
            numOfHitElement += (filterArray[i][1] - filterArray[i][0] + 1);
        }
        long copiedAddress = Uns.allocate(SIZE_OF_INT * numOfHitElement);
        long offset = 0L;
        for (int i = 0; i < filterArray.length; i++) {
            long size = (long) (filterArray[i][1] - filterArray[i][0] + 1) * SIZE_OF_INT;
            Uns.copyMemory(address, (long) filterArray[i][0] * SIZE_OF_INT,
                    copiedAddress, offset, size);
            offset += size;
        }
        int max = Uns.getInt(copiedAddress, 0);
        for (long i = 1; i < numOfHitElement; i++) {
            // System.out.println(i * SIZE_OF_INT + " " + i);
            max = Math.max(max, Uns.getInt(copiedAddress, i * SIZE_OF_INT));
        }
        return max;
    }

    private class SubTask implements Callable<Integer> {

        final long address;

        final int numOfElement;

        final int[][] filterArray;

        public SubTask(long address, int numOfElement, int[][] filterArray) {
            this.address = address;
            this.numOfElement = numOfElement;
            this.filterArray = filterArray;
        }

        @Override
        public Integer call() throws Exception {
            return worker(address, filterArray, numOfElement);
        }
    }

    private static long generateOffHeapRandomIntArray(Random random, long totalElementNum) {
        long address = Uns.allocate(SIZE_OF_INT * totalElementNum);
        for (int i = 0; i < totalElementNum; i++) {
            Uns.putInt(address, (long) i * SIZE_OF_INT, random.nextInt(Integer.MAX_VALUE));
        }
        return address;
    }

    private static long generateOffHeapRandomIntArray(Random random, long totalElementNum, int limit) {
        long address = Uns.allocate(SIZE_OF_INT * totalElementNum);
        for (int i = 0; i < totalElementNum; i++) {
            Uns.putInt(address, (long) i * SIZE_OF_INT, random.nextInt(limit));
        }
        return address;
    }
}
