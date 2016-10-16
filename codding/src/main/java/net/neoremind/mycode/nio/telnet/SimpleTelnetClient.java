package net.neoremind.mycode.nio.telnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 控制台请使用：telnet 127.0.0.1 8090
 *
 * @author zhangxu
 */
public class SimpleTelnetClient {

    public static void main(String[] args) {
        int port = 8090;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("i am neo");
            System.out.println("Send request to server!");
            String res = in.readLine();
            System.out.println("Response is " + res);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

}
