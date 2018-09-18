package net.neoremind.mycode.aio.time.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;


public class AsyncTimeServerHandler implements Runnable {

  CountDownLatch latch;

  AsynchronousServerSocketChannel asynchronousServerSocketChannel;

  public AsyncTimeServerHandler(int port) {
    //在构造方法中，我们首先创建一个异步的服务端通道AsynchronousServerSocketChannel，
    //然后调用它的bind方法绑定监听端口，如果端口合法且没被占用，绑定成功，打印启动成功提示到控制台。
    try {
      asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
      asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
      System.out.println("The time server is start in port : " + port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    //在线程的run方法中，初始化CountDownLatch对象，
    //它的作用是在完成一组正在执行的操作之前，允许当前的线程一直阻塞。
    //在本例程中，我们让线程在此阻塞，防止服务端执行完成退出。
    //在实际项目应用中，不需要启动独立的线程来处理AsynchronousServerSocketChannel，这里仅仅是个demo演示。
    latch = new CountDownLatch(1);
    doAccept();
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  //用于接收客户端的连接，由于是异步操作，
  //我们可以传递一个CompletionHandler＜AsynchronousSocketChannel,? super A＞类型的handler实例接收accept操作成功的通知消息，
  //在本例程中我们通过AcceptCompletionHandler实例作为handler来接收通知消息，
  public void doAccept() {
    System.out.println("The time server ready to accept request...");
    asynchronousServerSocketChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>() {
      @Override
      public void completed(AsynchronousSocketChannel result,
                            AsyncTimeServerHandler attachment) {
        //我们从attachment获取成员变量AsynchronousServerSocketChannel，然后继续调用它的accept方法。
        //在此可能会心存疑惑：既然已经接收客户端成功了，为什么还要再次调用accept方法呢？
        //原因是这样的：当我们调用AsynchronousServerSocketChannel的accept方法后，
        //如果有新的客户端连接接入，系统将回调我们传入的CompletionHandler实例的completed方法，
        //表示新的客户端已经接入成功，因为一个AsynchronousServerSocket Channel可以接收成千上万个客户端，
        //所以我们需要继续调用它的accept方法，接收其他的客户端连接，最终形成一个循环。
        //每当接收一个客户读连接成功之后，再异步接收新的客户端连接。
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        //链路建立成功之后，服务端需要接收客户端的请求消息，
        //创建新的ByteBuffer，预分配1M的缓冲区。
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //通过调用AsynchronousSocketChannel的read方法进行异步读操作。
        //下面我们看看异步read方法的参数。
        //ByteBuffer dst：接收缓冲区，用于从异步Channel中读取数据包；
        //A attachment：异步Channel携带的附件，通知回调的时候作为入参使用；
        //CompletionHandler＜Integer,? super A＞：接收通知回调的业务handler，本例程中为ReadCompletionHandler。
        result.read(buffer, buffer, new ReadCompletionHandler(result));
      }

      @Override
      public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
      }
    });
  }

}
