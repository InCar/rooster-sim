package com.incar.util;

import com.incar.TCP.TcpClient;
import com.incar.gradleTask.TaskUtil;
import com.incar.repository.OBDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/1.
 * 启动初始化参数
 */
@Component
public class OBDRunParameter implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(OBDRunParameter.class);

    private static OBDRepository obdRepository;

    //是否初始化过参数
    private static boolean initParent = false;

    public OBDRunParameter() {
    }

    public void init(){
        //校验 启动参数
        logger.info("启动参数校验");
        initTCP();
    }

    public void init(OBDRepository obdRepository){
        this.obdRepository = obdRepository;
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
        if (!initParent){
            initParent = true;
            initOBDCodes();
        }
    }

    /**
     * 初始化模拟设备 当没有可用的模拟设备时候 则不会像目标地址发送东西
     */
    private void initOBDCodes(){
        logger.info("初始化模拟设备MM");
        //设置模拟设备
        String obdCodes = ApplicationVariable.getObdCodes();
        String sign = obdCodes;
        if (obdCodes == null || "all".equals(obdCodes.toLowerCase())) {sign = null;}
        List<String> strings = StrUtils.splitSeparate(sign, ",");
        List<String> allDifferentCodes = obdRepository.findAllDifferentCodes(strings);
        String join = StrUtils.join(allDifferentCodes, ",");
        if (StrUtils.checkNull(join)){
            ApplicationVariable.setStartTheReady(false);
            logger.info("没有可用设备");
        }else {
            ApplicationVariable.setObdCodes(join);
            logger.info("模拟设备初始化完成MM");
        }
        initDate();
        otherParameter();
        if (ApplicationVariable.getIsRunSend()){
            TaskUtil.RootStartObd();
        }
    }

    private void initDate(){
        logger.info("初始化时间段");
        Integer defaultDay = 7; //默认天数
        //数据
        Integer days = ApplicationVariable.getDays();
        if (days == null || days == 0){
            ApplicationVariable.setDays(null);
            logger.info("数据默认值设置完成");
        }else {
            logger.info("数据设置校验完成");
        }
        Date startTime = ApplicationVariable.getStartTime();
        Date endTime = ApplicationVariable.getEndTime();
        //默认设置为2
        ApplicationVariable.setDataType(2);
        //设置时间段情况
        if (startTime != null && endTime!= null ){
            if (endTime.getTime()>startTime.getTime()){
                return;
            }else {
                ApplicationVariable.setStartTime(endTime);
                ApplicationVariable.setEndTime(startTime);
                return;
            }
        }
        //只设置days的情况
        if (startTime == null &&  endTime == null){
            ApplicationVariable.setDataType(1);
             return;
        }
        //设置days 或者设置了一个时间的情况
        if (startTime == null && endTime !=null){
           if (days == null )ApplicationVariable.setDays(defaultDay);
            startTime = new Date(endTime.getTime() - ApplicationVariable.getDays()* 24 * 3600 * 1000L);
            ApplicationVariable.setStartTime(startTime);
            return;
        }

        if (startTime != null && endTime == null ){
            if (days == null )   ApplicationVariable.setDays(defaultDay);
            endTime = new Date(startTime.getTime() + ApplicationVariable.getDays()*24 *3600 * 1000L);
            ApplicationVariable.setEndTime(endTime);
            return;
        }

    }

    private void otherParameter(){
        //间隔时间
        Integer time = ApplicationVariable.getTime();
        if (time == null || time < 0) {
            ApplicationVariable.setTime(1);
            logger.info("间隔时间默认值设置完成");
        }else {
            logger.info("间隔时间校验完成");
        }

        //轮循的次数
        Integer circulationNum = ApplicationVariable.getCirculationNum();
        if (circulationNum == null || circulationNum <0){
            ApplicationVariable.setCirculationNum(-1);
            logger.info("轮循次数默认值设置完成");
        }else {
            logger.info("轮循设置校验完成");
        }

        Boolean isShareTCP = ApplicationVariable.getIsShareTCP();
        if (isShareTCP == null ){
            ApplicationVariable.setIsShareTCP(true);
            logger.info("TCP连接方式默认值设置完成");
        }else if (!isShareTCP){
            TcpClient.channel.close();
            logger.info("TCP连接方式设置完成");
        }

        ApplicationVariable.setStartTheReady(true);

        Boolean isRunSend = ApplicationVariable.getIsRunSend();
        if (isRunSend == null){
            ApplicationVariable.setIsRunSend(true);
        }

    }

    @Override
    /**
     * 启动获取配置的参数
     */
    public void setEnvironment(Environment environment) {

         String days = environment.getProperty("sim.days");
         String time = environment.getProperty("sim.time");
         String objectiveIP = environment.getProperty("sim.objective.IP");
         String objectivePort = environment.getProperty("sim.objective.port");
         String obdCodes = environment.getProperty("sim.obdCodes");
         String circulationNum = environment.getProperty("sim.circulationNum");
         String isShareTCP = environment.getProperty("sim.isShareTCP");
         String startTime = environment.getProperty("sim.startTime");
         String endTime =  environment.getProperty("sim.endTime");
        String serverPort =  environment.getProperty("server.port");
        String isRunSend = environment.getProperty("sim.isRunSend");
        try{
            ApplicationVariable.setDays(Integer.valueOf(days));
        }catch (Exception e){}
        ApplicationVariable.setObdCodes(obdCodes);
        try{
            ApplicationVariable.setTime(Integer.valueOf(time));
        }catch (Exception e){

        }
        ApplicationVariable.setObjectiveIP(objectiveIP);

        try{
            ApplicationVariable.setObjectivePort(Integer.valueOf(objectivePort));
        }catch (Exception e){}

        try{
            ApplicationVariable.setCirculationNum(Integer.valueOf(circulationNum));
        }catch (Exception e){}

        try{
            ApplicationVariable.setIsShareTCP(Boolean.valueOf(isShareTCP));
        }catch (Exception e){}

        try{
            ApplicationVariable.setStartTime(DateUtils.parseStrToDate(startTime));
        }catch (Exception e){}

        try{
            ApplicationVariable.setEndTime(DateUtils.parseStrToDate(endTime));
        }catch (Exception e){}

        try{
            ApplicationVariable.setServerPort(Integer.parseInt(serverPort));
        }catch (Exception e){}

        try{
            ApplicationVariable.setIsRunSend(Boolean.valueOf(isRunSend));
        }catch (Exception e){}
    }
}
