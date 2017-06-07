package com.incar.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/7.
 */
public class YamlAnalyz {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 最跟目录的启动文件
     */
    private Class  runClass;

    /**
     * 文件对象
     */
    private Yaml yaml;

    /**
     * 存储结构
     */
    private LinkedHashMap<String,Object> documentMap;

    @SuppressWarnings("unchecked")
    public YamlAnalyz(String fileName, Class runClass) {
        this.fileName = fileName;
        this.runClass = runClass;
        yaml = new Yaml();
        try {
            documentMap = (LinkedHashMap<String, Object>) yaml.load(new FileInputStream(new File(getPath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件路径
     * @return
     */
    private String getPath(){
        return runClass.getClassLoader().getResource(fileName).getPath();
    }

    /**
     * 传入key的格式为   server.port
     * @param key
     * @return
     */
    public Object getValue(String key){
        List<String> keys = StrUtils.splitSeparate(key, "\\.");
        return findProperty(keys, documentMap, 0);
    }

    /**
     *
     * @param keys key集合 不能为空且必须长度大于1
     * @param obj 数据源
     * @param index 默认起始是0
     * @return
     */
    @SuppressWarnings("deprecation")
    private Object findProperty(List<String> keys, Object obj, Integer index){
        if (keys.size() == index)return obj;
        if (obj == null ) return obj;
        if (obj instanceof LinkedHashMap){
            Object o = ((LinkedHashMap) obj).get(keys.get(index));
            index ++ ;
            return findProperty(keys,o,index);
        }else {
            return obj;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Class getRunClass() {
        return runClass;
    }

    public void setRunClass(Class runClass) {
        this.runClass = runClass;
    }

    public Yaml getYaml() {
        return yaml;
    }

    public void setYaml(Yaml yaml) {
        this.yaml = yaml;
    }

    public LinkedHashMap<String, Object> getDocumentMap() {
        return documentMap;
    }

    public void setDocumentMap(LinkedHashMap<String, Object> documentMap) {
        this.documentMap = documentMap;
    }

}
