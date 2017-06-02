package com.incar.device;


import com.incar.TCP.TcpClient;
import com.incar.util.ApplicationVariable;
import com.incar.util.StringToHex;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/2.
 * 设备池
 */
public class DevicePool extends OBDTCPClient {

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
    private Integer circulationNum = ApplicationVariable.getCirculationNum();

    /**
     * 要发送的数据源
     */
    private List<String> dataInfo;

    /**
     * 超时时间
     */
    private int time =  ApplicationVariable.getTime();

    /**
     * 是否能继续发送  true为继续发送  false为停止发送
     */
    private boolean isSend;

    /**
     * 是否共享TCP连接
     */
    private boolean isShareTCP =  ApplicationVariable.getIsShareTCP();

    public DevicePool(String deviceName, String deviceCode, List<String> dataInfo) {
        this.deviceName = deviceName;
        this.deviceCode = deviceCode;
        this.dataInfo = dataInfo;
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
        if (!isShareTCP){
            tcpInit();
        }
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
     *启动
     */
    public void start(){
        new Thread(this).start();
    }

    public Object getMsg() {
        //1.取数据
        String data = fetchData();
        if (data == null || "".equals(data)){ //数据出现错误 则直接抛弃掉
            return null;
        }
        byte[] byteBuffer = StringToHex.getByteBuffer(data);

        return byteBuffer;
    }

    public void execute() throws Exception {
        logger.info(deviceCode+":启动发送数据");
        while (true){
            if (isSend){
                Object msg = getMsg();
                if (time >0){Thread.sleep(time*1000);}
                logger.info(deviceCode+":正在发送数据");
                if (isShareTCP){
                    TcpClient.sendMsg(msg);
                }else {
                    sendMsg(msg);
                }
                transferData();
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


}
