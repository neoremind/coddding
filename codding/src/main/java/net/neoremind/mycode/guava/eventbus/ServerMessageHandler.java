package net.neoremind.mycode.guava.eventbus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.common.eventbus.EventBus;

/**
 * 用event bus解耦接收消息{@link ServerMessageHandler}以及消费消息{@link ServerMessageConsumer}
 * <p/>
 * 一旦接收到socket连接，则尝试读取流，然后post到event bus，不知道谁会消费。
 *
 * @author zhangxu
 */
public class ServerMessageHandler implements Runnable {

    private EventBus eventbus;

    private Socket socket;

    public ServerMessageHandler(Socket socket, EventBus eventbus) {
        this.socket = socket;
        this.eventbus = eventbus;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                System.out.println("The server receives message : " + input);
                eventbus.post(input);
            }
        } catch (IOException e) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                socket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } finally {
            // If EOF reached
            eventbus.unregister(this);
        }
    }

}
