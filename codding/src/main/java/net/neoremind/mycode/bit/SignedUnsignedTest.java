package net.neoremind.mycode.bit;


public class SignedUnsignedTest {

	public static void main(String[] args) {
		byte x = 127;
		System.out.println("打印一个字节127:" + x); //127
		System.out.println("打印二进制，是正数，高位被扩展为符号位0，Integer.toBinaryString(127):" + Integer.toBinaryString(x)); //1111111(7个1)
		System.out.println("打印十六进制，是正数，高位被扩展为符号位0，Integer.toHexString(127):" + Integer.toHexString(x)); //7f
		
		x += 1;
		System.out.println("已经溢出了！((byte)127)+1:" + x);
		System.out.println("高位被扩展成符号位1，Integer.toBinaryString(127+1):" + Integer.toBinaryString(x)); //11111111111111111111111110000000
		System.out.println("高位被扩展成符号位1，Integer.toHexString(127+1):" + Integer.toHexString(x)); //ffffff80
		System.out.println("保证了高位不被扩展成符号位1，Integer.toHexString((127+1) & 0xff):" + Integer.toHexString(x & 0xff)); //80
	
		System.out.println("0x80:" + 0x80); // 128，java这种字面常亮，不遵循符号位扩展原则，高位全部补0
		System.out.println("0x81:" + 0x81); // 129
		System.out.println("0x82:" + 0x82); // 130
		System.out.println("(byte)0x80:" + (byte)0x80); // -128
		System.out.println("(byte)0x81:" + (byte)0x81); // -127
		System.out.println("(byte)0x82:" + (byte)0x82); // -126
		System.out.println("0x8000:" + 0x8000); // 32768
		System.out.println("(short)0x8000:" + (short)0x8000); // -32768
		System.out.println("0x80000000:" + 0x80000000); // -2147483648
		System.out.println("0x80000000L:" + 0x80000000L); // 2147483648
		
		System.out.println("高位被扩展为了fff...f，所以结果不对，Long.toHexString(0x1000000L + 0xcafebabe)：" + Long.toHexString(0x1000000L + 0xcafebabe)); // ffffffffcbfebabe
		System.out.println("高位没有杯扩展为了fff...f，所以结果对，Long.toHexString(0x1000000L + 0xcafebabe)：" + Long.toHexString(0x1000000L + 0xcafebabeL)); // cbfebabe
		
		System.out.println("如果是窄类型扩充为宽类型，符号位扩展，但是！！如果是char则扩展0，(int)(char)(byte)-1：" + (int)(char)(byte)-1); //65535
		System.out.println("如果是窄类型扩充为宽类型，符号位扩展，(int)(char)(byte)-1：" + (int)(short)(byte)-1); //-1
		
		char c = 'x';
		int y = c & 0xffff; //等同于int y = c; 如果是char，则零扩展
		System.out.println("char c = 'x';c:" + c);
		System.out.println("int y = c & 0xffff;y:" + y);
		
		byte b = -1;
		System.out.println("有符号位扩展，(short)b" + (short)b); // -1
		System.out.println("无符号位扩展，(short)(b & 0xff)" + (short)(b & 0xff)); //255
		
		System.out.println("符号位扩展后提升为int，高位全部补充了1，而右边的int高位是0，自然不相等，(byte)0x90 == 0x90:" + ((byte)0x90 == 0x90)); // false
		System.out.println("符号位扩展后提升为int，用掩码将其保留原值高位都是0，而右边的int高位是0，二者相等，(byte)0x90 == 0x90:" + (((byte)0x90 & 0xff) == 0x90)); // true
		
		byte[] byteArray = new byte[4];
		byteArray[0] = -1;
		byteArray[1] = 127;
		byteArray[2] = 3;
		byteArray[3] = -128;
		System.out.println(byteArrayToHexString(byteArray)); //ff7f0380
	}

	/**
	 * 将字节数组输出为十六进制字符串
	 * @param byteArray
	 * @return
	 */
	public static String byteArrayToHexString(byte[] byteArray) {
		String res = "";
		for (byte b : byteArray) {
			String hex = Integer.toHexString(b & 0xff); 
			hex = (hex.length() == 1) ? "0" + hex : hex;
			res += hex.toLowerCase();
		}
		return res;
	}
	
}
