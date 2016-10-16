package net.neoremind.mycode.nio.telnet;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangxu
 */
public class SimpleTelnetServer {

    public static void main(String[] args) {
        int port = 8090;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server starts in port : " + port);
            while (true) {
                Socket socket = server.accept();
                new Thread(new TelnetServerHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
