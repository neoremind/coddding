package net.neoremind.mycode.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Created with IntelliJ IDEA. 16/2/24 20:14
 * </p>
 * <p>
 * ClassName:ServerOnCompletionHandler
 * </p>
 * <p>
 * Description:基于CompletionHandler实现NIO2的服务端
 * </P>
 */
@Slf4j
public class ServerOnCompletionHandler {
  static final int DEFAULT_PORT = 7777;
  static final String IP = "127.0.0.1";

  public static void main(String[] args) {
    try (final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
      if (serverSocketChannel.isOpen()) {
        serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
        serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        serverSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
        log.info("Waiting for connections...");
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
          final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

          @Override
          public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
            //注意接收一个连接之后，紧接着可以接收下一个连接，所以必须再次调用accept方法
            serverSocketChannel.accept(null, this);
            try {
              log.info("Incoming connection from : " + socketChannel.getRemoteAddress());
              while (socketChannel.read(buffer).get() != -1) {
                buffer.flip();
                final ByteBuffer duplicate = buffer.duplicate();
                final CharBuffer decode = Charset.defaultCharset().decode(duplicate);
                log.info(decode.toString());
                socketChannel.write(buffer).get();
                if (buffer.hasRemaining()) {
                  buffer.compact();
                } else {
                  buffer.clear();
                }
              }
            } catch (InterruptedException | ExecutionException | IOException e) {
              log.error(e.getMessage(), e);
            } finally {
              try {
                socketChannel.close();
              } catch (IOException e) {
                log.error(e.getMessage(), e);
              }
            }
          }

          @Override
          public void failed(Throwable exc, Void attachment) {
            serverSocketChannel.accept(null, this);
            throw new UnsupportedOperationException("Cannot accept connections!");
          }
        });
        //主要是阻塞作用，因为AIO是异步的，所以此处不阻塞的话，主线程很快执行完毕，并会关闭通道
        System.in.read();
      } else {
        log.warn("The asynchronous server-socket channel cannot be opened!");
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}
