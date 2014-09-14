package net.neoremind.mycode.nio.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {

	private Socket socket;
	
	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(), true);
			String currentTime = null;
			String body = null;
			while (true) {
				body = in.readLine();
				if (body == null) {
					break;
				}
				System.out.println("The time server receive request : " + body);
				currentTime = "GET TIME".equals(body) ? new Date(System.currentTimeMillis()).toString() : "BAD REQUEST";
				out.println(currentTime);
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
