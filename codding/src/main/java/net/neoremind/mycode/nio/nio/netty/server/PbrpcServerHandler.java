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
package net.neoremind.mycode.nio.nio.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.neoremind.mycode.nio.nio.netty.protocol.PbrpcMsg;
import net.neoremind.mycode.nio.nio.netty.serializer.PbrpcMessageDeserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbrpcServerHandler extends SimpleChannelInboundHandler<PbrpcMsg> {

    private static final Logger LOG = LoggerFactory.getLogger(PbrpcMessageDeserializer.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, PbrpcMsg msg) throws Exception {
        try {
            PbrpcMsg obj = (PbrpcMsg) msg;
            obj.setId(obj.getId() + 1);
            obj.setName(obj.getName().toUpperCase());
            LOG.info("Biz logic will return:" + obj);
            ctx.channel().writeAndFlush(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOG.info("Server channelReadComplete>>>>>>>>");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("Server channelActive>>>>>>>>");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
