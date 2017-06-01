package com.incar.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/1.
 */
public class StrUtils {


    /**
     * 字符串数组 转字符串
     * @param strs 字符串数组
     * @param separate 分隔字符
     * @return  如果字符串数组是空或者长度为0 返回null
     */
    public static String  join(String[] strs,String separate){
        if (strs == null || strs.length == 0 ){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String str:strs){
            sb.append(str);
            sb.append(separate);
        }
        String s = sb.toString();
        return s.substring(0,s.length()-1);
    }
    /**
     * 字符串集合 转字符串
     * @param strs 字符串集合
     * @param separate 分隔字符
     * @return  如果字符串数组是空或者长度为0 返回null
     */
    public static String  join(List<String> strs,String separate){
        if (strs == null || strs.size() == 0 ){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String str:strs){
            sb.append(str);
            sb.append(separate);
        }
        String s = sb.toString();
        return s.substring(0,s.length()-1);
    }

    /**
     * 字符串根据规则转换字符集合
     * @param strs 字符串
     * @param separate 分隔符号
     * @return
     */
    public static List<String> splitSeparate(String strs,String separate){
       if(checkNull(strs)){
           return null;
       }
        separate = separate==null?"":separate;
        String[] split = strs.split(separate);
        return Arrays.asList(split);
    }

    /**
     * 校验是否是空字符串
     * @param str
     * @return true 是  false 不是
     */
    public static boolean checkNull(String str){
        if (str== null ||"".equals(str)){
            return true;
        }
        return false;
    }

}
