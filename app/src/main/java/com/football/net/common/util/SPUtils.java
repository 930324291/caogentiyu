package com.football.net.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.football.net.common.constant.Constant;


/**
 * Author：Raoqw on 2016/7/20 10:31
 * Email：lhholylight@163.com
 */
public class SPUtils {

    private static SharedPreferences mSharedPreferences;

    public static SharedPreferences getSharedPreferences() {
        decideSP();
        return mSharedPreferences;
    }

    /**
     * 判断首选项是否为空
     */
    private static void decideSP() {
        if (mSharedPreferences == null) {
            mSharedPreferences = UIUtils.getContext().getSharedPreferences(Constant.SP_NAME,
                    Context.MODE_PRIVATE);
        }
    }

    /**
     * 使用SharedPreferences存储boolean类型数据.
     *
     * @param key   要存储的数据的key
     * @param value 要存储的数据
     */
    public static void cacheBooleanData(String key, boolean value) {
        decideSP();
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 取出缓存的boolean类型数据
     *
     * @param key      要取出的数据的key
     * @param defValue 缺省值
     * @return
     */
    public static boolean getBooleanData(String key, boolean defValue) {
        decideSP();
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 缓存字符串数据
     *
     * @param key
     * @param value
     */
    public static void cacheStringData(String key, String value) {
        decideSP();
        mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 根据key获取缓存的数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringData(String key, String defValue) {
        decideSP();
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * 缓存int型数据
     *
     * @param key
     * @param value
     */
    public static void cacheIntData(String key, int value) {
        decideSP();
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 根据key获取缓存的int 数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public static int getIntData(String key, int defValue) {
        decideSP();
        return mSharedPreferences.getInt(key, defValue);
    }
}
