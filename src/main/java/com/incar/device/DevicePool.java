package com.incar.device;


import com.incar.TCP.TcpClient;
import com.incar.util.StringToHex;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/2.
 * 设备池
 */
public class DevicePool extends Transmitter {

    private static final Logger logger = Logger.getLogger(DevicePool.class);
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 需要循环的次数  小于0代表永久循环 0代表停止循环 ; 大于0代表需要循环的次数
     */
    private Integer circulationNum;

    /**
     * 要发送的数据源
     */
    private List<String> dataInfo;

    /**
     * 超时时间
     */
    private int time;

    /**
     * 是否能继续发送  true为继续发送  false为停止发送
     */
    private boolean isSend;

    public DevicePool(String deviceName, String deviceCode, Integer circulationNum, List<String> dataInfo,int time) {
        this.deviceName = deviceName;
        this.deviceCode = deviceCode;
        this.circulationNum = circulationNum;
        this.dataInfo = dataInfo;
        this.time = time;
        init();
    }

    /**
     * 初始化
     */
    public void init(){
        isSend = true;
        if (dataInfo== null || dataInfo.size() == 0){
            isSend = false;
        }
        if (circulationNum == null || circulationNum == 0){
            isSend = false;
        }
        setSentInfo(new ArrayList<String>());
    }
    /**
     * 取数据的方法
     */
    public String fetchData(){
        if (dataInfo.size() > 0){
            return dataInfo.get(0);
        } if (dataInfo.size() <= 0 ){
            if (circulationNum  < 0 || (circulationNum >0 && circulationNum<getIndex())){
                if (getSentInfo() != null && getSentInfo().size() >0){
                    dataInfo = getSentInfo();
                    setSentInfo(new ArrayList<String>());
                    setIndex(getIndex()+1);
                }else {
                    logger.info("deviceCode:"+"deviceCode;出现数据错误;已停止发送");
                    isSend = false;
                    return null;
                }
            }
        }
        return dataInfo.get(0);
    }

    /**
     * 数据转移到已发送
     */
    public void transferData(){
        String removeData = dataInfo.remove(0);
        getSentInfo().add(removeData);
    }

    /**
     * 发送数据
     */
    public void sendData(){
        //1.取数据
        String data = fetchData();
        if (data == null ){ //数据出现错误 则直接抛弃掉
            return;
        }
        try {
            //2.发送
            if (time >0){Thread.sleep(time*1000);}
            logger.info(deviceCode+":正在发送数据");
            TcpClient.sendMsg(StringToHex.getByteBuffer(data));
            //3.成功的话 则移除第一个数据  将数据添加到已发送数据中
            //不成功的话 则重新发送
            transferData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ;
    }


    @Override
    public void execute() {
        logger.info(deviceCode+":启动发送数据");
        while (true){
            if (isSend){
                sendData();
                //todo 发送信息
            }
        }
    }


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public List<String> getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(List<String> dataInfo) {
        this.dataInfo = dataInfo;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }


    public Integer getCirculationNum() {
        return circulationNum;
    }

    public void setCirculationNum(Integer circulationNum) {
        this.circulationNum = circulationNum;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
