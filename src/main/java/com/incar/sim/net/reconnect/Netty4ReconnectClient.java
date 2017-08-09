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

/**
 * 
 * 自動重連的客戶端
 * 
 * @author fanbeibei
 *
 */
/**
 * @author fanbeibei
 *
 */
public class Netty4ReconnectClient{
//	private static  Logger

	private String host;
	private int port;

	private EventLoopGroup eventLoop = new NioEventLoopGroup();
	private Bootstrap bootstrap = new Bootstrap();
	private ReConnectHandler reConnectHandler;
	private ReconnectListener reconnectListener;
	private Channel channel;
	

	public Netty4ReconnectClient() {
		this("127.0.0.1", 17777);
	}

	public Netty4ReconnectClient(String host, int port) {
		this.host = host;
		this.port = port;
		reConnectHandler = new ReConnectHandler(this);
		reconnectListener = new ReconnectListener(this);
	}

	public void start() {
		try {
			bootstrap.group(eventLoop);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(reConnectHandler);
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

	public void reStart() {
		bootstrap = new Bootstrap();
		start();
	}
	
	public void close(){
		eventLoop.shutdownGracefully();
	}
	
	/**
	 * 发送消息
	 * 
	 * 
	 * @param msg
	 * @return
	 */
	public boolean send(String msg){
		if (!channel.isActive()) {
			return false; 
		}
		
		channel.writeAndFlush(msg);
		return true;
	}
	
	
	/**
	 * 客户端是否已连接
	 * 
	 * @return
	 */
	public boolean isActive(){
		return null != channel  && channel.isActive();
	}

	public static void main(String[] args) {
		new Netty4ReconnectClient().start();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
