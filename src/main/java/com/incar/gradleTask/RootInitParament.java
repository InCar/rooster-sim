package com.incar.gradleTask;

import com.incar.util.URLUtils;
import com.incar.util.YamlUtils;
import org.apache.log4j.Logger;

/**
 * Created by zhouyongbo on 2017/6/7.
 * 参数重新初始化
 */
public class RootInitParament {
    private static final Logger logger = Logger.getLogger(RootInitParament.class);

    public static void main(String[] args) {
        TaskUtil.RootStartObd();
    }
}
