package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.football.net.common.util.ToastUtil;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class MineScoreListAdapter extends UltimateViewAdapter<MineScoreListAdapter.ScoreTeamAdapterViewHolder> {

    private ArrayList<GameBean> data;
    private OnItemClickLitener mOnItemClickLitener;
    private Context mContext;

    public MineScoreListAdapter( Context mContext,ArrayList<GameBean> msgList) {
        this.data = msgList;
        this.mContext = mContext;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public ScoreTeamAdapterViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ScoreTeamAdapterViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ScoreTeamAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mine_team_score, parent, false);
        ScoreTeamAdapterViewHolder vh = new ScoreTeamAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final ScoreTeamAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {
//            if(position % 2 == 0) {
//                holder.relCoupon.setVisibility(View.GONE);
//                holder.tvOtherMsg.setVisibility(View.VISIBLE);
//            }else {
//                holder.tvOtherMsg.setVisibility(View.GONE);
//                holder.relCoupon.setVisibility(View.VISIBLE);
//            }
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg1);
            } else {
                holder.itemView.setBackgroundResource(R.mipmap.item_bg2);
            }
            holder.bindView(data.get(position));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }



    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    public void swapPositions(int from, int to) {
        swapPositions(data, from, to);
    }


    public class ScoreTeamAdapterViewHolder extends UltimateRecyclerviewViewHolder {
        ImageView imageA,imageB,image3,teamType;
        TextView teamNameA,teamNameB,timeV,address,scoreA,scoreB;

        public ScoreTeamAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                 imageA = (ImageView) itemView.findViewById(R.id.imageA);
                imageB = (ImageView) itemView.findViewById(R.id.imageB);
                image3 = (ImageView) itemView.findViewById(R.id.image3);
                teamType = (ImageView) itemView.findViewById(R.id.teamType);

                teamNameA = (TextView) itemView.findViewById(R.id.teamNameA);
                teamNameB = (TextView) itemView.findViewById(R.id.teamNameB);
                timeV = (TextView) itemView.findViewById(R.id.timeV);
                address = (TextView) itemView.findViewById(R.id.address);
                scoreA = (TextView) itemView.findViewById(R.id.scoreA);
                scoreB = (TextView) itemView.findViewById(R.id.scoreB);
            }
        }

        public void bindView(final GameBean gameBean){

            teamType.setImageResource(CommonUtils.getTeamTypeImage(gameBean.getTeamA().getTeamType()));
            teamNameA.setText(gameBean.getTeamA().getTeamTitle());
            teamNameB.setText(gameBean.getTeamB().getTeamTitle());
            timeV.setText(CommonUtils.getDateStr(gameBean.getBeginTime(),"yyyy-MM-dd HH:mm"));
            address.setText(gameBean.getAddress());
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()),imageA, FootBallApplication.circOptions);
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()),imageB, FootBallApplication.circOptions);
            boolean isMyteamIsA = true;
//            if(gameBean.getTeamA().getId() != FootBallApplication.userbean.getTeam().getId()){
//                isMyteamIsA = false;
//            }

            int scoreANum = Integer.valueOf(gameBean.getScoreA() == null?"-1":gameBean.getScoreA());
            int scoreBNum = Integer.valueOf(gameBean.getScoreB() == null?"-1":gameBean.getScoreB());

            int scoreA1Num = Integer.valueOf(gameBean.getScoreA1() == null?"-1":gameBean.getScoreA1());
            int scoreB1Num = Integer.valueOf(gameBean.getScoreB1() == null?"-1":gameBean.getScoreB1());

            int scoreA2Num = Integer.valueOf(gameBean.getScoreA2() == null?"-1":gameBean.getScoreA2());
            int scoreB2Num = Integer.valueOf(gameBean.getScoreB2() == null?"-1":gameBean.getScoreB2());

            if(isMyteamIsA){
                if(scoreA1Num == -1 || scoreB1Num ==-1 ){
                    scoreA.setText("本队发布  "+ "-- : --");
                } else {
                    // 如果本队发布和审核发布一致，则本队发布不显示

                    if (scoreA1Num==scoreANum && scoreB1Num==scoreBNum) {
                        scoreA.setVisibility(View.GONE);
                    } else {
                        scoreA.setText("本队发布  "+ gameBean.getScoreA1() + " : " + gameBean.getScoreB1());
                    }

                }
            } else {
                if(scoreA2Num == -1 || scoreB2Num ==-1 ){
                    scoreA.setText("本队发布  "+ "-- : --");
                } else {
                    // 如果本队发布和审核发布一致，则本队发布不显示

                    if (scoreA2Num==scoreANum && scoreB2Num==scoreBNum) {
                        scoreA.setVisibility(View.GONE);
                    } else {
                        scoreA.setText("本队发布  "+ gameBean.getScoreA2() + " : " + gameBean.getScoreB2());
                    }
                }
            }

            if(scoreANum == -1 || scoreBNum ==-1 ){
                image3.setVisibility(View.INVISIBLE);
                scoreB.setText("审核发布  "+ "-- : --");
            }else{
                if(scoreANum>scoreBNum){
                    image3.setVisibility(View.VISIBLE);
                    if(isMyteamIsA){
                        image3.setImageResource(R.mipmap.win);
                    }else{
                        image3.setImageResource(R.mipmap.lose);
                    }

                } else if(scoreANum<scoreBNum){
                    if(isMyteamIsA){
                        image3.setImageResource(R.mipmap.lose);
                    }else{
                        image3.setImageResource(R.mipmap.win);
                    }

                } else if(scoreANum==scoreBNum) {
                    image3.setImageResource(R.mipmap.draw);
                }
//                if(gameBean.getScoreA1()==gameBean.getScoreB1()){
//                    image3.setImageResource(R.mipmap.draw);
//                }
                scoreB.setText("审核发布  "+ scoreANum+" : "+scoreBNum);
            }

            if(isMyteamIsA){
                scoreA.setText("本队发布  "+ (gameBean.getScoreA1()==null?"--":gameBean.getScoreA1())+" : " + gameBean.getScoreB1());
            }else{
                scoreA.setText("本队发布  "+ (gameBean.getScoreA2()==null?"--":gameBean.getScoreA2())+" : " + gameBean.getScoreB1());
            }


            imageA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == gameBean.getTeamA()) {
                        ToastUtil.show(mContext, "没有球队");
                        return;
                    }
//                    Intent intent = new Intent(mContext,TeamDetialActivity.class);
                    Intent intent = new Intent(mContext,TeamDetialActivity2.class);
                    intent.putExtra("teamBean",gameBean.getTeamA());
                    mContext.startActivity(intent);
                }
            });
            imageB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == gameBean.getTeamB()) {
                        ToastUtil.show(mContext, "没有球队");
                        return;
                    }
//                    Intent intent = new Intent(mContext,TeamDetialActivity.class);
                    Intent intent = new Intent(mContext,TeamDetialActivity2.class);
                    intent.putExtra("teamBean",gameBean.getTeamB());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
