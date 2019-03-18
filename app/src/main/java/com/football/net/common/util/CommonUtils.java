/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.football.net.common.util;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 常用工具类
 */
public class CommonUtils {

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }



    /**
     * 判断是否是手机号码
     *
     * @param name
     * @return
     */
    public static boolean isPhoneNumber(String name) {
        String str = "^(1[3|4|5|7|8])\\d{9}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(name);
        return m.matches();
    }


    /**
     * 判断是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 判断是否是邮箱
     *
     * @param name
     * @return
     */
    public static boolean isEmail(String name) {
        try {
//            String str = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
            String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            Pattern p = Pattern.compile(str);
            Matcher m = p.matcher(name);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 用时间戳生成照片名称
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        if (cretaeImagePath()) {
            return Environment.getExternalStorageDirectory() + "/CallX/Image/" + dateFormat.format(date) + ".jpg";
        } else {
            return Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + dateFormat.format(date) + ".jpg";
        }
    }

    public static boolean cretaeImagePath() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/CallX/Image");
            if (!file.exists()) {
                Boolean isOk = file.mkdirs();
                return isOk;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight() {
        int status_bar_height = 0;
        // 通过反射技术获取状态栏的高度
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");// 拿到字节码对象
            Object instance = clazz.newInstance();// 使用字节码对象new出一个实例
            Field field = clazz.getField("status_bar_height");// 通过字节码对象拿出属性
            int R_status_bar_height = Integer.parseInt(field.get(instance)
                    .toString());// 从实例身上中获取到属性的值
            status_bar_height = UIUtils.getContext().getResources().getDimensionPixelOffset(
                    R_status_bar_height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("status_bar_height:" + status_bar_height);
        return status_bar_height;
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static int[] getScreen(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * 将时间轴转换成时间
     *
     * @param time   日期时间轴
     * @param format 格式化之后的样式    yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateStr(long time, String format) {
        Date dat = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(gc.getTime());
    }

    /**
     * 如果是当天返回时间,否则返回日期
     *
     * @param time
     * @return
     */
    public static String getDateStr(long time) {
        Date todayDate = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(todayDate);
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdformat.format(gc.getTime());
        Date dat = new Date(time);
        gc.setTime(dat);
        if (today.equals(sdformat.format(gc.getTime()))) {
            sdformat = new SimpleDateFormat("HH:mm");
            return sdformat.format(gc.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(gc.getTime());
        }
    }

    public static String getDateTime(long time) {
        SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
        return sdformat.format(new Date(time));
    }

    public static String getDateDay(long time) {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        return sdformat.format(new Date(time));
    }

    public static String getFullTime(long time) {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdformat.format(new Date(time));
    }
    /**
     * 将生日格式转换成时间戳
     *
     * @param time
     * @return
     */
    public static long getBirthTimestamp(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * 获取日期时间
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getDate(int year, int month, int day) {
        String date;
        if (month < 9) {
            if (day < 10) {
                date = year + "-0" + (month + 1) + "-0" + day;
            } else {
                date = year + "-0" + (month + 1) + "-" + day;
            }
        } else {
            if (day < 10) {
                date = year + "-" + (month + 1) + "-0" + day;
            } else {
                date = year + "-" + (month + 1) + "-" + day;
            }
        }
        return date;
    }

    /**
     * 获取时间
     *
     * @param time   需要转换的时间
     * @param format 转换的格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long getTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * 获取时间
     *
     * @param hour
     * @param minute
     * @return
     */
    public static String getTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            if (minute < 10) {
                time = "0" + hour + ":0" + minute;
            } else {
                time = "0" + hour + ":" + minute;
            }
        } else {
            if (minute < 10) {
                time = hour + ":0" + minute;
            } else {
                time = hour + ":" + minute;
            }
        }
        return time;
    }

    /**
     * 控制只能输入两位小数 并限制最大大小
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText, final String maxNum, final Context context, final String msg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                if (!TextUtils.isEmpty(s)) {
                    float value = Float.valueOf(s.toString());
                    if (value > Float.valueOf(maxNum)) {
                        editText.setText(maxNum);
                        Toast.makeText(context, msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * 控制只能输入两位小数
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }

    /**
     * double
     *
     * @param s 返回保留两位小数的double
     * @return
     */
    public static double getDouble(String s) {
        DecimalFormat format = new DecimalFormat("0.00");
        s = format.format(new BigDecimal(s));
        System.out.println(s);
        return Double.valueOf(s);
    }


    /**
     * 判断指定Activity是否是当前界面
     *
     * @param TAG
     * @return
     */
    public static boolean isTopActivity(String TAG) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) UIUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(TAG)) {
            isTop = true;
        }
        return isTop;
    }

//    /**
//     * 加密数据
//     *  分段加密
//     * @param json
//     * @return
//     */
//    public static String getCipherStr(String json) {
//        RequestParam params = new RequestParam();
//        byte[] bytes = json.getBytes();
//        ArrayList<String> ciphertextlist = new ArrayList<String>();
//        try {
//            if (bytes.length > 117) {
//                int size = 29;
//                for (int i = 0; i < (json.length() / size) + 1; i++) {
//                    if (i == json.length() / size) {
//                        ciphertextlist.add(RSAEncryption.encrypt(json.substring(i * size)));
//                    } else {
//                        ciphertextlist.add(RSAEncryption.encrypt(json.substring(i * size, (i + 1) * size)));
//                    }
//                }
//                params.put("ciphertextlist", ciphertextlist);
//            } else {
//                params.put("ciphertext", RSAEncryption.encrypt(json));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return params.createParam();
//    }

    /**
     * 判断当前app是否处于前台
     *
     * @return
     */
    public static boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) UIUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals("com.ivfox.callx")) {
            return true;
        }
        return false;
    }


    /**
     * 为了防止用户或者测试MM疯狂的点击某个button，写个方法防止按钮连续点击。
     */
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 150) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 获取Assets中的文件
     *
     * @param fileName
     * @return
     */
    public static String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = UIUtils.getContext().getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            LogUtils.d("数据获取异常");
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 判断当前日期是星期几
     *
     * @param  pTime     设置的需要判断的时间  //格式如2012-09-08
     *

     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "星期日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "星期六";
        }
        return Week;
    }

    /**
     * 昵称保护
     * @param str
     * @return
     */
    public static String getStr(String str){
        if(TextUtils.isEmpty(str)){
            return "";
        }
        if(CommonUtils.isNumeric(str)){
            if(str.length() > 5){
                str = str.substring(0,3)+"****"+str.substring(str.length()-2);
            }
        }else{
            if(str.length()==2){
                str = str.substring(0,1) + "*";
            }else if(str.length() >= 3){
                str = str.substring(0,1) + "*"+str.substring(str.length()-1);
            }
        }
        return str;
    }

    /**
     * 供广场图片取大图使用
     * @param url
     * @return
     */
    public static String getRurl3(String url){
        if(TextUtils.isEmpty(url)){
            return "";
        }
        url = url.replace("\\","");
        if(url.startsWith("\"")){
            url = url.substring(1,url.length());
        }
        if(url.endsWith("\"")){
            url = url.substring(0,url.length()-1);
        }
        if(!url.startsWith("{")){
            return url;
        }
        Gson gson = new Gson();
        HashMap<String,String> map = gson.fromJson(url, HashMap.class);
        url = map.get("3");
        if(url == null){
            url = map.get("2");
        }
        if(url == null){
            url = map.get("1");
        }
        return url;
    }

    public static String getRurl(String url){
        if(TextUtils.isEmpty(url)){
            return "";
        }
        url = url.replace("\\","");
        if(url.startsWith("\"")){
            url = url.substring(1,url.length());
        }
        if(url.endsWith("\"")){
            url = url.substring(0,url.length()-1);
        }
        if(!url.startsWith("{")){
            return url;
        }
        Gson gson = new Gson();
        HashMap<String,String> map = gson.fromJson(url, HashMap.class);
        url = map.get("2");
        if(url == null){
            url = map.get("1");
        }
        return url;
    }

    public static int getTeamTypeImage(String teamType){

       if("3".equals(teamType)){
           return R.mipmap.people3;
       }

       if("5".equals(teamType)){
           return R.mipmap.people5;
       }

       if("7".equals(teamType)){
           return R.mipmap.people7;
       }

       if("11".equals(teamType)){
           return R.mipmap.people11;
       }
        return R.mipmap.people3;
    }

    public static int getLiksNum(String likes){
        int num1 = 0;
        if(likes != null){
            num1 =likes.split(",").length;
        }
        return num1;
    }

    public static String getGamePercent(int win,int total){
        String percent = "";
        if(total == 0){
            percent = "100";
        }else{
            float num= (float)win*100/total;
            if(num < 100){
                DecimalFormat df = new DecimalFormat("0");//格式化小数
                percent = df.format(num);//返回的是string类型
            }else{
                percent = "100";
            }
        }
        return percent;

    }

    //[[1,"前场"],[2,"中场"],[3,"后场"],[4,"门将"]],
    public static String getPositionStr(String position){
        if(TextUtils.isEmpty(position)){
            return "暂无";
        }
        if("1".equals(position)){
            return "前场";
        }else if("2".equals(position)){
            return "中场";
        }else if("3".equals(position)){
            return "后场";
        }else if("4".equals(position)){
            return "门将";
        }else{
            return "暂无位置";
        }
    }

    public static String getGameStatus(GameBean game) {
        if(game==null) {
            return "";
        } else {
            if (game.getTeamBOperation()==null) {
                if(game.getBeginTime() > System.currentTimeMillis()){
                    return "约战中";
                }else if(game.getBeginTime() <= System.currentTimeMillis()){
                    return "已过期";
                }
            } else {
                if(game.getTeamBOperation() == 2){
                    return "已拒战";
                }else if(game.getTeamBOperation() ==1 && game.getBeginTime() > System.currentTimeMillis()){
                    return "未开赛";
                }else if(game.getTeamBOperation() == 1 && game.getBeginTime() <= System.currentTimeMillis()) {
                    if (game.getAuditStatus()==null) {
                        if (StringUtils.isEmpty(game.getScoreA1()) || StringUtils.isEmpty(game.getScoreA2())) {
                            return "待录入";
                        } else if (!game.getScoreA1().equals(game.getScoreA2()) || !game.getScoreB1().equals(game.getScoreB2())) {
                            return "待审核";
                        }
                    } else {
                        if (game.getAuditStatus() == 2) {
                            return "审核不通过";
                        } else if (game.getAuditStatus() == 1) {
                            return "已结束";
                        }
                    }
                }
            }
        }
        return "";
    }


}
