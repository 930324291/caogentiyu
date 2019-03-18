package com.football.net.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.common.util.VolleyImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class AddPlacePhotoAdapter extends BaseAdapter {

    //    private final DisplayImageOptions options;
    private Context mContext;
    ArrayList<String> paths = new ArrayList<>();
    int width;
    int count;  //总共显示多少个

    public AddPlacePhotoAdapter(Context context, ArrayList<String> datas, int count) {
        this.mContext = context;
        paths.clear();
        paths.addAll(datas);
        this.count = count;
//        width = UIUtils.getScreenWidth() - UIUtils.dip2px(15 * 2 + 30);
        //显示图片的配置
//        options = new DisplayImageOptions.Builder() //直角
//                .cacheInMemory(false)    //设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
//                .showImageOnLoading(R.mipmap.home_img_bg)// 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.home_img_bg) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.home_img_bg) // 设置图片加载或解码过程中发生错误显示的图片
//                .build();
    }

    @Override
    public int getCount() {
        if (paths != null && paths.size() > 0 && paths.size() < count) {
            return paths.size() + 1;
        } else if (paths.size() == count) {
            return paths.size();
        }
        return 0;
    }

    public void setDatas(List<String> pics) {
        paths.clear();
        paths.addAll(pics);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PictureHolder vHolder = null;
        if (convertView == null) {
            vHolder = new PictureHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_look_pic, parent, false);
            vHolder.iv = (ImageView) convertView.findViewById(R.id.id_show_pic_img);
//            ImageView iv = new ImageView(mContext);
//            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width / ((GridView) parent).getNumColumns(), width / ((GridView) parent).getNumColumns());
//            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width / 3, width / 3);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            iv.setLayoutParams(lp);
//            convertView = iv;

//            vHolder.iv = iv;
            convertView.setTag(vHolder);
//            showDrawableRoundImg(mContext, R.mipmap.home_img_bg, vHolder.iv);
        } else {
            vHolder = (PictureHolder) convertView.getTag();
        }

//        ImageLoader.getInstance().cancelDisplayTask(vHolder.iv);   //防止网络慢的时候，图片覆盖
        if (paths.size() < 12) {
            if (position < paths.size()) {
                String path = paths.get(position);
                showRoundImg(mContext, path, R.mipmap.home_img_bg, R.mipmap.home_img_bg, vHolder.iv);
//                if (!TextUtils.isEmpty(paths.get(position)) && paths.get(position).startsWith("http")) {
////                    ImageLoader.getInstance().displayImage(paths.get(position), vHolder.iv, options);
//                    showRoundImg(mContext, paths.get(position), R.mipmap.home_img_bg, R.mipmap.home_img_bg, vHolder.iv);
//                } else {
////                    Bitmap bit = VolleyImageUtils.getScaledBitmap(paths.get(position), 200, 200);
////                    Bitmap bit = getLoacalBitmap(paths.get(position));
////                    vHolder.iv.setImageBitmap(bit);
//                    showRoundImg(mContext, paths.get(position), R.mipmap.home_img_bg, R.mipmap.home_img_bg, vHolder.iv);
//                }
            } else {
                showDrawableRoundImg(mContext, R.mipmap.dianjihou, vHolder.iv);
            }
        } else {
            String path = paths.get(position);
            showRoundImg(mContext, path, R.mipmap.home_img_bg, R.mipmap.home_img_bg, vHolder.iv);
//            if (!TextUtils.isEmpty(paths.get(position)) && paths.get(position).startsWith("http")) {
////                ImageLoader.getInstance().displayImage(paths.get(position), vHolder.iv, options);
//                showRoundImg(mContext, paths.get(position), R.mipmap.home_img_bg, R.mipmap.home_img_bg, vHolder.iv);
//            } else {
////                Bitmap bit = VolleyImageUtils.getScaledBitmap(paths.get(position), 200, 200);
////                vHolder.iv.setImageBitmap(bit);
//                showRoundImg(mContext, paths.get(position), R.mipmap.home_img_bg, R.mipmap.home_img_bg, vHolder.iv);
//            }
        }
        return convertView;
    }

    static class PictureHolder {
        ImageView iv;
    }

    /**
     * 显示本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showRoundImg(Context mContext, String picUrl, int loadingImg, int faildImg, ImageView mShowImg) {
//        if (mContext instanceof Activity) {
//            Activity act = (Activity) mContext;
//            if (act == null || act.isFinishing()) {
//                return;
//            }
//        }
//        if (Protect.checkLoadImageStatus(mContext)) {
        Glide.with(mContext).load(picUrl).placeholder(loadingImg).error(faildImg).crossFade().into(mShowImg);
//        }
    }

    public static void showDrawableRoundImg(Context mContext, int drawable, ImageView mShowImg) {
//        if (mContext instanceof Activity) {
//            Activity act = (Activity) mContext;
//            if (act == null || act.isFinishing()) {
//                return;
//            }
//        }
//        if (Protect.checkLoadImageStatus(mContext)) {
        Glide.with(mContext).load(drawable).crossFade().into(mShowImg);
//        }
    }
}

