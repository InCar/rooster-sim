package com.incar.sim.net.reconnect;

import com.incar.sim.net.NetClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 自動重連的客戶端
 *
 * @author fanbeibei
 */
public class Netty4Client implements NetClient{
    private static final Logger log = LogManager.getLogger(Netty4Client.class);

    /**
     * 服务端地址
     */
    private String host;
    /**
     * 服务端端口
     */
    private int port;

    private EventLoopGroup eventLoop = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();

    private ReConnectHandler reConnectHandler;
    private ReconnectListener reconnectListener;
    private Channel channel;


    public Netty4Client() {
        this("127.0.0.1", 17777);
    }

    public Netty4Client(String host, int port) {
        this.host = host;
        this.port = port;
        reConnectHandler = new ReConnectHandler(this);
        reconnectListener = new ReconnectListener(this);
    }

    @Override
    public void start() {
        try {
            bootstrap.group(eventLoop);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(reConnectHandler);
                    socketChannel.pipeline().addLast();
                }

            });

            bootstrap.remoteAddress(host, port);
            ChannelFuture f = bootstrap.connect().addListener(reconnectListener).sync();

            channel = f.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接
     */
    @Override
    public void reConnect() {
        //重新连接必须重启创建Bootstrap
        bootstrap = new Bootstrap();
        start();
    }

    @Override
    public void close() {
        eventLoop.shutdownGracefully();
    }

    /**
     * 发送消息
     *
     * @param pack
     * @return
     */
    @Override
    public boolean sendPack(byte[] pack) {
        if (!channel.isActive()) {
            return false;
        }

        channel.writeAndFlush(pack);
        return true;
    }


    /**
     * 客户端是否已连接
     *
     * @return
     */
    @Override
    public boolean isActive() {
        return null != channel && channel.isActive();
    }

    public static void main(String[] args) {
        new Netty4Client().start();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
