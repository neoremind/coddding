package net.neoremind.mycode.nio.nio.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingIOEchoServer {

	private Selector selector = null;
	
	private ServerSocketChannel serverSocketChannel = null;
	
	private int port = 8080;
	
	public NonBlockingIOEchoServer() throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("Server starts!");
	}
	
	public static void main(String[] args) throws Exception {
		NonBlockingIOEchoServer server = new NonBlockingIOEchoServer();
		server.service();
	}
	
	public void service() throws IOException {
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) {
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			System.out.println(readyKeys.size() + " selection key is ready");
			Iterator<SelectionKey> iter = readyKeys.iterator();
			while (iter.hasNext()) {
				SelectionKey key = null;
				try {
					key = iter.next();
					iter.remove();
					
					if (key.isValid()) {
						if (key.isAcceptable()) {
							System.out.println("Accept ready event happens!");
							ServerSocketChannel ssc = (ServerSocketChannel)(key.channel());
							SocketChannel socketChannel = (SocketChannel)(ssc.accept());
							System.out.println("Receive client connect from " + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
						}
						
						if (key.isReadable()) {
							System.out.println("Read ready event happens!");
							receive(key);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						if (key != null) {
							key.cancel();
							key.channel().close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}
	
	public void send(SocketChannel socketChannel, String message) throws IOException {
		if (message != null && message.trim().length() > 0) {
			byte[] bytes = message.getBytes();
			ByteBuffer writeBuff = ByteBuffer.allocate(128);
			writeBuff.put(bytes);
			writeBuff.flip();
			socketChannel.write(writeBuff);
			System.out.println("Send to client successfully!");
		}
	}
	
	public void receive(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel)key.channel();
		ByteBuffer readBuff = ByteBuffer.allocate(128);
		int readBytesNum = socketChannel.read(readBuff);
		System.out.println("Receive byte number:" + readBytesNum);
		if (readBytesNum > 0) {
			readBuff.flip();
			byte[] bytes = new byte[readBuff.remaining()];
			readBuff.get(bytes);
			String message = new String(bytes, "UTF-8");
			System.out.println("Receive message:" + message);
			send(socketChannel, message);
		} else if (readBytesNum < 0) {
			key.cancel();
			socketChannel.close();
			System.out.println("Channel reach end, so close it");
		} else {
			;
		}
	}
	
}
