package com.football.net.widget;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/26 0026.
 */
public class StaticObjectUtils {

    private static ArrayList<String> imageUrls = null;  //报事照片

    public static ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public static void setImageUrls(ArrayList<String> imageUrls) {
        StaticObjectUtils.imageUrls = imageUrls;
    }
}
