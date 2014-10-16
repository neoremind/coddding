package net.neoremind.mycode.nio.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferTest {

	public static void main(String[] args) throws Exception {
		testAllocate();
		testWrap();
	}
	
	public static void testWrap() throws Exception {
		short s = -1; //2 byte
		int i = 65535; //4 byte
		long l = 0x7fffL; //8 byte
		System.out.println(Long.parseLong("7fff", 16));
		byte[] str = "hello world".getBytes("UTF-8"); //max 16 byte
		byte[] strByte4send = new byte[16];
		System.arraycopy(str, 0, strByte4send, 0, str.length);
		
		ByteBuffer buf = ByteBuffer.allocate(30);
		buf.putShort(s).putInt(i).putLong(l).put(strByte4send);
		
		ByteBuffer buffer = ByteBuffer.wrap(buf.array());
		
		// 这里试试LITTLE_ENDIAN，数字会乱掉，而字符串不会，原因是数字式用的getXXX()；
		// Java默认是大尾端BIG_ENDIAN；
		// 如果是和C/C++交互，一般linux/windows平台都是小尾端的，所以这里在写跨语言通信的
		// 时候要注意
		buffer.order(ByteOrder.BIG_ENDIAN); 
		
		System.out.println(buffer.getShort());
		System.out.println(buffer.getInt());
		System.out.println(buffer.getLong());
		
		byte[] strByte4get = new byte[16];
		buffer.get(strByte4get, 0, 16);
		System.out.println(new String(strByte4get, "UTF-8"));
		System.out.println("-------");
	}
	
	public static void testAllocate() throws Exception {
		String str = "Hello world!";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		printByteBufferInfo(buffer);
		buffer.put(str.getBytes("UTF-8"));
		printByteBufferInfo(buffer);
		buffer.flip();
		printByteBufferInfo(buffer);
		
		System.out.println(buffer.array().length);
		System.out.println(buffer.remaining());
		
		byte[] packet = new byte[buffer.remaining()];
		buffer.get(packet);
		System.out.println(packet.length);
		System.out.println("-------");
	}
	
	private static void printByteBufferInfo(ByteBuffer buffer) {
		System.out.println("capacity:" + buffer.capacity());
		System.out.println("limit:" + buffer.limit());
		System.out.println("position:" + buffer.position());
		System.out.println("------");
	}
	
}
