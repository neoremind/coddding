package net.neoremind.mycode.nio.nio.netty.serializer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.neoremind.mycode.nio.nio.netty.protocol.NsHead;
import net.neoremind.mycode.nio.nio.netty.protocol.PbrpcMsg;
import net.neoremind.mycode.util.JsonMapper;

public class PbrpcMessageSerializer extends MessageToMessageEncoder<PbrpcMsg> {

    private static final Logger LOG = LoggerFactory.getLogger(PbrpcMessageSerializer.class);

    private JsonMapper jsonMapper = JsonMapper.buildNormalMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, PbrpcMsg msg, List<Object> out)
            throws Exception {
        PbrpcMsg obj = (PbrpcMsg) msg;

        String jsonStr = jsonMapper.toJson(obj);
        byte[] bodyBytes = jsonStr.getBytes();

        NsHead nsHead = new NsHead();
        nsHead.setBodyLen(bodyBytes.length);
        nsHead.setProvider("beidou");
        nsHead.toBytes();

        LOG.info("Send total byte size=" + (nsHead.toBytes().length + bodyBytes.length)
                + ", NsHead size=" + nsHead.toBytes().length + ", body size=" + bodyBytes.length);
        ByteBuf encodedMessage = Unpooled.copiedBuffer(nsHead.toBytes(), bodyBytes);
        out.add(encodedMessage);
    }

}
