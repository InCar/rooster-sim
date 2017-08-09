package com.incar.sim.net.reconnect;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

@Sharable
public class ReConnectHandler extends ChannelInboundHandlerAdapter{
	
	private Netty4ReconnectClient client;
	
	public ReConnectHandler(Netty4ReconnectClient reConnectClient) {
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
				client.reStart();
			}

		}, 1L, TimeUnit.SECONDS);
		
		super.channelInactive(ctx);
	}

}
