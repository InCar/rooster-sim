package com.incar.TCP;

import com.incar.util.OBDRunParameter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = Logger.getLogger(TcpClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //messageReceived方法,名称很别扭，像是一个内部方法.
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
        new OBDRunParameter().returnTCP();
//            channel.writeAndFlush("你好").sync();
    }
}
