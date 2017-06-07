package com.incar.controller;

import com.incar.device.DevicePool;
import com.incar.entity.Result;
import com.incar.util.ApplicationVariable;
import com.incar.util.OBDRunParameter;
import com.incar.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
@RestController
//@RequestMapping("/api")
public class StartController {

    private static final Logger logger = LoggerFactory.getLogger(ObdController.class);

    private static int index = 0;

    @RequestMapping(value = "/startObd",method = RequestMethod.GET)
    public Result startObd(){
        boolean isRun = ApplicationVariable.getIsRun();
        if (isRun){
            return new Result(false,"设备已经启动;无法重新启动,请重新初始化再进行启动");
        }
        if (!ApplicationVariable.getStartTheReady()){
            return  new Result(false,"初始化失败;请仔细检查参数");
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
        return  new Result(true,"成功启动了"+index+ "个模拟设备;");
    }


    @RequestMapping(value = "/initParameter",method = RequestMethod.GET)
    public Result initParameter(){
        new OBDRunParameter().init();
        return  new Result(false,"正在进行参数初始化");
    }
}
