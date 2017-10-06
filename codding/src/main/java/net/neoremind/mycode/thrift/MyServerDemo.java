package net.neoremind.mycode.thrift;

import net.neoremind.mycode.thrift.codegen.MyQueryService.Processor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * 服务端编码基本步骤：
 * <p>
 * 实现服务处理接口impl
 * 创建TServerTransport
 * 创建TProcessor
 * 创建TProtocol
 * 创建TServer
 * 启动Server
 * <p>
 * 数据传输协议：
 * TBinaryProtocol : 二进制格式.
 * TCompactProtocol : 压缩格式
 * TJSONProtocol : JSON格式
 * TSimpleJSONProtocol : 提供JSON只写协议, 生成的文件很容易通过脚本语言解析
 *
 * @author xu.zhang
 */
public class MyServerDemo {

    /**
     * blocking io单线程
     */
    public void startSimpleBlockingIOServer() {
        try {
            TServerSocket serverTransport = new TServerSocket(8089);
            Processor<MyQueryServiceImpl> process = new Processor<>(new MyQueryServiceImpl());
            TServer.Args args = new TServer.Args(serverTransport);
            args.processor(process);
            args.protocolFactory(new TBinaryProtocol.Factory());
            // args.protocolFactory(new TCompactProtocol.Factory());
            // args.protocolFactory(new TJSONProtocol.Factory());
            TServer server = new TSimpleServer(args);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * blocking io线程池
     */
    public void startThreadPooledBlockingIOServer() {
        try {
            TServerSocket serverTransport = new TServerSocket(8089);
            Processor<MyQueryServiceImpl> process = new Processor<>(new MyQueryServiceImpl());
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
            args.processor(process);
            args.protocolFactory(new TBinaryProtocol.Factory());
            // args.protocolFactory(new TCompactProtocol.Factory());
            // args.protocolFactory(new TJSONProtocol.Factory());
            TServer server = new TThreadPoolServer(args);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reactor模式，NIO，单线程处理IO和业务逻辑
     */
    public void startNonBlockingIOServer() {
        try {
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(8089);
            Processor<MyQueryServiceImpl> process = new Processor<>(new MyQueryServiceImpl());
            TNonblockingServer.Args args = new TNonblockingServer.Args(tnbSocketTransport);
            args.processor(process);
            // args.protocolFactory(new TBinaryProtocol.Factory());
            args.protocolFactory(new TCompactProtocol.Factory());
            // args.protocolFactory(new TJSONProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            TServer server = new TNonblockingServer(args);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reactor模式，NIO，Reactor线程池处理IO，只有一个，后端线程池worker pool做业务逻辑
     */
    public void startHsHaNonBlockingIOServer() {
        try {
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(8089);
            Processor<MyQueryServiceImpl> process = new Processor<>(new MyQueryServiceImpl());
            THsHaServer.Args args = new THsHaServer.Args(tnbSocketTransport);
            args.processor(process);
            // args.protocolFactory(new TBinaryProtocol.Factory());
            args.protocolFactory(new TCompactProtocol.Factory());
            // args.protocolFactory(new TJSONProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            // worker pool size
            args.minWorkerThreads(10);
            args.maxWorkerThreads(20);
            TServer server = new THsHaServer(args);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reactor模式，NIO，MainReactor线程池处理accept连接，有一个，SubReactor做IO读写的叫做selector threads，
     * 可以设置SubReactor线程池大小，后端线程池worker pool做业务逻辑。
     */
    public void startTThreadedSelectorServer() {
        try {
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(8089);
            Processor<MyQueryServiceImpl> process = new Processor<>(new MyQueryServiceImpl());
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(tnbSocketTransport);
            args.processor(process);
            // args.protocolFactory(new TBinaryProtocol.Factory());
            args.protocolFactory(new TCompactProtocol.Factory());
            // args.protocolFactory(new TJSONProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            args.selectorThreads(4);
            args.workerThreads(20);
            args.acceptQueueSizePerThread(1000);
            TServer server = new TThreadedSelectorServer(args);
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyServerDemo server = new MyServerDemo();
        server.startSimpleBlockingIOServer();
        server.startThreadPooledBlockingIOServer();
        server.startNonBlockingIOServer();
        server.startHsHaNonBlockingIOServer();
        server.startTThreadedSelectorServer();
    }

}
