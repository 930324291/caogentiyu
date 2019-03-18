package com.football.net.adapter.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.widget.StringUtils;

/**
 * 作者：Administrator on 2016/8/9 0009 10:15
 * 功能：封装viewholder
 * 说明：
 */
public class ViewHolder {
    private final SparseArray<View> mViews;

    private View mConvertView;

    private int mPosition;

    private Context mContext;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<View>();
        this.mPosition = position;
        this.mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (null == convertView) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder vh = (ViewHolder) convertView.getTag();
            vh.mPosition = position;
            return vh;
        }
    }

    /**
     * 通过控件的Id获取控件，如果没有则加入views
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (StringUtils.isEmpty(text)) {
            view.setText("");
        } else {
            view.setText(text);
        }
        return this;
    }

    /**
     *
     * @param viewId
     * @param flag
     */
    public void setTxtVisibility(int viewId, boolean flag) {
        if (flag) {
            getView(viewId).setVisibility(View.VISIBLE);
        } else {
            getView(viewId).setVisibility(View.GONE);
        }
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setBackgroundResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setBackgroundResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 添加网络图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ViewHolder setImageByUrl(int viewId, String url, int loadImg) {
        if (mContext instanceof Activity) {
            Activity act = (Activity) mContext;
            if (act == null || act.isFinishing()) {
                return this;
            }
        }
//        if (Protect.checkLoadImageStatus(mContext)) {
//            Glide.with(mContext).load(url).placeholder(loadImg).error(loadImg).into((ImageView) getView(viewId));
//        }
        return this;
    }

    /**
     * 添加一个圆形图片
     *
     * @param viewId
     * @param url
     * @param loadImg
     * @param context
     * @return
     */
    public ViewHolder setClrcleImageByUrl(int viewId, String url, int loadImg, Context context) {
        if (context instanceof Activity) {
            Activity act = (Activity) context;
            if (act == null || act.isFinishing()) {
                return this;
            }
        }
//        if (Protect.checkLoadImageStatus(mContext)) {
//            Glide.with(mContext).load(url).transform(new GlideCircleImage(context)).
//                    placeholder(loadImg).error(loadImg).crossFade().into((ImageView) getView(viewId));
//        }
        return this;
    }

    /**
     * 添加一个四角图片
     *
     * @param viewId
     * @param url
     * @param loadImg
     * @param context
     * @return
     */
    public ViewHolder setRoundImageByUrl(int viewId, String url, int loadImg, Context context) {
        if (context instanceof Activity) {
            Activity act = (Activity) context;
            if (act == null || act.isFinishing()) {
                return this;
            }
        }
        if (Protect.checkLoadImageStatus(mContext)) {
            Glide.with(mContext).load(url).transform(new GlideRoundImage(context)).
                    placeholder(loadImg).error(loadImg).crossFade().into((ImageView) getView(viewId));
        }
        return this;
    }

    /**
     * 添加一个中间圆
     *
     * @param viewId
     * @param url
     * @param loadImg
     * @param context
     * @return
     */
    public ViewHolder setRoundCenterImageByUrl(int viewId, String url, int loadImg, Context context) {
        if (context instanceof Activity) {
            Activity act = (Activity) context;
            if (act == null || act.isFinishing()) {
                return this;
            }
        }
//        if (Protect.checkLoadImageStatus(mContext)) {
//            Glide.with(mContext).load(url).transform(new GlideCenterRoundImage(context)).
//                    placeholder(loadImg).error(loadImg).crossFade().into((ImageView) getView(viewId));
//        }
        return this;
    }

    /**
     * 根据控件id返回控件
     *
     * @param viewId
     * @return
     */
    public View getViewById(int viewId) {
        View view = getView(viewId);
        return view;
    }

}
