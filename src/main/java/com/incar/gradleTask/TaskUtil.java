package com.incar.gradleTask;

import com.incar.util.URLUtils;
import com.incar.util.YamlUtils;
import org.apache.log4j.Logger;

/**
 * Created by zhouyongbo on 2017/6/7.
 */
public class TaskUtil {
    private static final Logger logger = Logger.getLogger(TaskUtil.class);

    public static void RootStartObd(){
        String url = "http://localhost:" + YamlUtils.getServerPort() + "/startObd";
        String s = URLUtils.sendGet(url, null);
        logger.info(s);
    }
}
