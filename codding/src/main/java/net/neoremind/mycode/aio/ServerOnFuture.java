package net.neoremind.mycode.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Created with IntelliJ IDEA. 16/2/24 16:57
 * </p>
 * <p>
 * ClassName:ServerOnFuture
 * </p>
 * <p>
 * Description:基于Future的NIO2服务端实现，此时的服务端还无法实现多客户端并发，如果有多个客户端并发连接该服务端的话，
 * 客户端会出现阻塞，待前一个客户端处理完毕，服务端才会接受下一个客户端的连接并处理
 * </P>
 */
@Slf4j
public class ServerOnFuture {
  static final int DEFAULT_PORT = 7777;
  static final String IP = "127.0.0.1";
  static ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

  public static void main(String[] args) {
    try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
      if (serverSocketChannel.isOpen()) {
        serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        serverSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
        log.info("Waiting for connections...");
        while (true) {
          Future<AsynchronousSocketChannel> channelFuture = serverSocketChannel.accept();
          try (AsynchronousSocketChannel socketChannel = channelFuture.get()) {
            log.info("Incoming connection from : " + socketChannel.getRemoteAddress());
            while (socketChannel.read(buffer).get() != -1) {
              buffer.flip();
              // Java NIO2或者Java AIO报： java.util.concurrent.ExecutionException: java.io.IOException: 指定的网络名不再可用。
              // 此处要注意，千万不能直接操作buffer，否则客户端会阻塞并报错，“java.util.concurrent.ExecutionException: java.io.IOException: 指定的网络名不再可用。”
              ByteBuffer duplicate = buffer.duplicate();
              showMessage(duplicate);
              socketChannel.write(buffer).get();
              if (buffer.hasRemaining()) {
                buffer.compact();
              } else {
                buffer.clear();
              }
            }
            log.info(socketChannel.getRemoteAddress() + " was successfully served!");
          } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
          }
        }
      } else {
        log.warn("The asynchronous server-socket channel cannot be opened!");
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  protected static void showMessage(ByteBuffer buffer) {
    CharBuffer decode = Charset.defaultCharset().decode(buffer);
    log.info(decode.toString());
  }
}
