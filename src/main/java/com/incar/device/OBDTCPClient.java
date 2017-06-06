package com.incar.device;

import com.incar.TCP.TcpClient;
import com.incar.util.ApplicationVariable;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by zhouyongbo on 2017/6/2.
 */
public abstract class OBDTCPClient implements Runnable{
    private static final Logger logger = Logger.getLogger(OBDTCPClient.class);

    private  Bootstrap bootstrap ;
    private  Channel channel ;
    /**
     * 已开启端口
     */
    private int port;


    private boolean isNormal = false;


    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    /**
     * 初始化Bootstrap
     * @return
     */
    public Bootstrap initBootstrap(){
        if (bootstrap != null ){
            return bootstrap;
        }
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new ByteArrayDecoder());
                pipeline.addLast("encoder", new ByteArrayEncoder());
                pipeline.addLast("handler", new TcpClientHandler());
            }
        });
        b.option(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    private class TcpClientHandler extends SimpleChannelInboundHandler<Object>{
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            logger.info("client接收到服务器返回的消息:"+msg);

        }

        /**
         * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
         *
         * @alia OneCoder
         * @author lihzh
         * @date 2013年11月16日 上午12:50:47
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

        }
    }

    public Channel initChannel(){
        if (this.channel != null  && channel.isOpen()  ){
            return channel;
        }
        Channel channel = null;
        try {
            channel = initBootstrap().connect(ApplicationVariable.getObjectiveIP(), ApplicationVariable.getObjectivePort()).sync().channel();
           port= ((InetSocketAddress) channel.localAddress()).getPort();
        } catch (Exception e) {
            logger.error(String.format("连接Server(IP[%s],PORT[%s])失败", ApplicationVariable.getObjectiveIP(),ApplicationVariable.getObjectivePort()),e);
            return null;
        }
        return channel;
    }

    /**
     * 0:发送失败 1发送成功
     * @param msg
     * @return
     */
    protected int sendMsg(Object msg){
        if (channel == null || !channel.isOpen() ){
            channel = initChannel();
        }
        if(channel!=null){
            try {
                channel.writeAndFlush(msg).sync();
                return 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            }

        }else{
            logger.warn("消息发送失败,连接尚未建立!");
            return 0;
        }
    }

    public void run() {
            execute();
    }


    /**
     * 获取执行数据
     */
    public abstract void execute();

    protected void tcpInit(){
        if (bootstrap == null ){
            bootstrap = initBootstrap();
        }
       if (channel == null ){
           channel = initChannel();
       }
        if (bootstrap == null || channel == null ){
            logger.info("TCP4连接初始化失败");
            isNormal = false;
        }else {
            logger.info("TCP4连接初始化成功");
            isNormal = true;
        }
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
