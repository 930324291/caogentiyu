package com.football.net.adapter.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.football.net.common.util.StringUtils;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class Protect {

    public static boolean checkLoadImageStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    public static boolean checkLoadImageStatus(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && fragment.getActivity().isDestroyed()) {
            return false;
        }
        return true;
    }

    public static boolean checkLoadImageStatus(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    /**
     * 设置ali图片服务器的路径地址
     * @param custName
     * @param noteName
     * @return
     */
    public static String setName(String custName, String noteName) {
        if (StringUtils.isEmpty(custName) && StringUtils.isEmpty(noteName)) {
            return "";
        }
        return TextUtils.isEmpty(noteName) ? custName : noteName;
    }

    /**
     * 获取服务器返回地址
     *
     * @param path
     * @return
     */
    public static String setPic(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if (path.contains("http") || path.contains("https")) {
            return path;
        } else {
//            return OssManager.OSS_HOST + path;
            return  path;
        }
    }

    /**
     * 返回oss路径
     *
     * @return
     */
    public static String returnOssPath(int flag,String lastPath) {
        String bucketName = "";
        if (flag == 1) {
            bucketName = "jiayoula";
        } else if (flag == 2) {
            bucketName = "xyjyl";
        }
//        return "http://" + bucketName + "." + OssManager.endpoint + "/" + lastPath;
        return "";
    }

}
