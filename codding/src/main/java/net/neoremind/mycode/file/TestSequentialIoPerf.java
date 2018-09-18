package net.neoremind.mycode.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.System.out;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

/**
 * 400MB file
 * ===========
 * RandomAccessFile    write=379,610,750   read=1,452,482,269 bytes/sec
 * RandomAccessFile    write=294,041,636   read=1,494,890,510 bytes/sec
 * RandomAccessFile    write=250,980,392   read=1,422,222,222 bytes/sec
 * RandomAccessFile    write=250,366,748   read=1,388,474,576 bytes/sec
 * RandomAccessFile    write=260,394,151   read=1,422,222,222 bytes/sec
 *
 * BufferedStreamFile  write=98,178,331    read=286,433,566 bytes/sec
 * BufferedStreamFile  write=100,244,738   read=288,857,545 bytes/sec
 * BufferedStreamFile  write=82,948,562    read=154,100,827 bytes/sec
 * BufferedStreamFile  write=108,503,311   read=153,869,271 bytes/sec
 * BufferedStreamFile  write=113,055,478   read=152,608,047 bytes/sec
 *
 * BufferedChannelFile write=228,443,948   read=356,173,913 bytes/sec
 * BufferedChannelFile write=265,629,053   read=374,063,926 bytes/sec
 * BufferedChannelFile write=223,825,136   read=1,539,849,624 bytes/sec
 * BufferedChannelFile write=232,992,036   read=1,539,849,624 bytes/sec
 * BufferedChannelFile write=212,779,220   read=1,534,082,397 bytes/sec
 *
 * MemoryMappedFile    write=300,955,180   read=305,899,925 bytes/sec
 * MemoryMappedFile    write=313,149,847   read=310,538,286 bytes/sec
 * MemoryMappedFile    write=326,374,501   read=303,857,566 bytes/sec
 * MemoryMappedFile    write=327,680,000   read=304,535,315 bytes/sec
 * MemoryMappedFile    write=326,895,450   read=303,632,320 bytes/sec
 *
 * 8GB File
 * ============
 * RandomAccessFile    write=167,402,321   read=251,922,012 bytes/sec
 * RandomAccessFile    write=193,934,802   read=257,052,307 bytes/sec
 * RandomAccessFile    write=192,948,159   read=248,460,768 bytes/sec
 * RandomAccessFile    write=191,814,180   read=245,225,408 bytes/sec
 * RandomAccessFile    write=190,635,762   read=275,315,073 bytes/sec
 *
 * BufferedStreamFile  write=154,823,102   read=248,355,313 bytes/sec
 * BufferedStreamFile  write=152,083,913   read=253,418,301 bytes/sec
 * BufferedStreamFile  write=133,099,369   read=146,056,197 bytes/sec
 * BufferedStreamFile  write=131,065,708   read=146,217,827 bytes/sec
 * BufferedStreamFile  write=132,694,052   read=148,116,004 bytes/sec
 *
 * BufferedChannelFile write=186,703,740   read=215,075,218 bytes/sec
 * BufferedChannelFile write=190,591,410   read=211,030,680 bytes/sec
 * BufferedChannelFile write=187,220,038   read=223,087,606 bytes/sec
 * BufferedChannelFile write=191,585,397   read=221,297,747 bytes/sec
 * BufferedChannelFile write=192,653,214   read=211,789,038 bytes/sec
 *
 * MemoryMappedFile    write=123,023,322   read=231,530,156 bytes/sec
 * MemoryMappedFile    write=121,961,023   read=230,403,600 bytes/sec
 * MemoryMappedFile    write=123,317,778   read=229,899,250 bytes/sec
 * MemoryMappedFile    write=121,472,738   read=231,739,745 bytes/sec
 * MemoryMappedFile    write=120,362,615   read=231,190,382 bytes/sec
 *
 * @author xu.zx
 */
public class TestSequentialIoPerf {
  public static final int PAGE_SIZE = 1024 * 4;
//  public static final long FILE_SIZE = PAGE_SIZE * 2000L * 1000L;
  public static final long FILE_SIZE = PAGE_SIZE * 2000L * 100L;
  public static final String FILE_NAME = "/tmp/test.dat";
  public static final byte[] BLANK_PAGE = new byte[PAGE_SIZE];

  public static void main(final String[] arg) throws Exception {
    preallocateTestFile(FILE_NAME);

    for (final PerfTestCase testCase : testCases) {
      for (int i = 0; i < 5; i++) {
        System.gc();
        long writeDurationMs = testCase.test(PerfTestCase.Type.WRITE,
            FILE_NAME);

        System.gc();
        long readDurationMs = testCase.test(PerfTestCase.Type.READ,
            FILE_NAME);

        long bytesReadPerSec = (FILE_SIZE * 1000L) / readDurationMs;
        long bytesWrittenPerSec = (FILE_SIZE * 1000L) / writeDurationMs;

        out.format("%s\twrite=%,d\tread=%,d bytes/sec\n",
            testCase.getName(),
            bytesWrittenPerSec, bytesReadPerSec);
      }
    }

    deleteFile(FILE_NAME);
  }

  private static void preallocateTestFile(final String fileName)
      throws Exception {
    RandomAccessFile file = new RandomAccessFile(fileName, "rw");

    for (long i = 0; i < FILE_SIZE; i += PAGE_SIZE) {
      file.write(BLANK_PAGE, 0, PAGE_SIZE);
    }

    file.close();
  }

  private static void deleteFile(final String testFileName) throws Exception {
    File file = new File(testFileName);
    if (!file.delete()) {
      out.println("Failed to delete test file=" + testFileName);
      out.println("Windows does not allow mapped files to be deleted.");
    }
  }

  public abstract static class PerfTestCase {
    public enum Type {READ, WRITE}

    private final String name;
    private int checkSum;

    public PerfTestCase(final String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public long test(final Type type, final String fileName) {
      long start = System.currentTimeMillis();

      try {
        switch (type) {
          case WRITE: {
            checkSum = testWrite(fileName);
            break;
          }

          case READ: {
            final int checkSum = testRead(fileName);
            if (checkSum != this.checkSum) {
              final String msg = getName() +
                  " expected=" + this.checkSum +
                  " got=" + checkSum;
              throw new IllegalStateException(msg);
            }
            break;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      return System.currentTimeMillis() - start;
    }

    public abstract int testWrite(final String fileName) throws Exception;

    public abstract int testRead(final String fileName) throws Exception;
  }

  private static PerfTestCase[] testCases =
      {
          new PerfTestCase("RandomAccessFile") {
            @Override
            public int testWrite(final String fileName) throws Exception {
              RandomAccessFile file = new RandomAccessFile(fileName, "rw");
              final byte[] buffer = new byte[PAGE_SIZE];
              int pos = 0;
              int checkSum = 0;

              for (long i = 0; i < FILE_SIZE; i++) {
                byte b = (byte) i;
                checkSum += b;

                buffer[pos++] = b;
                if (PAGE_SIZE == pos) {
                  file.write(buffer, 0, PAGE_SIZE);
                  pos = 0;
                }
              }

              file.close();

              return checkSum;
            }

            @Override
            public int testRead(final String fileName) throws Exception {
              RandomAccessFile file = new RandomAccessFile(fileName, "r");
              final byte[] buffer = new byte[PAGE_SIZE];
              int checkSum = 0;
              int bytesRead;

              while (-1 != (bytesRead = file.read(buffer))) {
                for (int i = 0; i < bytesRead; i++) {
                  checkSum += buffer[i];
                }
              }

              file.close();

              return checkSum;
            }
          },

          new PerfTestCase("BufferedStreamFile") {
            @Override
            public int testWrite(final String fileName) throws Exception {
              int checkSum = 0;
              OutputStream out =
                  new BufferedOutputStream(new FileOutputStream(fileName));

              for (long i = 0; i < FILE_SIZE; i++) {
                byte b = (byte) i;
                checkSum += b;
                out.write(b);
              }

              out.close();

              return checkSum;
            }

            @Override
            public int testRead(final String fileName) throws Exception {
              int checkSum = 0;
              InputStream in =
                  new BufferedInputStream(new FileInputStream(fileName));

              int b;
              while (-1 != (b = in.read())) {
                checkSum += (byte) b;
              }

              in.close();

              return checkSum;
            }
          },


          new PerfTestCase("BufferedChannelFile") {
            @Override
            public int testWrite(final String fileName) throws Exception {
              FileChannel channel =
                  new RandomAccessFile(fileName, "rw").getChannel();
              ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
              int checkSum = 0;

              for (long i = 0; i < FILE_SIZE; i++) {
                byte b = (byte) i;
                checkSum += b;
                buffer.put(b);

                if (!buffer.hasRemaining()) {
                  buffer.flip();
                  channel.write(buffer);
                  buffer.clear();
                }
              }

              channel.close();

              return checkSum;
            }

            @Override
            public int testRead(final String fileName) throws Exception {
              FileChannel channel =
                  new RandomAccessFile(fileName, "rw").getChannel();
              ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
              int checkSum = 0;

              while (-1 != (channel.read(buffer))) {
                buffer.flip();

                while (buffer.hasRemaining()) {
                  checkSum += buffer.get();
                }

                buffer.clear();
              }

              return checkSum;
            }
          },

          new PerfTestCase("MemoryMappedFile") {
            @Override
            public int testWrite(final String fileName) throws Exception {
              FileChannel channel =
                  new RandomAccessFile(fileName, "rw").getChannel();
              MappedByteBuffer buffer =
                  channel.map(READ_WRITE, 0,
                      Math.min(channel.size(), MAX_VALUE));
              int checkSum = 0;

              for (long i = 0; i < FILE_SIZE; i++) {
                if (!buffer.hasRemaining()) {
                  buffer =
                      channel.map(READ_WRITE, i,
                          Math.min(channel.size() - i, MAX_VALUE));
                }

                byte b = (byte) i;
                checkSum += b;
                buffer.put(b);
              }

              channel.close();

              return checkSum;
            }

            @Override
            public int testRead(final String fileName) throws Exception {
              FileChannel channel =
                  new RandomAccessFile(fileName, "rw").getChannel();
              MappedByteBuffer buffer =
                  channel.map(READ_ONLY, 0,
                      Math.min(channel.size(), MAX_VALUE));
              int checkSum = 0;

              for (long i = 0; i < FILE_SIZE; i++) {
                if (!buffer.hasRemaining()) {
                  buffer =
                      channel.map(READ_WRITE, i,
                          Math.min(channel.size() - i, MAX_VALUE));
                }

                checkSum += buffer.get();
              }

              channel.close();

              return checkSum;
            }
          },
      };
}
