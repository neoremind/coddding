package net.neoremind.mycode.nio.nio.netty.protocol;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NetBytesSwitch {
	
	private static String STR_ENCODE="GBK";

	/**
	 * 
	 * @see 将int型数值转换成字节数组
	 * @param x
	 * @return
	 * @author Andy
	 * @date 2008-6-11
	 * @version 1.0.0
	 */
	public static byte[] IntToBytes(int x) {

		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) ((x >> (i * 8)) & 0xFF);// 低位存储
		}

		return b;

	}
	
	/**
	 * 
	 * @see 将java的long型数值转换成字节数组
	 * @param x
	 * @return
	 * @author Andy
	 * @date 2008-9-25
	 * @version 1.0.0
	 */
	public static byte[] LongToBytes(long x) {

		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) ((x >> (i * 8)) & 0xFF);// 低位存储
		}

		return b;

	}
	
	/**
	 * 
	 * @see 将c的long型数值转换成字节数组，有8个字节啊
	 * @param x
	 * @return
	 * @author Andy
	 * @date 2008-9-25
	 * @version 1.0.0
	 */
	public static byte[] LongToLongBytes(long x) {

		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) ((x >> (i * 8)) & 0xFF);// 低位存储
		}

		return b;

	}

	/**
	 * 
	 * @see 将字节数组转换成int型数值
	 * @param x
	 * @return
	 * @author Andy
	 * @date 2008-6-11
	 * @version 1.0.0
	 */
	public static int BytesToInt(byte[] x) {

		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < 4; i++) {
			bLoop = x[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);

		}
		return iOutcome;

	}
	
	/**
	 * 
	 * @see 将字节数组转换成long型数值，防止uint转换时越界出错
	 * @param x
	 * @return
	 * @author Andy
	 * @date 2008-6-11
	 * @version 1.0.0
	 */
	public static long BytesToLong(byte[] x) {

		long iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < 4; i++) {
			bLoop = x[i];
			iOutcome += (bLoop & 0xFFL) << (8 * i);

		}
		return iOutcome;

	}
	
	/**
	 * 
	 * @see 将string转换成gbk的字节码
	 * @param input
	 * @return byte[]
	 * @author puyuda
	 * @date 2008-6-10
	 * @version 1.0.0
	 */
	public static byte[] StringToBytes(String input){
		byte[] result=null;
		if(input!=null){
			try{
				result=input.getBytes(STR_ENCODE);
			}catch(UnsupportedEncodingException e){
			}
		}
		return result;
		
	}
	
	public static String bytesToString(byte[] input){
		String result=null;
		if(input!=null){
			try{
				result=new String(input,STR_ENCODE);
			}catch(Exception e){
			}
		}
		return result;
	}
	
	
	/**
	 * 转型
	 * @see
	 * @param x
	 * @return
	 * @author piggie
	 * @date 2008-10-23
	 * @version 1.0.0
	 */
	public static long BytesToLong2(byte[] x) {
			ByteBuffer buffer = ByteBuffer.allocate(x.length);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			buffer.put(x);
			return buffer.getLong(0);

	}
	
	/**
	 * 用BigInteger支持uint64
	 * 由于doris中存储各字段为uint，因此创建BigInteger时设置signnum为1（正数）
	 * 
	 * @param x 假定输入参数为8字节、little-endian，内部处理时将翻转为bigendian
	 * @author yanjie
	 * @version 1.2.18
	 */
	public static BigInteger BytesToBigInteger(byte[] x){
		byte[] nb = new byte[x.length];
		int len = x.length - 1;
		int signnum = 0;
		for (int i = 0; i <= len; i++){
			nb[i] = x[len - i];
			if ((signnum == 0) && (nb[i] != 0)){
				signnum = 1; //正数
			}
		}
		return new BigInteger(signnum, nb);
	}
	
	/**
	 * 返回指定长度的bytes
	 * @param input
	 * @param length
	 * @return
	 * piggie
	 * 2009-3-14
	 */
	public static byte[] StringToBytes(String input, int length){
		byte[] tmp = StringToBytes(input);
		byte[] bytes = new byte[length];
		System.arraycopy(tmp, 0, bytes, 0, tmp.length);
		return bytes;
	}
	

}