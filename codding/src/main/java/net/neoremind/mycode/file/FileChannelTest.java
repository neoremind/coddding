package net.neoremind.mycode.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xu.zx
 */
public class FileChannelTest {

  public static final int SIZE = 1000000;

  public static final int LENGTH = 12;

  public static void main(String[] args) {
//    testWrite();
    testRead();
  }

  public static void testWrite() {
    File file = new File("/tmp/file-channel-test.dat");
    long counter = 0;
    int counter2 = 0;
    try (FileChannel fileChannel = new FileOutputStream(file).getChannel()) {
      ByteBuffer buffer = ByteBuffer.allocate(LENGTH);
      for (int i = 0; i < SIZE; i++) {
        buffer.putLong(counter++);
        buffer.putInt(counter2++);
        if (!buffer.hasRemaining()) {
          buffer.flip();
          fileChannel.write(buffer);
          System.out.println(counter);
          buffer.clear();
          // fileChannel.force(false);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void testRead() {
    File file = new File("/tmp/file-channel-test.dat");
    int offset = 0;
    try (FileChannel fileChannel = new FileInputStream(file).getChannel()) {
      System.out.println(fileChannel.size());
      for (int i = 0; i < SIZE; i++) {
        ByteBuffer buffer = read(fileChannel, offset, LENGTH);
//        System.out.println(buffer.getInt());
        offset += LENGTH;
      }
      System.out.println(offset);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static ByteBuffer read(FileChannel fileChannel, long offset, int length)
      throws IOException {
    ByteBuffer uncompressedBuffer = ByteBuffer.allocate(length);
    fileChannel.read(uncompressedBuffer, offset);
    if (uncompressedBuffer.hasRemaining()) {
      throw new IOException("Could not read all the data");
    }
    uncompressedBuffer.clear();
    return uncompressedBuffer;
  }

}
