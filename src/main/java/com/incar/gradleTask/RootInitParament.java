package com.incar.gradleTask;

import com.incar.util.URLUtils;
import com.incar.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhouyongbo on 2017/6/7.
 * 参数重新初始化
 */
public class RootInitParament {
    private static final Logger logger = LoggerFactory.getLogger(RootInitParament.class);

    public static void main(String[] args) {
        TaskUtil.RootStartObd();
    }
}
