package net.neoremind.mycode.aio.time.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

  private AsynchronousSocketChannel channel;

  public ReadCompletionHandler(AsynchronousSocketChannel channel) {
    //将AsynchronousSocketChannel通过参数传递到ReadCompletion Handler中当作成员变量来使用
    //主要用于读取半包消息和发送应答。本例程不对半包读写进行具体说明
    if (this.channel == null)
      this.channel = channel;
  }

  @Override
  public void completed(Integer result, ByteBuffer attachment) {
    //读取到消息后的处理，首先对attachment进行flip操作，为后续从缓冲区读取数据做准备。
    attachment.flip();
    //根据缓冲区的可读字节数创建byte数组
    byte[] body = new byte[attachment.remaining()];
    attachment.get(body);
    try {
      //通过new String方法创建请求消息，对请求消息进行判断，
      //如果是"QUERY TIME ORDER"则获取当前系统服务器的时间，
      String req = new String(body, "UTF-8");
      System.out.println("The time server receive order : " + req);
      String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new java.util.Date(
          System.currentTimeMillis()).toString() : "BAD ORDER";
      //调用doWrite方法发送给客户端。
      doWrite(currentTime);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private void doWrite(String currentTime) {
    if (currentTime != null && currentTime.trim().length() > 0) {
      //首先对当前时间进行合法性校验，如果合法，调用字符串的解码方法将应答消息编码成字节数组，
      //然后将它复制到发送缓冲区writeBuffer中，
      byte[] bytes = (currentTime).getBytes();
      ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
      writeBuffer.put(bytes);
      writeBuffer.flip();
      //最后调用AsynchronousSocketChannel的异步write方法。
      //正如前面介绍的异步read方法一样，它也有三个与read方法相同的参数，
      //在本例程中我们直接实现write方法的异步回调接口CompletionHandler。
      channel.write(writeBuffer, writeBuffer,
          new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
              //对发送的writeBuffer进行判断，如果还有剩余的字节可写，说明没有发送完成，需要继续发送，直到发送成功。
              if (buffer.hasRemaining())
                channel.write(buffer, buffer, this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
              //关注下failed方法，它的实现很简单，就是当发生异常的时候，对异常Throwable进行判断，
              //如果是I/O异常，就关闭链路，释放资源，
              //如果是其他异常，按照业务自己的逻辑进行处理,如果没有发送完成，继续发送.
              //本例程作为简单demo，没有对异常进行分类判断，只要发生了读写异常，就关闭链路，释放资源。
              try {
                channel.close();
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
      this.channel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}