package net.neoremind.mycode.nio.nio.blocking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingIOEchoServer {
	
	private int port = 8080;
	
	private ServerSocketChannel serverSocketChannel = null;
	
	private ExecutorService executor;

	public BlockingIOEchoServer() {
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Server starts!");
	}
	
	public static void main(String[] args) {
		new BlockingIOEchoServer().service();
	}
	
	public void service() {
		while(true) {
			SocketChannel socketChannel = null;
			try {
				socketChannel = serverSocketChannel.accept();
				executor.execute(new Handler(socketChannel));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

class Handler implements Runnable {
	
	private SocketChannel socketChannel;
	
	public Handler(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	
	@Override
	public void run() {
		handle(socketChannel);
	}
	
	private void handle(SocketChannel socketChannel) {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			Socket socket = socketChannel.socket();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			String line = null;
			
			while((line = in.readLine()) != null) {
				System.out.println(line);
				out.println("Echo=>" + line);
				if (line.equals("bye")) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socketChannel != null) {
					socketChannel.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}




