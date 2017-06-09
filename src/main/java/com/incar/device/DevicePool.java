package com.incar.device;


import com.incar.TCP.TcpClient;
import com.incar.threads.ThreadPool;
import com.incar.util.ApplicationVariable;
import com.incar.util.StringToHex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/2.
 * 设备池
 */
public class DevicePool implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DevicePool.class);

    /**
     * 发送器
     */
    private OBDTCPClient obdtcpClient;

    /**
     * 已发送信息
     */
    private List<String> sentInfo;

    /**
     * 轮循的次数
     */
    private int index = 1;
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
        if (dataInfo == null )dataInfo =  new DataService().getData(deviceCode);
        if (dataInfo== null || dataInfo.size() == 0){
            logger.info("code:"+deviceCode+";无数据");
            isSend = false;
            return;
        }

        if (circulationNum == null || circulationNum == 0){
            isSend = false;
            return;
        }
        if (getSentInfo()==null) setSentInfo(new ArrayList<String>());

        if (!isShareTCP){
            obdtcpClient = new OBDTCPClient();
            obdtcpClient.tcpInit();
        }else {
            obdtcpClient.setNormal(ApplicationVariable.getStartTheReady());
            obdtcpClient.setPort(TcpClient.port);
        }

    }
    /**
     * 取数据的方法
     */
    public String fetchData(){
        if (dataInfo.size() > 0){
            return dataInfo.get(0);
        }else if (dataInfo.size() <= 0 ){
            if (circulationNum  <= 0 || (circulationNum >0 && index<circulationNum)){
                if (sentInfo != null && sentInfo.size() >0){
                    dataInfo = sentInfo;
                    sentInfo = new ArrayList<String>();
                    logger.info("deviceCode:"+deviceCode+";数据已发送"+index+"次;");
                    index ++;
                    return dataInfo.get(0);
                }else {
                    logger.info("deviceCode:"+deviceCode+";出现数据错误;已停止发送");
                    isSend = false;
                    return null;
                }
            }else if (index == circulationNum){
                logger.info("deviceCode:"+deviceCode+";数据已发送"+index+"次;");
                logger.info("deviceCode:"+deviceCode+";数据发送完毕;");
                dataInfo = sentInfo;
                sentInfo = new ArrayList<String>();
                isSend = false;
                return null;
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
        if ( isSend && obdtcpClient.isNormal()){
            ThreadPool.scheduledThreadPool(this);
            return 0;
        }else {
            logger.error("deviceCode:"+deviceCode+";参数校验失败;无法启动该模拟器");
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


    @Override
    public void run() {
        execute();
    }

    public synchronized void execute() {
        if (isSend){
            logger.info(deviceCode+":启动发送数据");
            Thread thread = Thread.currentThread();
            logger.info("线程名称:"+ thread.getName()+ ";线程ID:"+thread.getId());
        }
        int retriesNumber = 0;
        while (isSend){
                Object msg = getMsg();
                if (msg!=null){
                    try{
                        if (time >0){Thread.sleep(time*1000);}
//                        logger.info("code:"+deviceCode+";port:"+obdtcpClient.getPort()+";正在发送数据");
                        if (isShareTCP){
                            TcpClient.sendMsg(msg);
                        }else {
                            obdtcpClient.sendMsg(msg);
                        }
                        retriesNumber = 0;
                        transferData(); // 如果与主机断开连接等异常 则重发
                    }catch (Exception e){
                        e.printStackTrace();
                        retriesNumber ++ ;
                        //连续3次异常之后 停止
                        if (retriesNumber>= 3){
                            isSend = false;
                            logger.info("发包异常:"+"第"+retriesNumber+"次;终止重发;" );
                        }else {
                            logger.info("发包异常:"+"第"+retriesNumber+"次" );
                        }
                        //2秒之后重试
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    }
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


    public OBDTCPClient getObdtcpClient() {
        return obdtcpClient;
    }

    public void setObdtcpClient(OBDTCPClient obdtcpClient) {
        this.obdtcpClient = obdtcpClient;
    }
}
