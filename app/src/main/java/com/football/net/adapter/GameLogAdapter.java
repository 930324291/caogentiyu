package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.bean.GameLogBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GameLogAdapter extends UltimateViewAdapter<GameLogAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    private int mType;
    ArrayList<GameLogBean> dataList = new ArrayList<GameLogBean>();

    public GameLogAdapter(Context context, ArrayList<GameLogBean> data, int type) {
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
                .inflate(R.layout.item_football_game2, parent, false);
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
        TextView tvBiFen1, tvBiFen2, nameA, nameB, timeTv, address, statusTv;
        ImageView imageA, imageB, imgResult;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (LinearLayout) itemView.findViewById(R.id.lin_root);
            tvBiFen1 = (TextView) itemView.findViewById(R.id.tv_bifen1);
            tvBiFen2 = (TextView) itemView.findViewById(R.id.tv_bifen2);
            nameA = (TextView) itemView.findViewById(R.id.nameA);
            nameB = (TextView) itemView.findViewById(R.id.nameB);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            address = (TextView) itemView.findViewById(R.id.address);
            statusTv = (TextView) itemView.findViewById(R.id.id_status);
            imageA = (ImageView) itemView.findViewById(R.id.imageA);
            imageB = (ImageView) itemView.findViewById(R.id.imageB);
            imgResult = (ImageView) itemView.findViewById(R.id.iv_result);
        }

        private void bindView(GameLogBean logBean) {
            final GameBean gameBean = logBean.getGame();
            nameA.setText(gameBean.getTeamA().getTeamTitle());
            nameB.setText(gameBean.getTeamB().getTeamTitle());
            timeTv.setText(CommonUtils.getDateStr(gameBean.getBeginTime(), "yyyy-MM-dd HH:mm"));
            address.setText(gameBean.getAddress());
            statusTv.setText(gameBean.getTitle());
            if (mType == 0) {
                tvBiFen1.setText("?");
                tvBiFen2.setText("?");
                imgResult.setVisibility(View.INVISIBLE);
            } else {
                tvBiFen1.setText(StringUtils.isEmpty(gameBean.getScoreA()) ? " -- " : gameBean.getScoreA());
                tvBiFen2.setText(StringUtils.isEmpty(gameBean.getScoreB()) ? " -- " : gameBean.getScoreB());
                imgResult.setVisibility(View.VISIBLE);

                //teamA是否是自己的球队
                boolean isMyteamIsA = false;
                if (null != gameBean.getTeam()){
                    if (gameBean.getTeamA().getId() == gameBean.getTeam().getId()){
                        isMyteamIsA = true;
                    }
                }
                int scoreANum = Integer.valueOf(StringUtils.isEmpty(gameBean.getScoreA()) ? "-1" : gameBean.getScoreA());
                int scoreBNum = Integer.valueOf(StringUtils.isEmpty(gameBean.getScoreB()) ? "-1" : gameBean.getScoreB());
                if (scoreANum == scoreBNum) {
                    imgResult.setImageResource(R.mipmap.draw);
                } else if (scoreANum < scoreBNum) {
                    if (isMyteamIsA) {
                        imgResult.setImageResource(R.mipmap.lose);
                    } else {
                        imgResult.setImageResource(R.mipmap.win);
                    }
                } else {
                    if (isMyteamIsA) {
                        imgResult.setImageResource(R.mipmap.win);
                    } else {
                        imgResult.setImageResource(R.mipmap.lose);
                    }
                }
            }
            ImageLoader.getInstance().displayImage( CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()), imageA, FootBallApplication.options);
            imageA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == gameBean.getTeamA()) {
                        ToastUtil.show(mContext, "没有球队");
                        return;
                    }
                    Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                    intent.putExtra("teamBean", gameBean.getTeamA());
                    mContext.startActivity(intent);
                }
            });
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()), imageB, FootBallApplication.options);
            imageB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == gameBean.getTeamB()) {
                        ToastUtil.show(mContext, "没有球队");
                        return;
                    }
                    Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                    intent.putExtra("teamBean", gameBean.getTeamB());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
