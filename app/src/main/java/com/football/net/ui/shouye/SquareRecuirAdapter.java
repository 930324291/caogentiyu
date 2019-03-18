package com.football.net.ui.shouye;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.RecruitBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SquareRecuirAdapter extends UltimateViewAdapter<SquareRecuirAdapter.ViewHolder> {

    private Context mContext;
    private  ArrayList<RecruitBean> dataList;
    private OnItemClickLitener mOnItemClickLitener;
    public SquareRecuirAdapter(Context context, ArrayList<RecruitBean> data) {
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
                    if(mOnItemClickLitener != null){
                        mOnItemClickLitener.onItemClick(v,position);
                    }
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
        TextView titletv,contentV,timeview,status;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if(isItem){
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                teamType = (ImageView) itemView.findViewById(R.id.teamType);
                titletv = (TextView) itemView.findViewById(R.id.titletv);
                contentV = (TextView) itemView.findViewById(R.id.contentV);
                timeview = (TextView) itemView.findViewById(R.id.timeview);
                status = (TextView) itemView.findViewById(R.id.status);
            }
        }


        private void bindview(int position,RecruitBean bean){
            if (!StringUtils.isEmpty(bean.getTeam().getIconUrl())) {
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getTeam().getIconUrl()),imageview, FootBallApplication.options);
            } else {
                // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic2.png", imageview, FootBallApplication.options);
                Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(imageview);
            }

            titletv.setText(bean.getTitle());
            teamType.setImageResource(CommonUtils.getTeamTypeImage(bean.getTeam().getTeamType()));
//            String timeTxt = CommonUtils.getDateStr(bean.get,"yy-MM-dd HH:mm");
//            long diff = System.currentTimeMillis() - bean.getCreateTime();
//            if(diff > 0 ){
//               long minute = diff/1000/60;
//                if(minute < 5){
//                    timeTxt = "刚刚";
//                }
//            }
            timeview.setText(CommonUtils.getFullTime(bean.getOpTime()));
            contentV.setText((bean.getContent() == null?"":bean.getContent()));

            String statusStr = "";
            if (bean.getConfirmStatus()==null) {
                statusStr = "等待球员回复";
            } else if (bean.getConfirmStatus()==1) {
                statusStr = "已入队";
            } else {
                statusStr = "已放弃入队";
            }
            status.setText(statusStr);
        }
    }
}
