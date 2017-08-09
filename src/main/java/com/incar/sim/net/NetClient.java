package com.incar.sim.net;/**
 * Created by fanbeibei on 2017/8/9.
 */

/**
 * @author Fan Beibei
 * @Description: 网络客户端
 * @date 2017/8/9 10:34
 */
public interface NetClient {
    /**
     * 连接服务端
     *
     * @param addr
     * @param port
     */
    void connect(String addr,int port);

    /**
     * 发送数据包
     *
     * @param pack
     */
    void sendPack(byte[] pack);


}
