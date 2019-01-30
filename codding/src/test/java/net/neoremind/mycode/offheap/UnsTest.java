package net.neoremind.mycode.offheap;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author xu.zx
 */
public class UnsTest {

  long bytesFromMb(long mb) {
    return mb * 1024 * 1024;
  }

  @Test
  public void test() throws InterruptedException {
    long address = Uns.allocate(bytesFromMb(768), true);
    Uns.putInt(address, 0, 1);
    Uns.putInt(address, 4, 2);
    Uns.putInt(address, 8, Integer.MAX_VALUE);
    Uns.putInt(address, bytesFromMb(758) - 4, Integer.MAX_VALUE);
    System.out.println("====");
    ByteBuffer buffer = Uns.directBufferFor(address, 0, 12, true);
    System.out.println("-----");
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    System.out.println(buffer.toString());
    System.out.println(buffer.getInt());
    System.out.println(buffer.getInt());
//    System.out.println(buffer.getInt());
    System.out.println(Uns.getInt(address, bytesFromMb(758) - 4));
    Uns.free(address);
    Thread.sleep(30000);
  }

  @Test
  public void test1() throws InterruptedException {
    long address = Uns.allocate(24, true);
    byte[] bytes = "123".getBytes();
    Uns.copyMemory(bytes, 0, address, 12, bytes.length);
    byte[] result = new byte[bytes.length];
    Uns.copyMemory(address, 13, result, 0, bytes.length);
    System.out.println(new String(result));
  }
}
