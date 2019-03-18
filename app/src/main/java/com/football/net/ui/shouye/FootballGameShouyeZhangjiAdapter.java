package com.football.net.ui.shouye;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class FootballGameShouyeZhangjiAdapter extends UltimateViewAdapter<FootballGameShouyeZhangjiAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    private int mType;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();

    private int id;

    public FootballGameShouyeZhangjiAdapter(Context context, ArrayList<GameBean> data, int type, int id) {
        mContext = context;
        mType = type;
        dataList = data;
        this.id = id;
    }

    public FootballGameShouyeZhangjiAdapter(Context context, ArrayList<GameBean> data, int type) {
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
                .inflate(R.layout.item_football_shouye_zhangji, parent, false);
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
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.semi_transparent));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
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

        ImageView imageA, imageB, image3;
        TextView teamNameA, teamNameB, timeV, address, scoreA, scoreB, gameName;

        public ViewHolder(View itemView) {
            super(itemView);
            gameName = (TextView) itemView.findViewById(R.id.gameName);
            imageA = (ImageView) itemView.findViewById(R.id.imageA);
            imageB = (ImageView) itemView.findViewById(R.id.imageB);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            teamNameA = (TextView) itemView.findViewById(R.id.teamNameA);
            teamNameB = (TextView) itemView.findViewById(R.id.teamNameB);
            timeV = (TextView) itemView.findViewById(R.id.timeV);
            address = (TextView) itemView.findViewById(R.id.address);
            scoreA = (TextView) itemView.findViewById(R.id.scoreA);
            scoreB = (TextView) itemView.findViewById(R.id.scoreB);
        }

        private void bindView(final GameBean gameBean) {
            gameName.setText(gameBean.getTitle());
            teamNameA.setText(gameBean.getTeamA().getTeamTitle());
            teamNameB.setText(gameBean.getTeamB().getTeamTitle());
            timeV.setText(CommonUtils.getDateStr(gameBean.getBeginTime(), "yyyy-MM-dd HH:mm"));
            address.setText(gameBean.getAddress());
            scoreA.setText(gameBean.getScoreA() + "");
            scoreB.setText(gameBean.getScoreB() + "");
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()), imageA, FootBallApplication.options);
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()), imageB, FootBallApplication.options);
            boolean isMyteamIsA = false;
            if (gameBean.getTeamA().getId() == id) {
                isMyteamIsA = true;
            }
            int scoreANum = Integer.valueOf(gameBean.getScoreA() == null ? "-1" : gameBean.getScoreA());
            int scoreBNum = Integer.valueOf(gameBean.getScoreB() == null ? "-1" : gameBean.getScoreB());
            if (scoreANum == -1 || scoreBNum == -1) {
                image3.setVisibility(View.INVISIBLE);
            } else {
                if (scoreANum > scoreBNum) {
                    image3.setVisibility(View.VISIBLE);
                    if (isMyteamIsA) {
                        image3.setImageResource(R.mipmap.win);
                    } else {
                        image3.setImageResource(R.mipmap.lose);
                    }
                } else if (scoreANum < scoreBNum) {

                    if (isMyteamIsA) {
                        image3.setImageResource(R.mipmap.lose);
                    } else {
                        image3.setImageResource(R.mipmap.win);
                    }
                } else if (scoreANum == scoreBNum) {
                    image3.setImageResource(R.mipmap.draw);
                }
            }

            /*  球队档案中不再允许查看球队详情战术板
            imageA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,TeamDetialActivity.class);
                    intent.putExtra("teamBean",gameBean.getTeamA());
                    mContext.startActivity(intent);
                }
            });
            imageB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,TeamDetialActivity.class);
                    intent.putExtra("teamBean",gameBean.getTeamB());
                    mContext.startActivity(intent);
                }
            });*/
        }
    }

}
