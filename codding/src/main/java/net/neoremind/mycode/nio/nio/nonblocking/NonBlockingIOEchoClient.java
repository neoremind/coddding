package net.neoremind.mycode.nio.nio.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingIOEchoClient {

	private Selector selector = null;
	
	private SocketChannel socketChannel = null;
	
	private int port = 8080;
	
	public NonBlockingIOEchoClient() throws IOException {
		selector = Selector.open();
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
	}
	
	public static void main(String[] args) throws Exception {
		NonBlockingIOEchoClient client = new NonBlockingIOEchoClient();
		client.talk();
	}
	
	public void talk() {
		try {
			connect();
			while (selector.select(1000) > 0) {
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = readyKeys.iterator();
				System.out.println(readyKeys.size());
				while (iter.hasNext()) {
					SelectionKey key = null;
					try {
						key = iter.next();
						iter.remove();
						
						if (key.isValid()) {
							if (key.isConnectable()) {
								System.out.println("Connect ready event happens!");
								if (socketChannel.finishConnect()) {
									socketChannel.register(selector, SelectionKey.OP_READ);
									send(socketChannel, "GET TIME");
								} else {
									System.out.println("Connect failed!");
									System.exit(1);
								}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connect() throws IOException {
		if (socketChannel.connect(new InetSocketAddress(port))) {
			System.out.println("Client connect to server successfully!");
			socketChannel.register(selector, SelectionKey.OP_READ);
		} else {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	public void send(SocketChannel socketChannel, String message) throws IOException {
		if (message != null && message.trim().length() > 0) {
			byte[] bytes = message.getBytes();
			ByteBuffer writeBuff = ByteBuffer.allocate(128);
			writeBuff.put(bytes);
			writeBuff.flip();
			socketChannel.write(writeBuff);
			if (!writeBuff.hasRemaining()) {
				System.out.println("Send message to server successfully!");
			}
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
		} else if (readBytesNum < 0) {
			key.cancel();
			socketChannel.close();
			System.out.println("Channel reach end, so close it");
		} else {
			;
		}
	}
	
}
