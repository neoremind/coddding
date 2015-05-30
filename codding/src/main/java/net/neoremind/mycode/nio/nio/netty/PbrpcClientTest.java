package net.neoremind.mycode.nio.nio.netty;

import net.neoremind.mycode.nio.nio.netty.client.PbrpcClient;
import net.neoremind.mycode.nio.nio.netty.protocol.PbrpcMsg;

public class PbrpcClientTest {

    public static void main(String[] args) throws Exception {
        PbrpcClient client = new PbrpcClient();
        client.connect("127.0.0.1", 8088);
        PbrpcMsg msg = new PbrpcMsg();
        msg.setId(100);
        msg.setName("zhangxu");
        client.doTransport(msg);
    }
    
}
