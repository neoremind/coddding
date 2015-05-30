package net.neoremind.mycode.nio.nio.netty.protocol;

public class UnsignedSwitch {

	/**
	 *<p>
	 *</p>
	 * 
	 * @see
	 * @param x
	 * @return
	 * @author futuremining
	 * @date 2009-2-17
	 * @version 1.0.0
	 */
	public static long UintToLong(int x) {
		return x & 0xffffffffl;
	}
	public static int UShortToInt(short x) {
		return (int)(x & 0xffff);
	}
	/**
	 *<p>
	 *</p>
	 * 
	 * @see
	 * @param x
	 * @return
	 * @author futuremining
	 * @date 2009-2-17
	 * @version 1.0.0
	 */
	public static int LongToUint(long x) {
		return (int) (x & 0xffffffff);
	}
	
	
	/**
	 * 将int转成unsigned short
	 * @param x
	 * @return
	 * piggie
	 * 2009-3-17
	 */
	public static short IntToUshort(int x) {
		return (short)(x & 0xffff);
	}
}
