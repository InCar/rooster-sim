package com.incar.sim.net.reconnect;/**
 * Created by fanbeibei on 2017/8/9.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Fan Beibei
 * @Description: 接收服务端反馈的handler
 * @date 2017/8/9 16:12
 */
public class RecieveHandler  extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(RecieveHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;

        logger.info("resp from server:"+ByteBufUtil.hexDump(buf));
    }
}
