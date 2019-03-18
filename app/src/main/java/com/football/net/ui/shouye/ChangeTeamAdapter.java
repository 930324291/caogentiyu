package com.football.net.ui.shouye;

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

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.TransferBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class ChangeTeamAdapter extends UltimateViewAdapter<ChangeTeamAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    ArrayList<TransferBean> dataList = new ArrayList<TransferBean>();

    public ChangeTeamAdapter(Context context, ArrayList<TransferBean> data) {
        mContext = context;
        this.dataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener onitemClickListener) {
        this.mOnitemClickListener = onitemClickListener;
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_change_team, parent, false);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.linRoot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.semi_transparent));
            } else {
                holder.linRoot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
            }
            holder.bindView(dataList.get(position));
            if(mOnitemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnitemClickListener.onItemClick(holder.itemView,position);
                    }
                });
            }
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

        LinearLayout linRoot,layout1,layout2;
        ImageView imageA,imageB,imageHeader;
        TextView nameA,nameB,timeA,timeB,memberName;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
            layout1 = (LinearLayout) itemView.findViewById(R.id.layout1);
            layout2 = (LinearLayout) itemView.findViewById(R.id.layout2);
            imageA = (ImageView) itemView.findViewById(R.id.imageA);
            imageB = (ImageView) itemView.findViewById(R.id.imageB);
            imageHeader = (ImageView) itemView.findViewById(R.id.imageHeader);
            nameA = (TextView) itemView.findViewById(R.id.nameA);
            nameB = (TextView) itemView.findViewById(R.id.nameB);
            timeA = (TextView) itemView.findViewById(R.id.timeA);
            timeB = (TextView) itemView.findViewById(R.id.timeB);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
        }

        private void bindView(final TransferBean bean){
           if(bean.getFromTeam() != null){
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+ CommonUtils.getRurl(bean.getFromTeam().getIconUrl()), imageA, FootBallApplication.options);
                nameA.setText(bean.getFromTeam().getTeamTitle());
               layout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null == bean.getFromTeam()) {
                            ToastUtil.show(mContext, "没有球队");
                            return;
                        }
                        /*Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
                        intent.putExtra("beandata",bean.getFromTeam());
                        mContext.startActivity(intent);*/

                        // 调出球队战术板
//                        Intent intent = new Intent(mContext,TeamDetialActivity.class);
                        Intent intent = new Intent(mContext,TeamDetialActivity2.class);
                        intent.putExtra("teamBean",bean.getFromTeam());
                        mContext.startActivity(intent);
                    }
                });
            }else{
               layout1.setOnClickListener(null);
           }
            if(bean.getToTeam() != null){
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+ CommonUtils.getRurl(bean.getToTeam().getIconUrl()), imageB, FootBallApplication.options);
                nameB.setText(bean.getToTeam().getTeamTitle());
                layout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null == bean.getToTeam()) {
                            ToastUtil.show(mContext, "没有球队");
                            return;
                        }
                        /*Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
                        intent.putExtra("beandata",bean.getToTeam());
                        mContext.startActivity(intent);*/

                        // 调出球队战术板
//                        Intent intent = new Intent(mContext,TeamDetialActivity.class);
                        Intent intent = new Intent(mContext,TeamDetialActivity2.class);
                        intent.putExtra("teamBean",bean.getToTeam());
                        mContext.startActivity(intent);
                    }
                });
            }else{
                layout2.setOnClickListener(null);
            }
            if(bean.getFromTime() == 0){
                timeA.setText("暂无");
            }else{
                timeA.setText(CommonUtils.getDateStr(bean.getFromTime(),"yyyy-MM-dd"));
            }
            timeB.setText(CommonUtils.getDateStr(bean.getToTime(),"yyyy-MM-dd"));
            if(bean.getPlayer()!= null){
                if (!StringUtils.isEmpty(bean.getPlayer().getIconUrl())) {
                    ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getPlayer().getIconUrl()), imageHeader, FootBallApplication.circOptions);
                } else {
                    // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic.png", imageHeader, FootBallApplication.circOptions);
                    Glide.with(mContext).load(R.mipmap.nopic).crossFade().into(imageHeader);
                }

                imageHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember){
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid",bean.getPlayer().getId());
                            mContext.startActivity(intent);
                        }else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid",bean.getPlayer().getId());
                            mContext.startActivity(intent);

                        }
                    }
                });
                if(!TextUtils.isEmpty(bean.getPlayer().getName())) {
                    memberName.setText(bean.getPlayer().getName());
                }
            }
        }
    }

}
