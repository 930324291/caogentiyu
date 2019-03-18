package com.football.net.ui.shouye;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.CommStatus;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.GameDetailAty;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 * 首页比赛项目适配器
 */
public class ShoyeFootballGameAdapter extends UltimateViewAdapter<ShoyeFootballGameAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;  // 项目点击监听器
    private Context mContext; // 上下文
    private int mType;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();

    private int mSaveId = -1;

    public ShoyeFootballGameAdapter(Context context, ArrayList<GameBean> data, int type) {
        mContext = context;
        mType = type;
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
                .inflate(R.layout.item_shouye_football_game, parent, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= dataList.size() : position < dataList.size()) && (customHeaderView != null ? position > 0 : true)) {
            if (position % 2 == 0) {
                holder.linRoot.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.linRoot.setBackgroundResource(R.mipmap.item_bg2);
            }
            GameBean gameBean = dataList.get(position);
            if (mType == 0) {
                mSaveId = CommStatus.MNEWSGAMEID;
            } else {
                mSaveId = CommStatus.MHISGAMEID;
            }
            if (mSaveId == gameBean.getId()) {
                holder.mChooseLinear.setBackgroundResource(R.color.bg_FFDEAD);
            } else {
                holder.mChooseLinear.setBackgroundResource(R.color.transparent);
            }
            holder.bindView(gameBean);
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

        LinearLayout linRoot, bifenview, mChooseLinear,mShowDetail;
        TextView tvBiFen1, tvBiFen2, nameA, nameB, timeTv, address, gameName;
        ImageView imageA, imageB, lingdang,teamType;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
            mChooseLinear = (LinearLayout) itemView.findViewById(R.id.id_choose_linear);
            mShowDetail = (LinearLayout) itemView.findViewById(R.id.id_show_detail_linear);
            tvBiFen1 = (TextView) itemView.findViewById(R.id.tv_bifen1);
            tvBiFen2 = (TextView) itemView.findViewById(R.id.tv_bifen2);
            nameA = (TextView) itemView.findViewById(R.id.nameA);
            nameB = (TextView) itemView.findViewById(R.id.nameB);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            address = (TextView) itemView.findViewById(R.id.address);
            imageA = (ImageView) itemView.findViewById(R.id.imageA);
            imageB = (ImageView) itemView.findViewById(R.id.imageB);
            gameName = (TextView) itemView.findViewById(R.id.gameName);
            teamType = (ImageView) itemView.findViewById(R.id.teamType);
            bifenview = (LinearLayout) itemView.findViewById(R.id.bifenview);
            lingdang = (ImageView) itemView.findViewById(R.id.lingdang);
        }

        private void bindView(final GameBean gameBean) {

            if (gameBean.getTeamA() != null) {
                imageA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null == gameBean.getTeamA()) {
                            ToastUtil.show(mContext, "没有球队");
                            return;
                        }
                        // 调出球队战术板
//                        Intent intent = new Intent(mContext, TeamDetialActivity.class);
                        Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                        intent.putExtra("teamBean", gameBean.getTeamA());
                        mContext.startActivity(intent);
                    }
                });
            }

            if (gameBean.getTeamB() != null) {
                imageB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null == gameBean.getTeamB()) {
                            ToastUtil.show(mContext, "没有球队");
                            return;
                        }
                        // 调出球队战术板
//                        Intent intent = new Intent(mContext, TeamDetialActivity.class);
                        Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                        intent.putExtra("teamBean", gameBean.getTeamB());
                        mContext.startActivity(intent);
                    }
                });
            }

            mShowDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GameDetailAty.class);
                    intent.putExtra(IntentKey.General.KEY_MODEL, gameBean);
                    intent.putExtra(IntentKey.General.KEY_TYPE, mType);
                    mContext.startActivity(intent);
                }
            });

            nameA.setText(gameBean.getTeamA().getTeamTitle());
            nameB.setText((gameBean.getTeamB() == null||gameBean.getTeamB().getId()==0) ? "未知" : gameBean.getTeamB().getTeamTitle());
            gameName.setText(gameBean.getTitle());
            timeTv.setText(CommonUtils.getDateStr(gameBean.getBeginTime(), "yyyy-MM-dd HH:mm"));
            address.setText(gameBean.getAddress());
            teamType.setImageResource(CommonUtils.getTeamTypeImage(gameBean.getTeamType()));
            if (mType == 0) {
                tvBiFen1.setText("?");
                tvBiFen2.setText("?");
                lingdang.setVisibility(View.VISIBLE);
                bifenview.setVisibility(View.GONE);
            } else {
                lingdang.setVisibility(View.GONE);
                bifenview.setVisibility(View.VISIBLE);
                tvBiFen1.setText(gameBean.getScoreA() == null ? "--" : gameBean.getScoreA() + "");
                tvBiFen2.setText(gameBean.getScoreB() == null ? "--" : gameBean.getScoreB() + "");
            }
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()), imageA, FootBallApplication.options);
            if (gameBean.getTeamB() == null||gameBean.getTeamB().getId()==0) {
                Glide.with(mContext).load(R.mipmap.icon_unknow).crossFade().into(imageB);
                // ImageLoader.getInstance().displayImage("http://football001.com/web/img/icon_unknow.png", imageB, FootBallApplication.options);
            } else {
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()), imageB, FootBallApplication.options);
            }
        }
    }

}
