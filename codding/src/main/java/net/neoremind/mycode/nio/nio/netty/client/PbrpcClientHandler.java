/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.neoremind.mycode.nio.nio.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.neoremind.mycode.nio.nio.netty.protocol.PbrpcMsg;
import net.neoremind.mycode.nio.nio.netty.serializer.PbrpcMessageDeserializer;

public class PbrpcClientHandler extends SimpleChannelInboundHandler<PbrpcMsg> {

    private static final Logger LOG = LoggerFactory.getLogger(PbrpcMessageDeserializer.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, PbrpcMsg msg) throws Exception {
        // ByteBuf in = (ByteBuf) msg; // (1)
        // System.out.println("Get back from server msg:");
        // while (in.isReadable()) { // (1)
        // System.out.print((char) in.readByte());
        // }
        LOG.info("Got msg from server:" + msg);
        ctx.fireChannelReadComplete();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOG.info("Client channelReadComplete>>>>>");
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("Client channelActive>>>>>");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // ctx.close();
        ctx.fireChannelRead("err");
    }

}
