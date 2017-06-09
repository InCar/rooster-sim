package com.incar.threads;

import com.incar.device.DevicePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by zhouyongbo on 2017/6/6.
 */
public class ThreadPool {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);

    private final static ExecutorService  scheduledThreadPool = Executors.newCachedThreadPool();

    public static void scheduledThreadPool(DevicePool devicePool) {
        scheduledThreadPool.execute(devicePool);
    }



    public static void poolInfo(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    ThreadPoolExecutor scheduledThreadPool = (ThreadPoolExecutor) ThreadPool.scheduledThreadPool;
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int activeCount = scheduledThreadPool.getActiveCount();
                    logger.debug("线程池主动执行任务的近似线程数:"+activeCount);
                    long taskCount = scheduledThreadPool.getTaskCount();
                    logger.debug("线程池当前需要执行的任务数量"+taskCount);
                    int poolSize = scheduledThreadPool.getPoolSize();
                    logger.debug("线程池线程池当前线程数:"+poolSize);
                }

            }
        });
        thread.start();
    }
}
