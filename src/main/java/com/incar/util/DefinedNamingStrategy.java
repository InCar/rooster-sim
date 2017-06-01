package com.incar.util;

import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

import java.io.Serializable;

/**
 * Created by zhouyongbo on 2017/5/31.
 * 备份  自定义校验策略
 */
public class DefinedNamingStrategy extends ImprovedNamingStrategy implements Serializable {


    private static final long serialVersionUID = -4061652392325924118L;

    public String propertyToColumnName(String propertyName) {
        if (propertyName.endsWith("__")) {
            System.out.println(propertyName);
            return propertyName.replace("__", "").toLowerCase();
        }
        return addUnderscores(StringHelper.unqualify(propertyName));
    }

    protected static String addUnderscores(String name){
        if (name.endsWith("__")){
            return name.replace("__", "");
        }else {
            return ImprovedNamingStrategy.addUnderscores(name);
        }
    }

    public String columnName(String columnName){
        return addUnderscores(columnName);
    }
}
