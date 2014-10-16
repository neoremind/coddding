package net.neoremind.mycode.bit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 
 * I have a byte[4] which contains a 32-bit unsigned integer (in big endian order) and I need to convert it to long (as int can't hold an unsigned number).

Also, how do I do it vice-versa (i.e. from long that contains a 32-bit unsigned integer to byte[4])?

 * @author helechen
 *
 */
public class EndianTest {

	public static void main(String[] args) {
	    byte[] payload = toArray(-1991249);
	    int number = fromArray(payload);
	    System.out.println(number); //-1991249
	    
	    System.out.println("按照大尾端来从byte[4]中解出int");
	    int result = 0xFF & payload[0];
	    result <<= 8;
	    result += 0xFF & payload[1];
	    result <<= 8;
	    result += 0xFF & payload[2];
	    result <<= 8;
	    result += 0xFF & payload[3];
	    System.out.println(result); //-1991249
	}

	public static  int fromArray(byte[] payload){
	    ByteBuffer buffer = ByteBuffer.wrap(payload);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    return buffer.getInt();
	}

	public static byte[] toArray(int value){
	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    buffer.putInt(value);
	    buffer.flip();
	    return buffer.array();
	}
	
}
