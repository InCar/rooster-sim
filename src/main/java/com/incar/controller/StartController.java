package com.incar.controller;

import com.incar.device.DevicePool;
import com.incar.entity.ObdHistory;
import com.incar.repository.OBDRepository;
import com.incar.util.ApplicationVariable;
import com.incar.util.OBDRunParameter;
import com.incar.util.StrUtils;
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

    @Autowired
    OBDRepository obdRepository;

    @RequestMapping(name = "/start",method = RequestMethod.GET)
    public Object start(){
        List<DevicePool> devicePools = new ArrayList<DevicePool>();
        List<String> obdCodes = StrUtils.splitSeparate(ApplicationVariable.getObdCodes(), ",");
        for (String code:obdCodes){
            List<String> allAndTimeByCodes = obdRepository.findAllAndTimeByCodes(code, ApplicationVariable.getDays());
            DevicePool devicePool = new DevicePool(null,code,allAndTimeByCodes);
            devicePools.add(devicePool);
        }
        int index = 0;
        for ( DevicePool devicePool:devicePools){
            try{
                devicePool.start();
                index++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "启动了"+index+"模拟设备";
    }
}
