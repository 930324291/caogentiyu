package com.football.net.common.util;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by footman on 2017/3/3.
 */

public class TimeUtils {
    /**
     * 把long 转换成 日期 再转换成String类型
     */
    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }
}
