package com.football.net.ui.shouye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnButtonClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.GameDetailAty;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class SquareAppointmentAdapter extends UltimateViewAdapter<SquareAppointmentAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<GameBean> dataList;
    private OnButtonClickListener mOnButtonClickListener;

    public SquareAppointmentAdapter(Context context, ArrayList<GameBean> data) {
        mContext = context;
        dataList = data;
    }

    public void setmOnButtonClickListener(OnButtonClickListener mOnButtonClickListener) {
        this.mOnButtonClickListener = mOnButtonClickListener;
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
                .inflate(R.layout.item_square_appointment, parent, false);
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
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }

            holder.bindview(position, dataList.get(position));
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

        LinearLayout mLinear;
        ImageView image1, teamType;
        TextView nameA, timeV, address, nameB, answerBtn, gameStatus;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                mLinear = (LinearLayout) itemView.findViewById(R.id.id_jump_detail_linear);
                image1 = (ImageView) itemView.findViewById(R.id.image1);
                teamType = (ImageView) itemView.findViewById(R.id.teamType);
                nameA = (TextView) itemView.findViewById(R.id.nameA);
                nameB = (TextView) itemView.findViewById(R.id.nameB);
                timeV = (TextView) itemView.findViewById(R.id.timeV);
                address = (TextView) itemView.findViewById(R.id.address);
                answerBtn = (TextView) itemView.findViewById(R.id.answerBtn);
                gameStatus = (TextView) itemView.findViewById(R.id.gameStatus);
            }
        }


        private void bindview(final int position, final GameBean bean) {
            String url = CommonUtils.getRurl(bean.getTeamA().getIconUrl());

            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + url, image1, FootBallApplication.options);
            nameA.setText(bean.getTeamA().getTeamTitle());
            timeV.setText(CommonUtils.getDateStr(bean.getBeginTime(), "yyyy-MM-dd HH:mm"));
            teamType.setImageResource(CommonUtils.getTeamTypeImage(bean.getTeamType()));
            address.setText(bean.getAddress());

            // 判断当前比赛的状态，并展示在TextView gameStatus中
            gameStatus.setText(CommonUtils.getGameStatus(bean));
            gameStatus.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

            // 显示B队名称
            if (bean.getTeamB()!=null) {
                nameB.setText(bean.getTeamB().getTeamTitle());
            } else {
                nameB.setText("未知");
            }

            answerBtn.setVisibility(View.GONE);

            // 如何显示出应战的按钮
            if (FootBallApplication.userbean!=null && FootBallApplication.userbean.getTeam()!=null && FootBallApplication.userbean.getIsCaptain()==1) {

                if (FootBallApplication.userbean.getTeam().getId()!= bean.getTeamA().getId()) {

                    if (bean.getTeamB()==null && bean.getBeginTime() > System.currentTimeMillis()) {
                        answerBtn.setVisibility(View.VISIBLE);
                        answerBtn.setBackgroundResource(R.color.txt_7dd9fd);
                        answerBtn.setText("应战");

                        answerBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mOnButtonClickListener != null) {
                                    mOnButtonClickListener.onItemClick(answerBtn, position);
                                }
                            }
                        });
                    }
                }
            }

            mLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GameDetailAty.class);
                    intent.putExtra(IntentKey.General.KEY_MODEL, bean);
                    intent.putExtra(IntentKey.General.KEY_TYPE, 1);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
