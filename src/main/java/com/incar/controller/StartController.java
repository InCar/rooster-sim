package com.incar.controller;

import com.incar.entity.ObdHistory;
import com.incar.repository.OBDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        List<ObdHistory> allAndTime = obdRepository.findAllAndTime("INCAR000001", null);
        return "启动";
    }
}
