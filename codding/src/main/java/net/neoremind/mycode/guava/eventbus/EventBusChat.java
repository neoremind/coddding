package net.neoremind.mycode.guava.eventbus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.common.eventbus.EventBus;

/**
 * 模拟eventbus，服务端有个{@link ServerMessageHandler}来接收telnet的输入，然后post到event bus，用{@link ServerMessageConsumer}来消费。
 * <p/>
 * 使用如下命令：
 * <pre>
 * telnet 127.0.0.1 8089
 * </pre>
 */
public class EventBusChat {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8089);
            while (true) {
                Socket socket = serverSocket.accept();
                eventBus.register(new ServerMessageConsumer(socket));
                new Thread(new ServerMessageHandler(socket, eventBus)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
