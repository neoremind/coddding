package net.neoremind.mycode.nio.nio.netty.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 公共包头NsHead
 * @author piggie
 *
 */
public class NsHead {
	public static final int NSHEAD_LEN = 36;
	 
	protected int id; 
	protected int version;
	
	/**
	 * unsigned int (4)
	 * 由 apache 产生的 logid，贯穿一次请求的所有网络交互。
	 * 要求前端产生的 logid 在短时间内 (例如 10 秒内) 在所有前端服务器范围内都不会重复出现
	 * 目的是用时间和 log_id 能够确定唯一一次 kr 会话
	 */
	protected long logId = Math.abs(new Random(System.currentTimeMillis()).nextInt());
	
	/**
	 * char (16)
	 * 请求包为客户端标识，命名方式：产品名-模块名，比如 "sf-web\0", "im-ext\0",”fc-web\0”，凤巢客户端一定要填上”fc-web\0”，否则得到的res中的竞价客户数是shifen的竞价客户数
	 */
	protected String provider;
	
	/**
	 * unsigned int (4)
	 * 特殊标识：常数 0xfb709394，标识一个包的起始
	 */
	private long magicNum = 0xfb709394;
	
	protected long reserved;
	
	/**
	 * unsigned int (4)
	 * head后数据的总长度
	 */
	protected long bodyLen;


	/**
	 * 将请求转为字节流
	 * @return
	 * piggie
	 * 2009-3-14
	 */
	public byte[] toBytes() throws RuntimeException{
		
		ByteBuffer bb = ByteBuffer.allocate(NSHEAD_LEN);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		try {
			bb.putShort(UnsignedSwitch.IntToUshort(id));
			bb.putShort(UnsignedSwitch.IntToUshort(version));
			bb.putInt(UnsignedSwitch.LongToUint(logId));
			byte[] prvd = NetBytesSwitch.StringToBytes(provider, 16);
			bb.put(prvd);
			bb.putInt(UnsignedSwitch.LongToUint(magicNum));
			bb.putInt(UnsignedSwitch.LongToUint(reserved));
			bb.putInt(UnsignedSwitch.LongToUint(bodyLen));
		} catch (Exception e) {
			throw new RuntimeException("exception when putting bytes for nshead...", e);
		}
		
		return bb.array();
	}
	
	/**
	 * @param input
	 */
	public NsHead(byte[] input){
		wrap(input);
	}
	public NsHead() {
		
	}
	
	/**
	 * 赋值所有字段
	 * @param input
	 * liuzeyin
	 * 2009-12-3
	 */
	public void wrap(byte[] input){
		ByteBuffer bb = ByteBuffer.allocate(NSHEAD_LEN);
		bb.order(ByteOrder.LITTLE_ENDIAN);		
		bb.put(input);
		bb.flip();
		
		id = UnsignedSwitch.UShortToInt(bb.getShort());
		version = UnsignedSwitch.UShortToInt(bb.getShort());
		logId = UnsignedSwitch.UintToLong(bb.getInt());
		byte[] bf = new byte[16];
		bb.get(bf);
		provider = NetBytesSwitch.bytesToString(bf);
		magicNum = UnsignedSwitch.UintToLong(bb.getInt());
		reserved = UnsignedSwitch.UintToLong(bb.getInt());
		bodyLen = UnsignedSwitch.UintToLong(bb.getInt());
	}
	
	//-----------------getter & setter-----------------
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public long getMagicNum() {
		return magicNum;
	}

	public void setMagicNum(long magicNum) {
		this.magicNum = magicNum;
	}

	public long getReserved() {
		return reserved;
	}

	public void setReserved(long reserved) {
		this.reserved = reserved;
	}

	public long getBodyLen() {
		return bodyLen;
	}

	public void setBodyLen(long bodyLen) {
		this.bodyLen = bodyLen;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
