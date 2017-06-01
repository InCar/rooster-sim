package com.incar;

import com.incar.entity.ObdHistory;
import com.incar.repository.OBDRepository;
import com.incar.util.OBDRunParameter;
import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(TestOBD.class);
//    @Autowired
//    OBDRepository obdRepository;

    @Test
    public void queryOBD(){
        logger.info("编译成功");
    }

}
