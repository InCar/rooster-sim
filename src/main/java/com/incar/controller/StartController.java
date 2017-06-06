package com.incar.controller;

import com.incar.device.DevicePool;
import com.incar.entity.ObdHistory;
import com.incar.repository.OBDRepository;
import com.incar.util.ApplicationVariable;
import com.incar.util.OBDRunParameter;
import com.incar.util.StrUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
@RestController
public class StartController {

    private static final Logger logger = Logger.getLogger(StartController.class);
    private static int index = 0;


    private OBDRunParameter obdRunParameter;
//
    @Autowired
    public void setObdRunParameter(OBDRunParameter obdRunParameter) {
        this.obdRunParameter = obdRunParameter;
    }

    @RequestMapping(name = "/start",method = RequestMethod.GET)
    public Object start(){
        boolean isRun = ApplicationVariable.getIsRun();
        if (isRun){
            return "设备已经启动;无法重新启动,请重新初始化再进行启动";
        }
        if (!ApplicationVariable.getStartTheReady()){
            return "初始化失败;请仔细检查参数";
        }
        List<String> obdCodes = StrUtils.splitSeparate(ApplicationVariable.getObdCodes(), ",");
        index = 0;
        for (String code:obdCodes){
            int start = new DevicePool(null, code).start();
            if (start == 0){
                index ++;
            }else {
                logger.error("codes:"+code+";启动失败");
            }
        }
        return "成功启动了"+index+ "个模拟设备;";
    }

//    @RequestMapping(name = "/parameterInit",method = RequestMethod.GET)
//    public Object parameter(){
//        obdRunParameter.init();
//        return "正在进行参数初始化";
//    }
}
