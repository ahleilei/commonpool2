package commons.pool2.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class Decoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        
        int length = msg.readableBytes();
        
        byte[] arr = new byte[length];
        
        msg.getBytes(msg.readerIndex(), arr, 0, length);
        
        out.add(new String(arr,"utf-8"));
    }

}
