package com.incar.threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by zhouyongbo on 2017/6/6.
 */
public class ThreadPool {
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(20);


}
