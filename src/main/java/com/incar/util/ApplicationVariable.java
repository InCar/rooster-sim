package com.incar.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zhouyongbo on 2017/6/1.
 * 初始化启动常量
 */
public final class ApplicationVariable {


    /**
     * 参数是否校验通过
     */
    private static Boolean startTheReady;

    /**
     * 间隔时间
     */
    private static Integer time;

    /**
     * 目标的IP地址
     */
    private static String objectiveIP;

    /**
     * 目标的端口
     */
    private static Integer objectivePort;

    /**
     *  设备号
     */
    private static String obdCodes;

    /**
     *  天数
     */
    private static Integer days;

    /**
     * 小于0代表永久循环 0代表停止循环 ; 大于0代表需要循环的次数
     */
    private static Integer circulationNum;

    /**
     * 代表多设备是否共享一个TCP连接 only:共享 more不共享(每个设备都会有一个自己的TCP连接通道)  默认为only
     */
    private static Boolean isShareTCP;

    /**
     * 开始时间
     */
    private static Date startTime;

    /**
     * 结束时间
     */
    private static Date endTime;


    /**
     *是否已经启动
     */
    private static boolean isRun;


    /**
     * 已什么形式取数据源   1:已days 形式  2:已时间段的形式;
     * @return
     */
    private static Integer dataType;


    public static Integer getTime() {
        return time;
    }

    public static void setTime(Integer time) {
        ApplicationVariable.time = time;
    }

    public static String getObjectiveIP() {
        return objectiveIP;
    }

    public static void setObjectiveIP(String objectiveIP) {
        ApplicationVariable.objectiveIP = objectiveIP;
    }

    public static Integer getObjectivePort() {
        return objectivePort;
    }

    public static void setObjectivePort(Integer objectivePort) {
        ApplicationVariable.objectivePort = objectivePort;
    }

    public static String getObdCodes() {
        return obdCodes;
    }

    public static void setObdCodes(String obdCodes) {
        ApplicationVariable.obdCodes = obdCodes;
    }

    public static Integer getDays() {
        return days;
    }

    public static void setDays(Integer days) {
        ApplicationVariable.days = days;
    }

    public static Integer getCirculationNum() {
        return circulationNum;
    }

    public static void setCirculationNum(Integer circulationNum) {
        ApplicationVariable.circulationNum = circulationNum;
    }

    public static Boolean getIsShareTCP() {
        return isShareTCP;
    }

    public static void setIsShareTCP(Boolean isShareTCP) {
        ApplicationVariable.isShareTCP = isShareTCP;
    }

    public static Date getStartTime() {
        return startTime;
    }

    public static void setStartTime(Date startTime) {
        ApplicationVariable.startTime = startTime;
    }

    public static Date getEndTime() {
        return endTime;
    }

    public static void setEndTime(Date endTime) {
        ApplicationVariable.endTime = endTime;
    }

    public static Boolean getStartTheReady() {
        return startTheReady;
    }

    public static void setStartTheReady(Boolean startTheReady) {
        ApplicationVariable.startTheReady = startTheReady;
    }

    public static Integer getDataType() {
        return dataType;
    }

    public static void setDataType(Integer dataType) {
        ApplicationVariable.dataType = dataType;
    }

    public static boolean getIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean isRun) {
        ApplicationVariable.isRun = isRun;
    }
}
