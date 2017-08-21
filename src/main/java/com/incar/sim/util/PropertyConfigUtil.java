/**
 *
 */
package com.incar.sim.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * @author Lucian
 * @ClassName: CostomizedPropertyPlaceholderConfigurer
 * @Description: 缓存系统属性配置文件如：appliction.properties的属性
 * @date 2014年10月29日 上午9:18:24
 */
public class PropertyConfigUtil extends PropertyPlaceholderConfigurer {
    private static final Logger log = LogManager.getLogger(PropertyConfigUtil.class);

    private static Map<String, String> cxtPropertiesMap;
    private Resource[] locations;
    private String projectName;



    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        cxtPropertiesMap = new HashMap<String, String>();

        String dirPath = System.getProperty("global.config.path");

        if (StringUtils.isBlank(dirPath)) {
            dirPath = "/home/webdata/" + projectName + "/webroot/config";
        } else {
            dirPath = dirPath + File.separator + projectName;
        }

        props.put("config_path", dirPath);// log4j用的
        for (Resource location : locations) { // 只加载需要加载的文件
            try {
                File propFile = new File(dirPath + File.separator + location.getFilename());
                if (!propFile.exists()) {
                    throw new RuntimeException("Config file <" + propFile.getAbsolutePath() + "> doesn't exists.");
                }
                try {
                    InputStream is = new FileInputStream(propFile);
                    props.load(is);
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                log.error("PropertyConfigUtil getconfig error:", e);
            }
        }

        super.processProperties(beanFactoryToProcess, props);
        /*Level level = Level.toLevel(props.getProperty("log4j.level"));
        LogManager.getRootLogger().setLevel(level);*/
        cxtPropertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            cxtPropertiesMap.put(keyStr, value);
        }
    }

    public static String getProperty(String name) {
        return cxtPropertiesMap.get(name);
    }

    public static Map<String, String> getCxtPropertiesMap() {
        return cxtPropertiesMap;
    }

    public Resource[] getLocations() {
        return locations;
    }

    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }

    public static void setCxtPropertiesMap(Map<String, String> cxtPropertiesMap) {
        PropertyConfigUtil.cxtPropertiesMap = cxtPropertiesMap;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

}
