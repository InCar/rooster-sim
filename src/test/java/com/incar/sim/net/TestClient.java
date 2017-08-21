package com.incar.sim.net;/**
 * Created by fanbeibei on 2017/8/9.
 */

import com.incar.sim.net.reconnect.Netty4Client;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Fan Beibei
 * @Description: 描述
 * @date 2017/8/9 15:47
 */
public class TestClient {
    @Test
    public void test(){
        Netty4Client client = new Netty4Client();
        client.start();



        client.send("eeeeeeeeeee");

        try {
            System.in.read();
        }catch (IOException e){

        }

    }

}
