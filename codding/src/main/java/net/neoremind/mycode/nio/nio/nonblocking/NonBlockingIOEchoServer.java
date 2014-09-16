package net.neoremind.mycode.nio.nio.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingIOEchoServer {

	private Selector selector = null;
	
	private ServerSocketChannel serverSocketChannel = null;
	
	private int port = 8080;
	
	private Charset charset = Charset.forName("GBK");
	
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
			Iterator<SelectionKey> iter = readyKeys.iterator();
			while (iter.hasNext()) {
				SelectionKey key = null;
				try {
					key = iter.next();
					iter.remove();
					
					if (key.isAcceptable()) {
						System.out.println("Accept ready event happens!");
						ServerSocketChannel ssc = (ServerSocketChannel)(key.channel());
						SocketChannel socketChannel = (SocketChannel)(ssc.accept());
						System.out.println("Receive client connect from " + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					}
					
					if (key.isReadable()) {
						System.out.println("Read ready event happens!");
						receive(key);
					}
					
//					if (key.isWritable()) {
//						//System.out.println("Write ready event happens!");
//						send(key);
//					}
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
	
	public void send(SelectionKey key) throws IOException {
		ByteBuffer buffer = (ByteBuffer)key.attachment();
		SocketChannel socketChannel = (SocketChannel)key.channel();
		buffer.flip();
		String data = decode(buffer);
		//System.out.println(data);
		if (data.indexOf("\r\n") == -1) {
			return;
		}
		String outputData = data.substring(0, data.indexOf("\n") + 1);
		System.out.println(outputData);
		ByteBuffer outputBuffer = encode("Echo:" + outputData);
		while (outputBuffer.hasRemaining()) {
			socketChannel.write(outputBuffer);
		}
		
		ByteBuffer temp = encode(outputData);
		buffer.position(temp.limit());
		buffer.compact();
		
		if (outputData.equals("bye\r\n")) {
			key.cancel();
			socketChannel.close();
			System.out.println("Close connection with client");
		}
	}
	
	public void receive(SelectionKey key) throws IOException {
		ByteBuffer buffer = (ByteBuffer)key.attachment();
		SocketChannel socketChannel = (SocketChannel)key.channel();
		socketChannel.read(readBuff)
		ByteBuffer readBuff = ByteBuffer.allocate(32);
		socketChannel.read(readBuff);
		readBuff.flip();
		
		buffer.limit(buffer.capacity());
		buffer.put(readBuff);
		System.out.println(decode(buffer));
	}
	
	public String decode(ByteBuffer buffer) {
		CharBuffer charBuffer = charset.decode(buffer);
		return charBuffer.toString();
	}
	
	public ByteBuffer encode(String str) {
		return charset.encode(str);
	}
	
}
