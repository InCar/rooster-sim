package com.incar.gradleTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhouyongbo on 2017/6/7.
 * 参数重新初始化
 */
public class RootInitParament {
    private static final Logger logger = LoggerFactory.getLogger(RootInitParament.class);

    public static void main(String[] args) {
        TaskUtil.initParament();
    }
}
