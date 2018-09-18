package net.neoremind.mycode.aio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class AIOEchoClient {

  public static void main(String[] args) throws IOException {

    final AsynchronousSocketChannel client = AsynchronousSocketChannel.open();

    InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1",8001);

    CompletionHandler<Void, ? super Object> handler = new CompletionHandler<Void,Object>(){

      @Override
      public void completed(Void result, Object attachment) {
        client.write(ByteBuffer.wrap("hello".getBytes()),null,
            new CompletionHandler<Integer,Object>(){

              @Override
              public void completed(Integer result,
                                    Object attachment) {
                final ByteBuffer buffer = ByteBuffer.allocate(32);
                client.read(buffer,buffer,new CompletionHandler<Integer,ByteBuffer>(){

                  @Override
                  public void completed(Integer result,
                                        ByteBuffer attachment) {
                    buffer.flip();
                    final CharBuffer decode = Charset.defaultCharset().decode(buffer);
                    System.out.println("received: " + decode.toString());
                    try {
                      client.close();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  }

                  @Override
                  public void failed(Throwable exc,
                                     ByteBuffer attachment) {
                    exc.printStackTrace();
                  }

                });
              }

              @Override
              public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
              }

            });
      }

      @Override
      public void failed(Throwable exc, Object attachment) {
        exc.printStackTrace();
      }

    };

    client.connect(serverAddress, null, handler);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
