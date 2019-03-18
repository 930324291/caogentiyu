package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnButtonClickListener;
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
public class InformAppointmentdapter extends UltimateViewAdapter<InformAppointmentdapter.MineAdapterViewHolder> {

    private OnItemClickListener mOnItemClickLitener;
    private OnButtonClickListener answerOnButtonClickListener;
    private OnButtonClickListener rejectOnButtonClickListener;
    private ArrayList<GameBean> dataList;

    private Context mContext;

    public InformAppointmentdapter(ArrayList<GameBean> data) {
        dataList = data;
    }

    public InformAppointmentdapter(Context mContext, ArrayList<GameBean> data) {
        this.mContext = mContext;
        dataList = data;
    }

    public void setOnItemClickLitener(OnItemClickListener onItemClickLitener) {
        this.mOnItemClickLitener = onItemClickLitener;
    }

    public void setAnswerOnButtonClickListener(OnButtonClickListener answerOnButtonClickListener) {
        this.answerOnButtonClickListener = answerOnButtonClickListener;
    }

    public void setRejectOnButtonClickListener(OnButtonClickListener rejectOnButtonClickListener) {
        this.rejectOnButtonClickListener = rejectOnButtonClickListener;
    }

    @Override
    public MineAdapterViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MineAdapterViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public MineAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inform_appointment, parent, false);
        MineAdapterViewHolder vh = new MineAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final MineAdapterViewHolder holder, final int position) {
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
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }


    public class MineAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView image1, teamType, image2;
        TextView nameA, timeV, address, nameB, gameStatus, answerBtn, rejectBtn;

        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                image1 = (ImageView) itemView.findViewById(R.id.image1);
                image2 = (ImageView) itemView.findViewById(R.id.id_img_2);
                teamType = (ImageView) itemView.findViewById(R.id.teamType);
                nameA = (TextView) itemView.findViewById(R.id.nameA);
                timeV = (TextView) itemView.findViewById(R.id.timeV);
                address = (TextView) itemView.findViewById(R.id.address);
                nameB = (TextView) itemView.findViewById(R.id.nameB);
                gameStatus = (TextView) itemView.findViewById(R.id.gameStatus);
                answerBtn = (TextView) itemView.findViewById(R.id.answerBtn);
                rejectBtn = (TextView) itemView.findViewById(R.id.rejectBtn);
            }
        }

        private void bindview(final int position, final GameBean bean) {
            if (null != bean.getTeamA()) {
                String url = CommonUtils.getRurl(bean.getTeamA().getIconUrl());
                ImageLoader.getInstance().displayImage(url, image1, FootBallApplication.options);
                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid", bean.getTeamA().getId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid", bean.getTeamA().getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
            if (null != bean.getTeamB()) {
                String url2 = CommonUtils.getRurl(bean.getTeamB().getIconUrl());
                Glide.with(mContext).load(url2).error(R.mipmap.icon_unknow).into(image2);
//                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + url2, image2, FootBallApplication.options);
                image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid", bean.getTeamB().getId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid", bean.getTeamB().getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
            nameA.setText(bean.getTeamA().getTeamTitle());
            timeV.setText(CommonUtils.getDateStr(bean.getBeginTime(), "yyyy-MM-dd HH:mm"));
            teamType.setImageResource(CommonUtils.getTeamTypeImage(bean.getTeamType()));
            address.setText(bean.getAddress());

            // 显示B队名称
            if (bean.getTeamB()!=null) {
                nameB.setText(bean.getTeamB().getTeamTitle());
            } else {
                nameB.setText("未知");
                // ImageLoader.getInstance().displayImage("http://football001.com/web/img/icon_unknow.png", image2, FootBallApplication.options);
                Glide.with(mContext).load(R.mipmap.icon_unknow).crossFade().into(image2);

            }

            // 判断当前比赛的状态，并展示在TextView gameStatus中
            gameStatus.setText(CommonUtils.getGameStatus(bean));
            gameStatus.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

            answerBtn.setVisibility(View.GONE);
            rejectBtn.setVisibility(View.GONE);

            // 判断同意按钮的出现
            // 如何显示出应战的按钮
            if (FootBallApplication.userbean!=null && FootBallApplication.userbean.getTeam()!=null && FootBallApplication.userbean.getIsCaptain()==1) {

                if (FootBallApplication.userbean.getTeam().getId()!= bean.getTeamA().getId()) {

                    // B队是当前登录队长所在球队，方可进行同意和拒绝操作
                    if (bean.getTeamB()!=null) {
                        if (bean.getTeamB().getId()==FootBallApplication.userbean.getTeam().getId()
                                && bean.getTeamBOperation()==null
                                && bean.getBeginTime() > System.currentTimeMillis()) {

                            answerBtn.setVisibility(View.VISIBLE);
                            answerBtn.setBackgroundResource(R.color.txt_7dd9fd);
                            answerBtn.setText("应战");

                            answerBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (answerOnButtonClickListener != null) {
                                        answerOnButtonClickListener.onItemClick(answerBtn, position);
                                    }
                                }
                            });

                            rejectBtn.setVisibility(View.VISIBLE);
                            rejectBtn.setBackgroundResource(R.color.bg_f15661);
                            rejectBtn.setText("拒绝");
                            rejectBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (rejectOnButtonClickListener != null) {
                                        rejectOnButtonClickListener.onItemClick(rejectBtn, position);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

}
