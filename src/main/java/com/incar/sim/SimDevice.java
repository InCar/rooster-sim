package com.incar.sim;/**
 * Created by fanbeibei on 2017/8/21.
 */

import com.incar.sim.dao.ObdHistoryDao;
import com.incar.sim.net.NetClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Fan Beibei
 * @Description: 描述
 * @date 2017/8/21 10:40
 */
public class SimDevice {

    private NetClient netClient;
    private ObdHistoryDao dao;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private static final int PERIOD = 5*60;

    public SimDevice(NetClient netClient, ObdHistoryDao dao) {
        this.netClient = netClient;
        this.dao = dao;
    }


    public void start(){

        netClient.start();

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("##333333");
            }
        },PERIOD,PERIOD, TimeUnit.SECONDS);

    }

    public void close(){

        executorService.shutdownNow();
        netClient.close();

    }

}
