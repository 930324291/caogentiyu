package com.football.net.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;

public class NewsAdapter extends UltimateViewAdapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<NewsBean> dataList;
    private OnItemClickLitener mOnItemClickLitener;

    public NewsAdapter(Context context, ArrayList<NewsBean> data) {
        mContext = context;
        dataList = data;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_first_news, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public int getAdapterItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
        } else {
            holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            }
        });
        holder.bindview(position, dataList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        LinearLayout linRoot;
        ImageView iv_news_icon;
        TextView title, timeView, contentView;


        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
                iv_news_icon = (ImageView) itemView.findViewById(R.id.iv_news_icon);
                title = (TextView) itemView.findViewById(R.id.title);
                timeView = (TextView) itemView.findViewById(R.id.timeView);
                contentView = (TextView) itemView.findViewById(R.id.contentView);

            }
        }

        private void bindview(int position, NewsBean bean) {
            if (!StringUtils.isEmpty(bean.getThumbnail())) {
                iv_news_icon.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(HttpUrlConstant.SERVER_URL+bean.getThumbnail()).crossFade().into(iv_news_icon);
            } else {
                Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(iv_news_icon);
            }

            /*
            if (StringUtils.isEmpty(bean.getThumbnail())) {
                iv_news_icon.setVisibility(View.GONE);
            } else {
                iv_news_icon.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(HttpUrlConstant.SERVER_URL+bean.getThumbnail()).crossFade().into(iv_news_icon);
            }*/

            title.setText(bean.getTitle());
            timeView.setText(CommonUtils.getDateStr(bean.getStartTime(), "yyyy年MM月dd日 HH:mm"));
            CharSequence charSequence = Html.fromHtml(bean.getContent());
            String content = charSequence.toString();
            if (content.length() > 50) {   // 这里修改为显示新闻内容
                contentView.setText(content.substring(0, 50) + "...");
            } else {
                contentView.setText(content);
            }
            // ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getContent()), iv_news_icon, FootBallApplication.options);
        }
    }

}
