package net.neoremind.mycode.thrift;

import com.google.common.collect.Lists;
import net.neoremind.mycode.thrift.codegen.MyQueryService;
import net.neoremind.mycode.thrift.codegen.QueryRequest;
import net.neoremind.mycode.thrift.codegen.QueryResponse;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author xu.zhang
 */
public class MyClientDemo {

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8089;
    public static final int SOCKET_TIMEOUT = 30000;
    public static final int CONN_TIMEOUT = 30000;

    public void callEcho() {
        try (TTransport transport = new TSocket(SERVER_IP, SERVER_PORT, SOCKET_TIMEOUT, CONN_TIMEOUT)) {
            TProtocol protocol = new TBinaryProtocol(transport);
            // TProtocol protocol = new TCompactProtocol(transport);
            // TProtocol protocol = new TJSONProtocol(transport);
            MyQueryService.Client client = new MyQueryService.Client(protocol);
            transport.open();
            String result = client.echo("abc");
            System.out.println("response is " + result);
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public void callEchoInt() {
        try (TTransport transport = new TSocket(SERVER_IP, SERVER_PORT, SOCKET_TIMEOUT, CONN_TIMEOUT)) {
//            TProtocol protocol = new TBinaryProtocol(transport);
            TProtocol protocol = new TCompactProtocol(transport);
            // TProtocol protocol = new TJSONProtocol(transport);
            MyQueryService.Client client = new MyQueryService.Client(protocol);
            transport.open();
            int result = client.echoInt(100);
            System.out.println("response is " + result);
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public void callHelloVoid() {
        try (TTransport transport = new TSocket(SERVER_IP, SERVER_PORT, SOCKET_TIMEOUT, CONN_TIMEOUT)) {
//            TProtocol protocol = new TBinaryProtocol(transport);
            TProtocol protocol = new TCompactProtocol(transport);
            // TProtocol protocol = new TJSONProtocol(transport);
            MyQueryService.Client client = new MyQueryService.Client(protocol);
            transport.open();
            client.helloVoid();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public void callSubmitQuery() {
        try (TTransport transport = new TSocket(SERVER_IP, SERVER_PORT, SOCKET_TIMEOUT, CONN_TIMEOUT)) {
//            TProtocol protocol = new TBinaryProtocol(transport);
            TProtocol protocol = new TCompactProtocol(transport);
            // TProtocol protocol = new TJSONProtocol(transport);
            MyQueryService.Client client = new MyQueryService.Client(protocol);
            transport.open();
            QueryRequest req = new QueryRequest();
            req.setId(123);
            req.setName("haha");
            req.setQuiet(false);
            req.setOptList(Lists.newArrayList("jack", "neo"));
            QueryResponse res = client.submitQuery(req);
            System.out.println(res);
        } catch (TException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MyClientDemo demo = new MyClientDemo();
        demo.callEcho();
        demo.callEchoInt();
        demo.callHelloVoid();
        demo.callSubmitQuery();
    }

}

