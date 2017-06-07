package com.incar.util;

import com.incar.Application;

/**
 * Created by zhouyongbo on 2017/6/7.
 */
public class YamlUtils {
    public static Integer getServerPort(){
        YamlAnalyz yamlAnalyz = new YamlAnalyz("application.yml",Application.class);
        return Integer.valueOf((Integer) yamlAnalyz.getValue("server.port"));
    }
}
