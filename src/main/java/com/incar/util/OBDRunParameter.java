package com.incar.util;

import com.incar.Application;
import com.incar.TCP.TcpClient;
import com.incar.repository.OBDRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by zhouyongbo on 2017/6/1.
 */
@Component
public class OBDRunParameter implements EnvironmentAware {
    private static final Logger logger = Logger.getLogger(OBDRunParameter.class);



    @Autowired
    private OBDRepository obdRepository;

    public OBDRunParameter() {
    }

    public void init(){
        //校验 启动参数
        logger.info("启动参数校验");
        initTCP();
    }

    /**
     * 初始化TCP
     */
    private void initTCP(){
        logger.info("进行TCP连接校验");
        if (ApplicationVariable.getObjectivePort()  == null ||  ApplicationVariable.getObjectivePort()<= 0){
            ApplicationVariable.setObjectivePort(60000);
        }
        if (ApplicationVariable.getObjectiveIP() == null ){
            ApplicationVariable.setObjectiveIP("127.0.0.1");
        }
        TcpClient.init();
    }

    /**
     * 初始化TCP返回的结果
     */
    public void returnTCP(){
        logger.info("TCP连接成功");
        logger.info("TCP:objectiveIP-"+ ApplicationVariable.getObjectiveIP()+ ";objectivePort-"+ApplicationVariable.getObjectivePort());
        initOBDCodes();
    }

    /**
     * 初始化模拟设备 当没有可用的模拟设备时候 则不会像目标地址发送东西
     */
    private void initOBDCodes(){
        logger.info("初始化模拟设备");
        //设置模拟设备
        String obdCodes = ApplicationVariable.getObdCodes();
        if (obdCodes == null) {
            ApplicationVariable.setObdCodes("ALL");
        } else {
            ApplicationVariable.setObdCodes(obdCodes);
        }
        otherParameter();
    }

    private void otherParameter(){
        //间隔时间
        Integer time = ApplicationVariable.getTime();
        if (time == null || time < 0) ApplicationVariable.setTime(1);
        logger.info("间隔时间校验完成");
        //数据
        Integer days = ApplicationVariable.getDays();
        if (days == null || days == 0){
            ApplicationVariable.setDays(null);
        }
        logger.info("数据设置校验完成");
    }

    @Override
    public void setEnvironment(Environment environment) {
         String days = environment.getProperty("sim.days");
         String time = environment.getProperty("sim.time");
         String objectiveIP = environment.getProperty("sim.objective.IP");
         String objectivePort = environment.getProperty("sim.objective.port");
         String obdCodes = environment.getProperty("sim.obdCodes");

        try{
            ApplicationVariable.setDays(Integer.valueOf(days));
            ApplicationVariable.setObdCodes(obdCodes);
            ApplicationVariable.setTime(Integer.valueOf(time));
            ApplicationVariable.setObjectiveIP(objectiveIP);
            ApplicationVariable.setObjectivePort(Integer.valueOf(objectivePort));
        }catch (Exception e){

        }
    }
}
