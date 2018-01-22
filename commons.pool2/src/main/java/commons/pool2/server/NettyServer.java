package commons.pool2.server;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 
 * 北京掌中彩信息科技有限公司源代码，版权归北京掌中彩信息科技有限公司所有。
 * 
 * 项目名称 : commons.pool2
 * 创建日期 : 2018年1月19日
 * 类  描  述 : netty server
 * 修改历史 : 
 *     1. [2018年1月19日]创建文件 by ziqiang.zhang
 */
public class NettyServer {

    private final static Logger log = LoggerFactory.getLogger(NettyServer.class);

    public void start(String ip, int port) {

        EventLoopGroup boss = new NioEventLoopGroup();

        EventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();

        try {

            b.group(boss, work).channel(NioServerSocketChannel.class);

            b.option(ChannelOption.SO_BACKLOG, 1024);

            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline cp = ch.pipeline();

                    cp.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));

                    cp.addLast("pack decoder", new Decoder());

//                    cp.addLast("frameencoder", new LengthFieldPrepender(2));

                    cp.addLast("pack encoder", new Encoder());

                    cp.addLast(new ServerHandler());
                }
            });

            ChannelFuture f = b.bind(ip, port).sync();

            f.channel().closeFuture().sync();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

    class ServerHandler extends ChannelHandlerAdapter {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            log.info("ServerHandler.exceptionCaught " + cause.getMessage());

            ctx.close();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            log.info("ServerHandler.channelRead " + msg);
                  
            String resp = "999";
            
            if("100".equals(msg)){
                resp = "100";
            }else if("103".equals(msg)){
                resp = UUID.randomUUID().toString();
            }else if("200".equals(msg)){
                resp = "200";
            }
            
            log.info("ServerHandler.channelRead " + resp);
            
            ctx.writeAndFlush(resp);
        }
    }

    public static void main(String[] args) {

        new NettyServer().start("127.0.0.1", 8080);

    }

}
