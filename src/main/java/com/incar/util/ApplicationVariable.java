package com.incar.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by zhouyongbo on 2017/6/1.
 * 初始化启动常量
 */
@Component
public class ApplicationVariable {
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
}
