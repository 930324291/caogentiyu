/**
 * ID: MainerApplication.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.manager;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v13.app.ActivityCompat;

import com.football.net.R;
import com.football.net.bean.PlayerLikeBean;
import com.football.net.bean.TeamLikeBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.IConfig;
import com.football.net.http.reponse.impl.UnreadNumResult;
import com.football.net.widget.Displayer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.youku.cloud.player.YoukuPlayerConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FootBallApplication extends Application implements IConfig {

    /**
     * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
     */
    private static FootBallApplication instance;   //单例模式解耦
    public static DisplayImageOptions options,circOptions;    //直角
    /**
     * 记录所有活动的Activity
     */
    public static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    private boolean appLog;        //是否打印详细日志
    /**
     * 接口密钥
     */
    public static String appKey = "10193053";

    public static int APPLacationRole =2; //captain 1;team member 2;

    public static int ROLE_INIT= -1; //刚开始注册
    public static int ROLE_CAPTAIN = 1;  //队长
    public static int ROLE_TeamMember = 2;  //队员

    public static String access_token;

    public static List<TeamLikeBean> teamLikes; // 用户对球队的点赞集合
    public static List<PlayerLikeBean> playerLikes; // 用户对球员的点赞集合

    public static int like_player_max;  // 每天给球员点赞次数上限

    public static int like_team_max;  // 每天给球队点赞次数上限

    public static int dare_max;  // 球队每天约战的次数上限

    public static int create_team_max; // 球员每天创建球队上限

    public static UserBean userbean;
    public static UnreadNumResult unreadNumResult;

    //优酷视频clientId，clientSecret
    public static final String CLIENT_ID_WITH_AD = "937f9355701f8383";
    public static final String CLIENT_SECRET_WITH_AD = "bc17f29237940a00027e2813f07ce754";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initial(getApplicationContext());

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_MODERATE) {
            finishAll();
        }

    }

    public static FootBallApplication getInstance() {
        return instance;
    }

    private void initial(Context context) {
        //初始化图片加载器
        init_imageloader(context);
        //是否打印log
        appLog = true;
        userbean = new UserBean();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CrashReport.initCrashReport(getApplicationContext(), "900047644", true);
                YoukuPlayerConfig.setClientIdAndSecret(CLIENT_ID_WITH_AD,CLIENT_SECRET_WITH_AD);
                YoukuPlayerConfig.onInitial(instance);
                YoukuPlayerConfig.setLog(false);
            }
        }).start();

    }

    private void init_imageloader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());

        options = new DisplayImageOptions.Builder() //圆角
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.color.bg_FFA19D9D)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.color.bg_FFA19D9D) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.color.bg_FFA19D9D) // 设置图片加载或解码过程中发生错误显示的图片
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        //显示图片的配置
        circOptions = new DisplayImageOptions.Builder() //圆形
                .cacheInMemory(false)    //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
                .showImageOnLoading(R.mipmap.head)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.head) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.head) // 设置图片加载或解码过程中发生错误显示的图片
                .displayer(new Displayer(0))
                .build();

    }
    public boolean isAppLog() {
        return appLog;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        FootBallApplication.appKey = appKey;
    }

    /**
     * 检查网络状态
     *
     * @return Boolean 是否有网络
     */
    public boolean isConnected() {
        NetworkInfo info = null;
        try {

            ConnectivityManager connectivity =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 关闭所有Activity
     */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (FootBallApplication.mActivities) {
            copy = new ArrayList<BaseActivity>(FootBallApplication.mActivities);
        }
        mActivities.clear();
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }



    public static final int REQUEST_EXTERNAL_STORAGE = 111;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

}
