package com.incar.controller;

import com.incar.device.DataManage;
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
public class StartController {

    private static final Logger logger = LoggerFactory.getLogger(StartController.class);


    @RequestMapping(value = "/startObd",method = RequestMethod.GET)
    public Result startObd(){
        if (!ApplicationVariable.getStartTheReady()){
            return  new Result(false,"初始化失败;请仔细检查参数");
        }
        int index = DataManage.start();
        return  new Result(true,"成功启动了"+index+ "个模拟设备;");
    }


    @RequestMapping(value = "/initParameter",method = RequestMethod.GET)
    public Result initParameter(){
        if (!ApplicationVariable.getStartTheReady()){
            new OBDRunParameter().init();
            return  new Result(true,"正在进行参数初始化");
        }else {
            return new Result(false,"参数已初始化过");
        }
    }
}
