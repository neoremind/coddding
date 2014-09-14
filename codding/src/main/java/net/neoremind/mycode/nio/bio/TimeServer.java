package net.neoremind.mycode.nio.bio;

import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {

	public static void main(String[] args) {
		int port = 8080;
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server starts in port : " + port);
			Socket socket = null;
			while (true) {
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
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
