package net.neoremind.mycode.guava.eventbus;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.common.eventbus.Subscribe;

/**
 * 消息消费者，echo message+时间戳，写入socket的输出流
 *
 * @author zhangxu
 */
public class ServerMessageConsumer {

    private PrintWriter out;

    public ServerMessageConsumer(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void recieveMessage(String message) {
        if (out != null) {
            out.println("Echo " + message + " " + System.currentTimeMillis());
        }
    }

}
