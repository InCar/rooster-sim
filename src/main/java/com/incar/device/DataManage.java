package com.incar.device;

import com.incar.threads.ThreadPool;
import com.incar.util.ApplicationVariable;
import com.incar.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/9.
 */
public class DataManage {
    private static final Logger logger = LoggerFactory.getLogger(DataManage.class);

    private static int index; //已启动设备

   private static List<DevicePool> devicePools; //OBD设备


    public static List<DevicePool> getDevicePools() {
        return devicePools;
    }

    public static void setDevicePools(List<DevicePool> devicePools) {
        DataManage.devicePools = devicePools;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        DataManage.index = index;
    }


    public static int start(){
        if (devicePools == null ){
            ThreadPool.poolInfo();
            devicePools = new ArrayList<DevicePool>();
            List<String> obdCodes = StrUtils.splitSeparate(ApplicationVariable.getObdCodes(), ",");
            for (String code:obdCodes){
                DevicePool devicePool = new DevicePool(null, code);
                devicePools.add(devicePool);
                devicePool.start();
                int start = devicePool.start();
                if (start == 0){
                    index ++;
                }else {
                    logger.error("codes:"+code+";启动失败");
                }
            }

//            devicePools = new ArrayList<DevicePool>();
//            List<String> obdCodes = StrUtils.splitSeparate(ApplicationVariable.getObdCodes(), ",");
//            for (String code:obdCodes){
//                DevicePool devicePool = new DevicePool(null, code);
//                devicePools.add(devicePool);
//                int start = devicePool.start();
//                if (start == 0){
//                    index ++;
//                }else {
//                    logger.error("codes:"+code+";启动失败");
//                }
//            }
        }else {
            continueRun();
        }
        return index;
    }

    public static void continueRun(){
        logger.info("continue:启动数据发送");
        for (DevicePool devicePool:devicePools){
            if (!devicePool.isSend()){
                devicePool.setIndex(1);
                devicePool.setSend(true);
                devicePool.start();
            }
        }
    }

}
