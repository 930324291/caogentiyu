package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.football.net.bean.CommentBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class CommentAdapter extends UltimateViewAdapter<CommentAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();

    public CommentAdapter(Context context, ArrayList<CommentBean>  data) {
        mContext = context;
        dataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener onitemClickListener) {
        this.mOnitemClickListener = onitemClickListener;
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.linRoot.setBackgroundResource(R.mipmap.team_infor_bg2);
            } else {
                holder.linRoot.setBackgroundResource(R.mipmap.team_infor_bg3);
            }
            holder.bindview(dataList.get(position));
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

        LinearLayout linRoot;
        ImageView imageview;
        TextView username,timeV,comment;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            username = (TextView) itemView.findViewById(R.id.username);
            timeV = (TextView) itemView.findViewById(R.id.timeV);
            comment = (TextView) itemView.findViewById(R.id.comment);
        }

        private void bindview(final CommentBean bean){
            timeV.setText(CommonUtils.getDateStr(bean.getOpTime(),"yyyy-MM-dd HH:mm"));
            username.setText(bean.getPlayer().getName());
            comment.setText(Html.fromHtml(bean.getComment()));

            if (!StringUtils.isEmpty(bean.getPlayer().getIconUrl())) {
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+ CommonUtils.getRurl(bean.getPlayer().getIconUrl()),imageview, FootBallApplication.options);
            } else {
                // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic2.png", imageview, FootBallApplication.options);
                Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(imageview);
            }

            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                        Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                        intent.putExtra("beanid", bean.getPlayer().getId());
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                        intent.putExtra("beanid", bean.getPlayer().getId());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

}
