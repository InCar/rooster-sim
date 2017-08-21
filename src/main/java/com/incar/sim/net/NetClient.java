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
     * 启动
     */
    public void start();

    /**
     * 关闭
     */
    public void close();


    /**
     * 连接服务端
     */
    void reConnect();

    /**
     * 发送数据包
     *
     * @param pack 数据
     *
     * @return  true 发送成功，false  发送失败
     */
    boolean sendPack(byte[] pack);

    /**
     * 客户端是否已连接
     *
     * @return
     */
    boolean isActive();
}
