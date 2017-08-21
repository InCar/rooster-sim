package com.incar.sim.net.reconnect;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * 处理channelInactive事件的handler，channelInactive事件发生后重新连接
 */
@Sharable
public class ReConnectHandler extends ChannelInboundHandlerAdapter{
	private static final Logger logger = LogManager.getLogger(ReConnectHandler.class);
	private Netty4Client client;
	
	public ReConnectHandler(Netty4Client reConnectClient) {
		if (null == reConnectClient) {
			throw new IllegalArgumentException();
		}
		this.client = reConnectClient;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {//通讯中突然断开连接则停1秒后重连
		System.out.println("Reconnect");
		final EventLoop eventLoop = ctx.channel().eventLoop();
		eventLoop.schedule(new Runnable() {

			@Override
			public void run() {
				client.reConnect();
			}

		}, 1L, TimeUnit.SECONDS);
		
		super.channelInactive(ctx);
	}

}
