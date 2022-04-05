package net.neoremind.mycode.nio.simple;

import net.neoremind.mycode.nio.simple.client.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class SimpleClient {

    private final ByteBuffer writeBuffer = ByteBuffer.allocate(256);

    private final ByteBuffer readBuffer = ByteBuffer.allocate(256);

    private long counter = 0;

    private long totalCounter = 0L;

    private long startTime = System.currentTimeMillis();

    public void run() throws IOException {
        try {
            Random random = new Random(System.currentTimeMillis());
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            long times = Integer.MAX_VALUE;
            for (int i = 0; i < times; i++) {
                int bodyLen = 1 + random.nextInt(40);
                String text = RandomStringUtils.randomAlphanumeric(bodyLen);
                int logId = IdGenerator.genUUID();
                writeBuffer.putInt(bodyLen + 4);
                writeBuffer.putInt(logId);
                writeBuffer.put(text.getBytes(StandardCharsets.UTF_8));
                writeBuffer.flip();
                System.out.println("request =" + text + " " + bodyLen);
                client.write(writeBuffer);
                writeBuffer.clear();
                int numOfBytesRead = client.read(readBuffer);
                readBuffer.flip();
                int actualBodyLen = readBuffer.getInt();
                int actualLogId = readBuffer.getInt();
                if (logId != actualLogId) {
                    throw new RuntimeException("log id not equal!");
                }
                byte[] res = new byte[numOfBytesRead - 8];
                readBuffer.get(res);
                String response = new String(res);
                System.out.println("response=" + response + " " + response.length());
                if (!response.equals(text.toUpperCase())) {
                    throw new RuntimeException("Not equal!");
                }
                readBuffer.clear();
                counter++;
                totalCounter++;
                if (counter % 100000 == 0) {
                    System.out.println(totalCounter + " " + (counter * 1.0) / (System.currentTimeMillis() - startTime));
                    counter = 0;
                    startTime = System.currentTimeMillis();
                }
//                if (++i % 1 == 0) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int threadCount = 1;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new SimpleClient().run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Thread.sleep(600000);
    }
}
