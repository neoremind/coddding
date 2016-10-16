package net.neoremind.mycode.nio.telnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author zhangxu
 */
public class TelnetServerHandler implements Runnable {

    private Socket socket;

    public TelnetServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            // 必须带有autoFlush=true！！！
            out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
            boolean isCancel = false;
            while (!isCancel) {
                String body = in.readLine();
                if (body != null) {
                    System.out.println(body);
                    out.println("Wow! " + body.toUpperCase());
                    if (body.equals("bye")) {
                        isCancel = true;
                    }
                }
            }
        } catch (Exception e) {
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

            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
