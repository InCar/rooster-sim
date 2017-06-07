package com.incar;

import org.jboss.logging.Logger;
import org.junit.Test;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
public class OBDTest {

    private static final Logger logger = Logger.getLogger(OBDTest.class);
//    @Autowired
//    OBDRepository obdRepository;

    @Test
    public void queryOBD(){
        logger.info("编译成功");
    }


    @Test
    public void sendMsg() throws Exception {
//            String s = "AA";
//        TcpClient.sendMsg(s);
    }


    @Test
    public void testMsg(){
//        String s = "AA";
//        String s1 = StrUtils.StrToBinstr(s);
//        byte[] bytes = StringToHex.hexStringToBytes(s);
//        String s1 = Integer.toHexString(4);
//        System.out.println(s1);
    }
}
