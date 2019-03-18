/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.football.net.widget;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包<br>
 * <p/>
 * <b>创建时间</b> 2014-8-14
 *
 * @author kymjs (https://github.com/kymjs)
 * @version 1.1
 */
public class StringUtils {

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence... strs) {
        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj, int defValue) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj, int defValue) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
            return defValue;
        }
    }

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        // p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        // p = Pattern
        // .compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        p = Pattern.compile("^[1][3578][0-9]{9}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 是否是正确的身份证号
     *
     * @param str
     * @return
     */
    public static boolean isIdCard(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("/^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$/i");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumRic(String str) {
        return str.matches("[0-9]*");
    }

    /**
     * 判断是否是纯字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterRic(String str) {
        return str.matches("[a-zA-Z]+");
    }


    /**
     * 字符串隐藏
     *
     * @param str
     * @param start 开始隐藏的位置
     * @param end   结束隐藏的位置
     * @return (stat != 0 end != str.length() 格式 189***1628)  (stat = 0 end != str.length() 格式 ******1628)  (stat = 0 end = str.length() 格式 18963961628)
     */
    public static String replaceSubString(String str, int start, int end) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String newStr = "";
        String s = "";
        String e = "";
        if (start != 0) {
            s = str.substring(0, start);
        }
        if (end != str.length()) {
            e = str.substring(str.length() - end, str.length());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = start; i < str.length() - end; i++) {
            sb = sb.append("*");
        }
        newStr = s + sb.toString() + e;
        return newStr;
    }

    /**
     * 银行卡 *隐藏 并格式化
     *
     * @param str
     * @param end
     * @return
     */
    public static String replaceBankString(String str, int start, int end) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String card = replaceSubString(str, start, end);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < card.length(); i++) {
            if (i == 0) {
                sb.append(card.charAt(i));
            } else {
                if (i % 4 == 0) {
                    sb.append(" " + card.charAt(i));
                } else {
                    sb.append(card.charAt(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 去除所有的其他字符
     *
     * @param s
     * @return
     */
    public static String deleteStr(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s.replaceAll("\\<.*?>|\\n", "");
        }
        return s;
    }

    /**
     * 字符串分割
     *
     * @param str
     * @return
     */
    public static String[] strSplit(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String[] s = str.split("\\s+");
        return s;
    }

    /**
     * 保留2位小数
     *
     * @param money
     * @return
     */
    public static String retain2Point(double money) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(money);
    }

    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * 去掉小数点后无用的0
     *
     * @return
     */
    public static String removePoint(String s) {
        if (StringUtils.isEmpty(s)) {
            return "";
        }
        if (s.indexOf(".") > 0) {
            //正则表达
            s = s.replaceAll("0+?$", "");//去掉后面无用的零
            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return s;
    }

    public String getFilePicName(String path) {
        String temp[] = path.replaceAll("\\\\", "/").split("/");
        String fileName = "";
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
            System.out.println(fileName);
        }
        return fileName;
    }

}
