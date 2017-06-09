package com.incar.gradleTask;

import com.incar.util.URLUtils;
import com.incar.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhouyongbo on 2017/6/7.
 */
public class TaskUtil {
    private static final Logger logger = LoggerFactory.getLogger(TaskUtil.class);

    public static void RootStartObd(){
        String url = "http://localhost:" + YamlUtils.getServerPort() + "/startObd";
        String s = URLUtils.sendGet(url, null);
        logger.info(s);
    }

    public static void initParament(){
        String url = "http://localhost:" + YamlUtils.getServerPort() + "/initParameter";
        String s = URLUtils.sendGet(url, null);
        logger.info(s);
    }
}
