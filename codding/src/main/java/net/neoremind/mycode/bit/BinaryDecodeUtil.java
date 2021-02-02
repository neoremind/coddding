package net.neoremind.mycode.bit;

import java.nio.ByteBuffer;

public class BinaryDecodeUtil {

	/**
	 * decode int value from bytes in buffer
	 * @param buffer
	 * @return
	 */
	public static int readInt(ByteBuffer buffer) {
		int value = 0;
		int i = 0;
		int b;
		while (((b=buffer.get()) & 0x80) != 0) {
			value |= (b & 0x7F) << i;
			i += 7;
		}
		return value |= (b << i);
	}

	/**
	 * decode long value from bytes in buffer
	 * @param buffer
	 * @return
	 */
	public static long readLong(ByteBuffer buffer) {
		long value = 0;
		int i = 0;
		long b;
		while (((b = buffer.get()) & 0x80) != 0) {
			value |= (b & 0x7F) << i;
			i += 7;
		}
		return value |= (b << i);
	}

}
