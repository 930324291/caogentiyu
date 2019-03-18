package com.football.net.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by footman on 2017/3/19.
 */

public class BeanUtils {

    //由出生日期获得年龄
    public static int getAge(long birthDay) {
        if(birthDay <= 0){
            return  -1;
        }
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        Date birthDate = new Date(birthDay);
        cal.setTime(birthDate);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

}
