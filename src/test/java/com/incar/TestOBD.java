package com.incar;

import com.incar.entity.ObdHistory;
import com.incar.repository.OBDRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestOBD {

    @Autowired
    OBDRepository obdRepository;

    @Test
    public void queryOBD(){
//        ObdHistory one = obdRepository.findOne(6);
        Object obdHistories = obdRepository.findAllAndTime("INCAR000001", null);
        System.out.println();
    }


    public static void main(String[] args) {
        String str = "你好";
        byte[] bytes = str.getBytes();
        System.out.println(bytes);
    }
}
