package com.football.net.widget;

import android.util.DisplayMetrics;

import com.football.net.manager.FootBallApplication;

/**
 * @author：cc_demo on 2017/1/8 19:57
 * @email：282800622@qq.com
 * @description：
 */

public class DeviceUtil {
    public static int getWidth() {
        DisplayMetrics dm = FootBallApplication.getInstance().getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    public static int getHeight() {
        DisplayMetrics dm = FootBallApplication.getInstance().getResources().getDisplayMetrics();
        int w_screen = dm.heightPixels;
        return w_screen;
    }
}
