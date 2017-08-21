package com.incar.sim.net.reconnect;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

/**
 * 监听连接是否成功的监听器。不成功则重连
 */
public class ReconnectListener implements ChannelFutureListener {
	private Netty4Client client;

	public ReconnectListener(Netty4Client reConnectClient) {
		if (null == reConnectClient) {
			throw new IllegalArgumentException();
		}
		this.client = reConnectClient;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (!future.isSuccess()) {

			System.out.println("Reconnect");

			final EventLoop loop = future.channel().eventLoop();
			loop.schedule(new Runnable() {
				@Override
				public void run() {
					client.reConnect();
				}

			}, 1L, TimeUnit.SECONDS);

		} else {
			System.out.println("connect successfully to   " + client.getHost() + ":" + client.getPort());
		}
	}

}
