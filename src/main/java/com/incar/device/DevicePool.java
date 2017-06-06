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

    public DevicePool(String deviceName, String deviceCode) {
        this.deviceName = deviceName;
        this.deviceCode = deviceCode;
        init();
    }

    /**
     * 初始化
     */
    public void init(){
        isSend = true;
        //初始化数据
        dataInfo =  new DataService().getData(deviceCode);
        if (dataInfo== null || dataInfo.size() == 0){
            logger.info("code:"+deviceCode+";无数据");
            isSend = false;
            return;
        }
        if (circulationNum == null || circulationNum == 0){
            isSend = false;
            return;
        }
        setSentInfo(new ArrayList<String>());
        if (!isShareTCP){
            tcpInit();
        }else {
            setNormal(ApplicationVariable.getStartTheReady());
        }

    }
    /**
     * 取数据的方法
     */
    public String fetchData(){
        if (dataInfo.size() > 0){
            return dataInfo.get(0);
        }else if (dataInfo.size() <= 0 ){
            if (circulationNum  < 0 || (circulationNum >=0 && getIndex()<circulationNum)){
                if (getSentInfo() != null && getSentInfo().size() >0){
                    dataInfo = getSentInfo();
                    setSentInfo(new ArrayList<String>());
                    setIndex(getIndex()+1);
                    logger.info("deviceCode:"+deviceCode+";数据轮循"+getIndex()+"次;");
                    return dataInfo.get(0);
                }else {
                    logger.info("deviceCode:"+deviceCode+";出现数据错误;已停止发送");
                    isSend = false;
                    return null;
                }
            }else {
                isSend = false;
                return null;
            }
        }
        return null;
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
     * 0:启动成功
     * 1:启动失败
     */
    public int start(){
        if (isNormal()){
            new Thread(this).start();
            return 0;
        }else {
            return 1;
        }
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

    public void execute() {
        Long start = System.currentTimeMillis();
        logger.info(deviceCode+":启动发送数据");
        while (isSend){
                Object msg = getMsg();

//                logger.info(deviceCode+":正在发送数据");
            if (msg!=null){
                try{
                    if (time >0){Thread.sleep(time*1000);}
                    if ("INCAR10001168643".equals(deviceCode)){
                        logger.debug("code:"+deviceCode+";正在发送数据");
                    }
//                    logger.info("code:"+deviceCode+";正在发送数据");
                    if (isShareTCP){
                        TcpClient.sendMsg(msg);
                    }else {
                        sendMsg(msg);
                    }
                    transferData(); // 如果与主机断开连接等异常 则重发
                }catch (Exception e){
                    e.printStackTrace();
                    logger.info("异常状况:"+"重发");
                }
            }
        }
        logger.info(deviceCode+":数据发送完毕;"+"耗时:"+(System.currentTimeMillis() - start)/1000+"秒");
//        if (getChannel() == null) {
//            getChannel().close();
//        }
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
