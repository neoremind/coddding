package net.neoremind.mycode.aio.time.server;

import java.io.IOException;

/**
 * 例子来自于https://www.cnblogs.com/wade-luffy/p/6165233.html
 */
public class TimeServer {

  public static void main(String[] args) throws IOException {
    int port = 8080;
    if (args != null && args.length > 0) {
      try {
        port = Integer.valueOf(args[0]);
      } catch (NumberFormatException e) {
        // 采用默认值
      }
    }
    //首先创建异步的时间服务器处理类，然后启动线程将AsyncTimeServerHandler启动
    AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
    new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
  }
}
