package com.football.net.ui.shouye;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.ApplyBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.FindTeaamDetailActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SquareApplyAdapter extends UltimateViewAdapter<SquareApplyAdapter.ViewHolder> {

    private Context mContext;
    private  ArrayList<ApplyBean> dataList;
    public SquareApplyAdapter(Context context, ArrayList<ApplyBean> data) {
        mContext = context;
        dataList = data;
    }


    @Override
    public ViewHolder newFooterHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_square_recruit, parent, false);
        return new ViewHolder(view,true);
    }

    @Override
    public int getAdapterItemCount() {
        if(dataList != null){
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
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FindTeaamDetailActivity.class);
                    intent.putExtra("beandata",dataList.get(position));
                    mContext.startActivity(intent);

                }
            });
            holder.bindview(position,dataList.get(position));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView imageview,teamType;
        TextView titletv,contentV,timeview;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                teamType = (ImageView) itemView.findViewById(R.id.teamType);
                titletv = (TextView) itemView.findViewById(R.id.titletv);
                contentV = (TextView) itemView.findViewById(R.id.contentV);
                timeview = (TextView) itemView.findViewById(R.id.timeview);
            }
        }


        private void bindview(int position,ApplyBean bean){
            if(bean.getPlayer() != null){
                if (!StringUtils.isEmpty(bean.getPlayer().getIconUrl())) {
                    ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getPlayer().getIconUrl()),imageview, FootBallApplication.circOptions);
                } else {
                    // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic.png", imageview, FootBallApplication.circOptions);
                    Glide.with(mContext).load(R.mipmap.nopic).crossFade().into(imageview);
                }
            }else{
                imageview.setImageResource(R.color.bg_FFA19D9D);
            }

            titletv.setText(bean.getTitle());
            if(bean.getDreamType() != null){
                teamType.setVisibility(View.VISIBLE);
                teamType.setImageResource(CommonUtils.getTeamTypeImage(bean.getDreamType()));
            }else{
                teamType.setVisibility(View.INVISIBLE);
            }
            contentV.setText(bean.getContent());
            String timeTxt = CommonUtils.getDateStr(bean.getApplyTime(),"yyyy-MM-dd HH:mm");
            long diff = System.currentTimeMillis() - bean.getApplyTime();
            if(diff > 0 ){
               long minute = diff/1000/60;
                if(minute < 15){
                    timeTxt = "刚刚";
                }
            }
            timeview.setText(timeTxt);
            contentV.setText(bean.getContent()); // 找队
        }
    }

}
