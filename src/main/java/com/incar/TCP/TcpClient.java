package com.incar.TCP;


import com.incar.util.ApplicationVariable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;


/**
 * Created by zhouyongbo on 2017/5/31.
 */
public class TcpClient {
    private static final Logger logger = Logger.getLogger(TcpClient.class);

    public static Bootstrap bootstrap = null;
    public static Channel channel = null;
    /**
     * 初始化Bootstrap
     * @return
     */
    public static final Bootstrap getBootstrap(){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast("decoder", new ByteArrayDecoder());
                pipeline.addLast("encoder", new ByteArrayEncoder());
                pipeline.addLast("handler", new TcpClientHandler());
            }
        });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    public static final Channel getChannel(String host,int port){
        Channel channel = null;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            logger.error(String.format("连接Server(IP[%s],PORT[%s])失败", host,port),e);
            return null;
        }
        return channel;
    }

    public static void sendMsg(Object msg) throws Exception {
        if(channel!=null){
            channel.writeAndFlush(msg).sync();
        }else{
            logger.warn("消息发送失败,连接尚未建立!");
        }
    }

//    public static void main(String[] args) throws Exception {
//        initHP("127.0.0.1",60000);
//        try {
////            long t0 = System.nanoTime();
////            for (int i = 0; i < 100000; i++) {
////                Thread.sleep(2000);
//            String s = "16 08 31 30 30 31 30 30 32 31 00 00 00 00 09 31 37 00 4C 53 56 46 56 36 31 38 33 44 32 32 30 30 39 33 32 00 32 30 31 34 2D 30 36 2D 32 35 20 31 35 3A 30 30 3A 31 37 00 00 00 40 00 00 31 34 2E 31 32 00 00 01 30 00 00 02 B9 D8 00 00 04 43 4C 00 00 05 2D 2D 2D 00 00 06 35 38 2E 38 00 00 07 38 37 00 00 08 2D 33 2E 31 00 00 0A 31 2E 36 00 00 11 36 31 00 00 12 31 37 34 32 00 00 13 31 34 00 00 14 31 38 00 00 15 35 33 00 00 17 31 39 2E 32 00 00 19 4F 32 53 31 32 20 7C 20 4F 32 53 31 31 00 00 1A 30 2E 39 33 30 00 00 1B 2D 37 2E 30 00 00 1C 31 2E 32 37 35 00 00 4A 45 4F 42 44 00 00 7D 33 35 00 00 7E 30 00 00 83 30 2E 30 00 00 84 30 2E 30 00 00 85 30 00 00 86 39 34 00 00 89 35 39 36 2E 38 00 00 8D CE DE D0 A7 00 00 8E CE DE D0 A7 00 00 8F D3 D0 D0 A7 00 00 90 CD EA B3 C9 00 00 91 CD EA B3 C9 00 00 92 CD EA B3 C9 00 00 93 CE DE D0 A7 00 00 94 CE DE D0 A7 00 00 95 CE DE D0 A7 00 00 96 CE DE D0 A7 00 00 97 CE DE D0 A7 00 00 98 CE DE D0 A7 00 00 99 CE DE D0 A7 00 00 9A CE DE D0 A7 00 00 9B CE B4 CD EA B3 C9 00 00 9C CE B4 CD EA B3 C9 00 00 9D CE B4 CD EA B3 C9 00 00 9E CE B4 CD EA B3 C9 00 00 9F CE B4 CD EA B3 C9 00 00 A0 CE B4 CD EA B3 C9 00 00 A1 CE B4 CD EA B3 C9 00 00 A2 CE B4 CD EA B3 C9 00 00 A3 31 34 2E 30 36 35 00 00 A4 33 38 2E 34 00 00 A5 31 2E 30 30 30 00 00 A6 31 31 2E 30 00 00 A7 32 37 00 00 A8 32 31 2E 36 00 00 AA 33 33 2E 37 00 00 AB 33 34 2E 35 00 00 AD 31 35 2E 33 00 00 B0 30 00 00 B1 30 00 00 B2 30 00 00 B3 30 00 00 B4 30 00 00 B5 47 41 53 00 ";
//            TcpClient.sendMsg(s);
////            }
////            long t1 = System.nanoTime();
////            System.out.println((t1-t0)/1000000.0);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public static void init(){
        bootstrap = getBootstrap();
        channel = getChannel(ApplicationVariable.getObjectiveIP(),ApplicationVariable.getObjectivePort());
        if (bootstrap == null || channel == null ){
            logger.info("TCP4连接初始化失败");
        }else {
            logger.info("TCP4连接初始化成功");
        }
    }

    private static void initHP(String host,Integer port){
        bootstrap = getBootstrap();
        channel = getChannel(host,port);
        if (bootstrap == null || channel == null ){
            logger.info("TCP4连接初始化失败");
        }else {
            logger.info("TCP4连接初始化成功");
        }
    }
}
