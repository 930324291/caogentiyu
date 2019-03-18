package com.football.net.ui.shouye;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class FirstNewsAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<NewsBean> dataList = new ArrayList<NewsBean>();

    public FirstNewsAdapter(Context context, ArrayList<NewsBean> newsList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        dataList = newsList;
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            if (dataList.size() < 7) {
                return dataList.size();
            } else {
                return 6;
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_first_news, null);
            vHolder = new ViewHolder();
            vHolder.linRoot = (LinearLayout) convertView.findViewById(R.id.lin_root);
            vHolder.iv_news_icon = (ImageView) convertView.findViewById(R.id.iv_news_icon);
            vHolder.title = (TextView) convertView.findViewById(R.id.title);
            vHolder.timeView = (TextView) convertView.findViewById(R.id.timeView);
            vHolder.contentView = (TextView) convertView.findViewById(R.id.contentView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            vHolder.linRoot.setBackgroundResource(R.mipmap.item_bg1);
        } else {
            vHolder.linRoot.setBackgroundResource(R.mipmap.item_bg2);
        }
//        ImageLoader.getInstance().displayImage();
        NewsBean bean = dataList.get(position);

        if (!StringUtils.isEmpty(bean.getThumbnail())) {
            vHolder.iv_news_icon.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(HttpUrlConstant.SERVER_URL+bean.getThumbnail()).crossFade().into(vHolder.iv_news_icon);
        } else {
            Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(vHolder.iv_news_icon);
        }

        /*
        if (StringUtils.isEmpty(bean.getThumbnail())) {
            vHolder.iv_news_icon.setVisibility(View.GONE);
        } else {
            vHolder.iv_news_icon.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(HttpUrlConstant.SERVER_URL+bean.getThumbnail()).crossFade().into(vHolder.iv_news_icon);
        }
         */


        vHolder.title.setText(bean.getTitle());
        vHolder.timeView.setText(CommonUtils.getDateStr(bean.getStartTime(), "yyyy年MM月dd日 HH:mm"));
        CharSequence charSequence = Html.fromHtml(bean.getContent());
        String content = charSequence.toString();
        if (content.length() > 50) {   // 这里修改为显示新闻内容
            vHolder.contentView.setText(content.substring(0, 50) + "...");
        } else {
            vHolder.contentView.setText(content);
        }

        return convertView;
    }

    public static class ViewHolder {
        LinearLayout linRoot;
        ImageView iv_news_icon;
        TextView title, timeView, contentView;
    }
}
