package com.incar.util;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.List;

import static io.netty.buffer.Unpooled.buffer;

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


    // 将字符串转换成二进制字符串，以空格相隔
    public static String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }


    public static String StrToStr(String str){
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }


    /**
     * 把16进制字符串转换成字节数组
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static void main(String[] args) {
//        byte b = Integer.valueOf("0", 16).byteValue();
//        System.out.println(b);

        String s = "0";
//        byte b = Byte.parseByte(s, 16);
        byte b =  Integer.valueOf("9D", 16).byteValue();
        System.out.println(b);
//        byte s1 = (byte) (0x0+s);
//        System.out.println(s1);

//        String s = "16 08 31 30 30";
//        ByteBuf byteBuf = getByteBuf(s);
//        System.out.println(byteBuf);
    }

    public static ByteBuf getByteBuf(String str){
        //根据16进制字符串得到ByteBuf对象(netty)
        ByteBuf bb=buffer(1024);

        String[] command=str.split(" ");
        byte[] abc=new byte[command.length];
        for(int i=0;i<command.length;i++){
            abc[i]=Integer.valueOf(command[i],16).byteValue();
        }
        bb.writeBytes(abc);
        return bb;
    }



}
