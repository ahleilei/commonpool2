package commons.pool2.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {   
        
        byte[] b = msg.getBytes(Charset.forName("utf-8"));
        
        byte[] r = new byte[b.length+2];
        
        r[0] = (byte)(b.length>>8);
        
        r[1] = (byte)(b.length);
        
        System.arraycopy(b, 0, r, 2, b.length);
        
        out.writeBytes(r);
    }

}
