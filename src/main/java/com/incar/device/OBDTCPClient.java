package com.incar.device;

import com.incar.util.ApplicationVariable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.log4j.Logger;

/**
 * Created by zhouyongbo on 2017/6/2.
 */
public abstract class OBDTCPClient extends Transmitter implements Runnable{
    private static final Logger logger = Logger.getLogger(OBDTCPClient.class);

    private  Bootstrap bootstrap ;
    private  Channel channel ;

    /**
     * 初始化Bootstrap
     * @return
     */
    public Bootstrap initBootstrap(){
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

    public Channel getChannel(String host,int port){
        Channel channel = null;
        try {
            channel = bootstrap.connect(host, port).sync().channel();
        } catch (Exception e) {
            logger.error(String.format("连接Server(IP[%s],PORT[%s])失败", host,port),e);
            return null;
        }
        return channel;
    }

    protected void sendMsg(Object msg){
        if(channel!=null){
            try {
                channel.writeAndFlush(msg).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            logger.warn("消息发送失败,连接尚未建立!");
        }
    }

    public void run() {
        try {
            execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取执行数据
     */
    public abstract void execute() throws Exception;

    protected void tcpInit(){
        bootstrap = initBootstrap();
        channel = getChannel(ApplicationVariable.getObjectiveIP(),ApplicationVariable.getObjectivePort());
        if (bootstrap == null || channel == null ){
            logger.info("TCP4连接初始化失败");
        }else {
            logger.info("TCP4连接初始化成功");
        }
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
