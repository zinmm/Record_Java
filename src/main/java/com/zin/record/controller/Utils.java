package com.zin.record.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhujinming on 2018/8/30.
 */
public enum Utils {

    INSTANCE;

    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */

    public String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    public boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }
}
