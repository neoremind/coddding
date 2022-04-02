package net.neoremind.mycode.nio.simple;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class SimpleClient2 {

    private final ByteBuffer writeBuffer = ByteBuffer.allocate(256);

    private final ByteBuffer readBuffer = ByteBuffer.allocate(256);

    public void run() throws IOException {
        try {
            Random random = new Random(System.currentTimeMillis());
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            for (int i = 0; i < 1; i++) {
                int bodyLen = 1 + random.nextInt(40);
                String text = RandomStringUtils.randomAlphanumeric(bodyLen);
                writeBuffer.putInt(bodyLen);
                writeBuffer.put(text.getBytes(StandardCharsets.UTF_8));
                writeBuffer.flip();
                //System.out.println("request =" + text + " " + bodyLen);
                client.write(writeBuffer);
                writeBuffer.clear();
                int numOfBytesRead = client.read(readBuffer);
                String response = new String(readBuffer.array(), 0, numOfBytesRead);
                //System.out.println("response=" + response);
                if (!response.equals(text.toUpperCase())) {
                    throw new RuntimeException("Not equal!");
                }
                readBuffer.clear();
//                if (++i % 100 == 0) {
//                    try {
//                        Thread.sleep(1);
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
        Thread[] threads = new Thread[1];
        for (int i = 0; i < 1; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new SimpleClient2().run();
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
