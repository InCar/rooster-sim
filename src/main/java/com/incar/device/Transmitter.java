package com.incar.device;

import com.incar.TCP.TcpClient;
import com.incar.util.StringToHex;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/2.
 * 发送器
 */
public abstract class Transmitter {
    private static final Logger logger = Logger.getLogger(Transmitter.class);
    /**
     * 已发送信息
     */
    private List<String> sentInfo;

    /**
     * 轮循的次数
     */
    private int index = 0;


    public List<String> getSentInfo() {
        return sentInfo;
    }

    public void setSentInfo(List<String> sentInfo) {
        this.sentInfo = sentInfo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
