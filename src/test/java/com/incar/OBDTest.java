package com.incar;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class OBDTest {

    private static final Logger logger = Logger.getLogger(OBDTest.class);
//    @Autowired
//    OBDRepository obdRepository;

    @Test
    public void queryOBD(){
        logger.info("编译成功");
    }

}
