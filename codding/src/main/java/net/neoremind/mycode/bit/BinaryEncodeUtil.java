package net.neoremind.mycode.bit;

import java.nio.ByteBuffer;

public class BinaryEncodeUtil {

	public static int varLengthSize(int value) {
		int size = 1;
		while ((value & (~0x7f)) != 0) {
			value >>>= 7;
			size++;
		}
		return size;
	}

	public static int varLengthSize (long value) {
		int size = 1;
		while ((value & (~0x7f)) != 0) {
			value >>>= 7;
			size++;
		}
		return size;
	}

	/**
	 * Encode int value with msb method to reduce storage space.
	 * Each byte in a varint, except the last byte, has the most significant bit(msb)
	 * set – this indicates that there are further bytes to come.
	 *
	 * This function also applies to negative int number use a ZigZag encode.
	 * ZigZag encoding maps signed integers to unsigned integers so that numbers
	 * with a small absolute value (for instance, -1) have a small varint encoded
	 * value too. It does this in a way that "zig-zags" back and forth through the
	 * positive and negative integers, so that -1 is encoded as 1, 1 is encoded as 2,
	 * -2 is encoded as 3, and so on
	 * @param intValue : value to be encoded
	 * @param buffer   : encoded bytes
	 * @return size    : encoded bytes of intValue
	 */
	public static int writeInt(int value, ByteBuffer buffer) {
		int size = 1;
		while ((value & 0xFFFFFF80) != 0L) {
			buffer.put((byte) ((value & 0x7F) | 0x80));
			value >>>= 7;
			size++;
		}
		buffer.put((byte) (value & 0x7F));
		return size;
	}

	/**
	 * Encode long value with msb method to reduce storage space.
	 * Same principle with writeVarLengthInt, extends from 32bit to 64bit only
	 * @param longValue to be encoded
	 * @param buffer to write encoded bytes
	 * @return encode size
	 */
	public static int writeLong(long value , ByteBuffer buffer) {
		int size = 1;
		while ((value & (~0x7f)) != 0) {
			buffer.put((byte) ((value & 0x7f) | 0x80));
			value >>>= 7;
			size++;
		}
		buffer.put((byte) value);
		return size;
	}
}
