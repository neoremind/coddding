package net.neoremind.mycode.aio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AIOEchoServer {

  public final static int PORT = 8001;
  public final static String IP = "127.0.0.1";


  private AsynchronousServerSocketChannel server = null;

  public AIOEchoServer() {
    try {
      //同样是利用工厂方法产生一个通道，异步通道 AsynchronousServerSocketChannel
      server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(IP, PORT));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //使用这个通道(server)来进行客户端的接收和处理
  public void start() {
    System.out.println("Server listen on " + PORT);

    //注册事件和事件完成后的处理器，这个CompletionHandler就是事件完成后的处理器
    server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

      final ByteBuffer readBuffer = ByteBuffer.allocate(32);

      @Override
      public void completed(AsynchronousSocketChannel result, Object attachment) {
        Future<Integer> writeResult = null;
        try {
          result.read(readBuffer).get();

          readBuffer.flip();
          final CharBuffer decode = Charset.defaultCharset().decode(readBuffer);
          String receivedMsg = decode.toString();

          System.out.println(Thread.currentThread().getName() + " - received: " + receivedMsg);

          //将数据写回客户端, to upper case
          ByteBuffer writeBuffer = ByteBuffer.allocate(receivedMsg.length());
          writeBuffer.put(receivedMsg.toUpperCase().getBytes());
          writeBuffer.flip();
          writeResult = result.write(writeBuffer);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          server.accept(null, this);
          try {
            writeResult.get();
            result.close();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
          readBuffer.clear();
        }

      }

      @Override
      public void failed(Throwable exc, Object attachment) {
        exc.printStackTrace();
      }

    });
  }

  public static void main(String[] args) {
    new AIOEchoServer().start();
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
