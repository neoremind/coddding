package net.neoremind.mycode.nio.multithreadbio;

import java.net.ServerSocket;
import java.net.Socket;

import net.neoremind.mycode.nio.bio.TimeServerHandler;

public class TimeServer {

	public static void main(String[] args) {
		int port = 8080;
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server starts in port : " + port);
			Socket socket = null;
			TimeServerHandlerExecutePool executor = new TimeServerHandlerExecutePool(50, 1000);
			while (true) {
				socket = server.accept();
				executor.execute(new TimeServerHandler(socket));
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
