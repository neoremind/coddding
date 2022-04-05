package net.neoremind.mycode.nio.simple.benchmark;

import lombok.extern.slf4j.Slf4j;
import net.neoremind.mycode.nio.simple.SimpleClient;
import net.neoremind.mycode.nio.simple.client.CallFuture;
import net.neoremind.mycode.nio.simple.client.NioClient;
import net.neoremind.mycode.nio.simple.client.TimeoutException;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConcurrentClientCall {

    private final BlockingQueue<NioClient> clientPool = new LinkedBlockingQueue<>();

    public void run(String[] args) throws IOException, InterruptedException {
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        int readBufferSize = Integer.parseInt(args[3]);
        int numOfClientConnections = Integer.parseInt(args[4]);
        int threadCount = Integer.parseInt(args[5]);
        long numOfCallsPerThread = Integer.parseInt(args[6]);
        if ("inf".equals(args[6])) {
            numOfCallsPerThread = -1;
        }
        int maxRandomStringLength = Integer.parseInt(args[7]);
        boolean checkResultEquals = Boolean.parseBoolean(args[8]);
        execute(host, port, readBufferSize, numOfClientConnections, threadCount, numOfCallsPerThread, maxRandomStringLength, checkResultEquals);
    }

    public void execute(String host, int port, int readBufferSize, int numOfClientConnections,
                        int threadCount, final long numOfCallsPerThread, int maxRandomStringLength, boolean checkResultEquals)
            throws IOException, InterruptedException {
        log.info("host={}, port={}, readBufferSize={}, numOfClientConnections={}," +
                        "threadCount={}, numOfCallsPerThread={}, maxRandomStringLength={}, checkResultEquals={}", host, port,
                readBufferSize, numOfClientConnections, threadCount, numOfCallsPerThread, maxRandomStringLength, checkResultEquals);
        for (int i = 0; i < numOfClientConnections; i++) {
            NioClient nioClient = new NioClient(host, port, readBufferSize);
            clientPool.add(nioClient);
        }

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    Random random = new Random(System.currentTimeMillis() + Thread.currentThread().getId());
                    long limit = numOfCallsPerThread;
                    if (numOfCallsPerThread == -1L) {
                        limit = Long.MAX_VALUE;
                    }
                    try {
                        for (int j = 0; j < limit; j++) {
                            try {
                                int bodyLen = 1 + random.nextInt(maxRandomStringLength);
                                String text = RandomStringUtils.randomAlphanumeric(bodyLen);
                                byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
                                NioClient client = clientPool.take();
                                CallFuture<byte[]> future = client.asyncSend(bytes);
                                clientPool.add(client);
                                String response = new String(future.get(500, TimeUnit.MILLISECONDS));
                                //System.out.println("response=" + response);
                                if (checkResultEquals && !response.equals(text)) {
                                    throw new RuntimeException("Not equal!");
                                }
                                if (j % 100000 == 0) {
                                    printStat(j, startTime);
                                }
                            } catch (TimeoutException e) {
                                log.warn("timeout found due to no response on time");
                                // omit
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }

                private void printStat(long counter, long startTime) {
                    long elapsed = System.currentTimeMillis() - startTime;
                    double tps = (counter * 1.0d / elapsed) * 1000d;
                    log.info("Running {}ms, counter={}, TPS={}", elapsed, counter, tps);
                }
            });
            threads[i].setName("Caller-" + i);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Thread.sleep(600000);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new ConcurrentClientCall()
                .execute("localhost", 8080, 1024, 4, 32, 10000000, 40, true);
    }
}
