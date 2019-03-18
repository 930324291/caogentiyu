package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.Constant;
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
public class FootballTeamMemberAdapter extends UltimateViewAdapter<FootballTeamMemberAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    ArrayList<UserBean> dataList = new ArrayList<UserBean>();

    public FootballTeamMemberAdapter(Context context,  ArrayList<UserBean> data) {
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
                .inflate(R.layout.item_football_team_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getAdapterItemCount() {
        if(dataList != null){
            return  dataList.size();
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
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.bindView(dataList.get(position));
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
        TextView name,bodyHeight,bodyWeight,valueView,positionInTeam,idNum,winPercent;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            name = (TextView) itemView.findViewById(R.id.name);
            bodyHeight = (TextView) itemView.findViewById(R.id.bodyHeight);
            bodyWeight = (TextView) itemView.findViewById(R.id.bodyWeight);
            valueView = (TextView) itemView.findViewById(R.id.valueView);
            positionInTeam = (TextView) itemView.findViewById(R.id.positionInTeam);
            idNum = (TextView) itemView.findViewById(R.id.idNum);
            winPercent = (TextView) itemView.findViewById(R.id.winPercent);
        }

        private void bindView(final UserBean bean){
            name.setText(TextUtils.isEmpty(bean.getName())? "暂无": bean.getName());
            bodyHeight.setText(StringUtils.isEmpty(bean.getHeight())? "":bean.getHeight()+"CM");
            bodyWeight.setText(StringUtils.isEmpty(bean.getWeight())? "":bean.getWeight()+"KG");
            valueView.setText("$"+bean.getValue());
            positionInTeam.setText(CommonUtils.getPositionStr(bean.getPosition()));
            idNum.setText(bean.getPoint()+"");
            winPercent.setText(CommonUtils.getGamePercent(bean.getWin(),bean.getTotal())+"%");
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getIconUrl()), imageview, FootBallApplication.options);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                        Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                        intent.putExtra("beanid", bean.getId());
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                        intent.putExtra("beanid", bean.getId());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

}
