//package net.neoremind.mycode;
//
//import com.google.common.base.Preconditions;
//import fj.test.Bool;
//
//import java.io.*;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.*;
//
///**
// * big text file
// * fill with letter
// * <p>
// * given a short string
// * find if it is in the file?
// * <p>
// * JDK indexOf
// */
//public class Main {
//
//    private static final int TASK_CONCURRENCY = 4;
//
//    public static void main(String[] args) {
//        // call the master to launch..
//    }
//
//    /**
//     *
//     * hello world! Microsoft and azure.
//     *         |          |
//     *
//     * hello and azure
//     *
//     * world! Microsoft
//     *
//     * 9
//     *
//     */
//    private void master(String file, String target, int partitionNum) throws ExecutionException, InterruptedException {
//        long size = new File(file)....
//
//        long lenOfEachPart = size / partitionNum;
//
//        // handle size % partitionNum != 0;
//
//        ExecutorService executorService = Executors.newFixedThreadPool(TASK_CONCURRENCY);
//        List<Future<Result>> results = new ArrayList<>(partitionNum);
//        for (int i = 0; i < partitionNum; i++) {
//            long offset = i * lenOfEachPart;
//            Future<Result> future = executorService.submit(new Task(file, offset,
//                    (int) (i == partitionNum - 1 ? size - offset : lenOfEachPart),
//                    target));
//            results.add(future);
//        }
//
//        // ****------**** **** -----****
//        //
//        String prevPartTail = "";
//        for (int i = 0; i < partitionNum; i++) {
//            Result result = results.get(i).get();
//            if (result.exist) {
//                System.out.println("found!");
//            }
//            String boundaryStr = prevPartTail + result.head;
//            int boundaryIndex = boundaryStr.indexOf(target);
//            if (boundaryIndex > 0) {
//                System.out.println("found!");
//            }
//            prevPartTail = result.tail;
//        }
//
//        System.out.println("not found!");
//    }
//
//    private class Task implements Callable<Boolean> {
//
//        private final String file;
//
//        private final long offset;
//
//        private final int len;
//
//        private final String target;
//
//        public Task(String file, long offset, int len, String target) {
//            this.file = file;
//            this.offset = offset;
//            this.len = len;
//            this.target = target;
//        }
//
//        @Override
//        public Boolean call() throws Exception {
//            return worker(file, offset, len, target);
//        }
//    }
//
//    private boolean worker(String file, long offset, long len, String target) throws UnsupportedEncodingException {
//        return existInFile(file, offset, len, target);
//    }
//
//    private Result existInFile(String file, long offset, long len, String target) throws UnsupportedEncodingException {
//        // read strings out of the file
//        // todo
//        // buffer io/ mmap/ directio
//        // seek to offset, read len, leverage buffer to do it
//        // bytes into string (utf-8)
//
//        // make sure target is not blank....
//        Preconditions.checkArgument(len >= target.length(), "illegal args");
//        ByteBuffer buffer = ByteBuffer.allocate((int) len);
//        try (FileChannel fileChannel = new FileInputStream(new File(file)).getChannel()) {
//            fileChannel.position(offset);
//            fileChannel.read(buffer);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // builder design pattern
//        Result result = new Result();
//        String str = new String(buffer.array(), "UTF-8");
//        String head = str.substring(0, target.length());
//        String tail = str.substring((int) (len - target.length()), len);
//        for (int i = 0; i < (str.length() - target.length()); i++) {
//            int j = 0;
//            while (str.charAt(i + j) == target.charAt(j)) {
//                j++;
//            }
//            if (j == target.length()) {
//                result.exist = true;
//                return true;
//            }
//        }
//        result.exist = false;
//        return false;
//    }
//
//    private Result existInFilePerformantVersion(String file, long offset, long len, String target) throws UnsupportedEncodingException {
//        // read strings out of the file
//        // todo
//        // buffer io/ mmap/ directio
//        // seek to offset, read len, leverage buffer to do it
//        // bytes into string (utf-8)
//
//        // make sure target is not blank....
//        Preconditions.checkArgument(len >= target.length(), "illegal args");
//
//        // builder design pattern
//        Result result = new Result();
//
//        int bufferSize = 4096;
//        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
//
//        // TODO
//        // put head and tail into the result...
//
//        String prevPartTail = "";
//        try (FileChannel fileChannel = new FileInputStream(new File(file)).getChannel()) {
//            // partition
//            // loop
//            {
//                fileChannel.position(offset);
//                fileChannel.read(buffer);
//                String str = new String(buffer.array(), "UTF-8");
//
//                // handle edge case
//                String boundaryStr = prevPartTail + str.substring(target.length());
//                int boundaryIndex = boundaryStr.indexOf(target);
//                if (boundaryIndex > 0) {
//                    result.exist = true;
//                    return result;
//                }
//                prevPartTail = result.tail;
//
//                for (int i = 0; i < (str.length() - target.length()); i++) {
//                    int j = 0;
//                    while (str.charAt(i + j) == target.charAt(j)) {
//                        j++;
//                    }
//                    if (j == target.length()) {
//                        result.exist = true;
//                        return result;
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        result.exist = false;
//        return result;
//    }
//
//    private class Result {
//        boolean exist;
//        String head;
//        String tail;
//    }
//
//}
