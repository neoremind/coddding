package net.neoremind.mycode.aio.time.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

  private AsynchronousSocketChannel client;
  private String host;
  private int port;
  private CountDownLatch latch;

  //首先通过AsynchronousSocketChannel的open方法创建一个新的AsynchronousSocketChannel对象。
  public AsyncTimeClientHandler(String host, int port) {
    this.host = host;
    this.port = port;
    try {
      client = AsynchronousSocketChannel.open();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    //创建CountDownLatch进行等待，防止异步操作没有执行完成线程就退出。
    latch = new CountDownLatch(1);
    //通过connect方法发起异步操作，它有两个参数，
    //A attachment：AsynchronousSocketChannel的附件，用于回调通知时作为入参被传递，调用者可以自定义；
    //CompletionHandler＜Void,? super A＞ handler：异步操作回调通知接口，由调用者实现。
    client.connect(new InetSocketAddress(host, port), this, this);
    try {
      latch.await();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //异步连接成功之后的方法回调——completed方法
  @Override
  public void completed(Void result, AsyncTimeClientHandler attachment) {
    //创建请求消息体，对其进行编码，然后复制到发送缓冲区writeBuffer中，
    //调用Asynchronous SocketChannel的write方法进行异步写。
    //与服务端类似，我们可以实现CompletionHandler ＜Integer, ByteBuffer＞接口用于写操作完成后的回调。
    byte[] req = "QUERY TIME ORDER".getBytes();
    ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
    writeBuffer.put(req);
    writeBuffer.flip();
    client.write(writeBuffer, writeBuffer,
        new CompletionHandler<Integer, ByteBuffer>() {
          @Override
          public void completed(Integer result, ByteBuffer buffer) {
            //如果发送缓冲区中仍有尚未发送的字节，将继续异步发送，如果已经发送完成，则执行异步读取操作。
            if (buffer.hasRemaining()) {
              client.write(buffer, buffer, this);
            } else {
              //客户端异步读取时间服务器服务端应答消息的处理逻辑
              ByteBuffer readBuffer = ByteBuffer.allocate(1024);
              //调用AsynchronousSocketChannel的read方法异步读取服务端的响应消息。
              //由于read操作是异步的，所以我们通过内部匿名类实现CompletionHandler＜Integer，ByteBuffer＞接口,
              //当读取完成被JDK回调时，构造应答消息。
              client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                  //从CompletionHandler的ByteBuffer中读取应答消息，然后打印结果。
                  buffer.flip();
                  byte[] bytes = new byte[buffer.remaining()];
                  buffer.get(bytes);
                  String body;
                  try {
                    body = new String(bytes, "UTF-8");
                    System.out.println("Now is : " + body);
                    latch.countDown();
                  } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                  }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                  //当读取发生异常时，关闭链路，
                  //同时调用CountDownLatch的countDown方法让AsyncTimeClientHandler线程执行完毕，客户端退出执行。
                  try {
                    client.close();
                    latch.countDown();
                  } catch (IOException e) {
                    // ingnore on close
                  }
                }
              });
            }
          }

          @Override
          public void failed(Throwable exc, ByteBuffer attachment) {
            try {
              client.close();
              latch.countDown();
            } catch (IOException e) {
              // ingnore on close
            }
          }
        });
  }

  @Override
  public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
    exc.printStackTrace();
    try {
      client.close();
      latch.countDown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
