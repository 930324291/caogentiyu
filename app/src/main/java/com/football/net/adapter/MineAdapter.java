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
import com.football.net.bean.MessageBean;
import com.football.net.bean.MessageTankBean;
import com.football.net.bean.MineBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/27.
 */
public class MineAdapter extends UltimateViewAdapter<MineAdapter.MineAdapterViewHolder> {

    private ArrayList<MineBean> data;
    private OnItemClickLitener mOnItemClickLitener;
    Context mContext;

    public MineAdapter(Context mContext,ArrayList<MineBean> msgList) {
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
                .inflate(R.layout.item_mine, parent, false);
        MineAdapterViewHolder vh = new MineAdapterViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        if (data != null) {
            return data.size() >= 8?8:data.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final MineAdapterViewHolder holder, final int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= data.size() : position < data.size()) && (customHeaderView != null ? position > 0 : true)) {

            holder.bindView(position,data.get(position));
            if(position%2==0){
                holder.itemView.setBackgroundResource(R.mipmap.team_infor_bg2);
            }else{
                holder.itemView.setBackgroundResource(R.mipmap.team_infor_bg3);
            }
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


    public class MineAdapterViewHolder extends UltimateRecyclerviewViewHolder {

//        TextView relCoupon,commitBtn;
        View layout1,layout3,layout4;
        //photo video
        ImageView imageview,videoicon;
        TextView contentV,timeview,numb1,numb2;
        //MineJoinedExerciseActivity
        TextView exercise_titletv,exercise_contentV,exercise_timeview;
        //score
        ImageView imageA,imageB,image3;
        TextView teamNameA,teamNameB,timeV,address,scoreA,scoreB;

        public MineAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
//                commitBtn = (TextView) itemView.findViewById(R.id.commitBtn);
                layout1 = itemView.findViewById(R.id.photo_layout);
                layout3 = itemView.findViewById(R.id.exercise_Layout);
                layout4 = itemView.findViewById(R.id.score_layout);

                //photo video
                imageview = (ImageView) itemView.findViewById(R.id.imageview);
                videoicon = (ImageView) itemView.findViewById(R.id.videoicon);
                contentV = (TextView) itemView.findViewById(R.id.contentV);
                timeview = (TextView) itemView.findViewById(R.id.timeview);
                numb1 = (TextView) itemView.findViewById(R.id.numb1);
                numb2 = (TextView) itemView.findViewById(R.id.numb2);
                //MineJoinedExerciseActivity
                exercise_titletv = (TextView) itemView.findViewById(R.id.exercise_titletv);
                exercise_contentV = (TextView) itemView.findViewById(R.id.exercise_contentV);
                exercise_timeview = (TextView) itemView.findViewById(R.id.exercise_timeview);
                //score
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
        }

        void bindView(int position,MineBean bean){
            if(bean.getBeanType() == 1){
                setLayout(1);
                bindPhotoview(position, (SquarePhotoBean) bean);
            }else  if(bean.getBeanType() == 2){
                setLayout(2);
                bindVideoview(position, (SquareVideoBean) bean);
            }else  if(bean.getBeanType() == 3){
                setLayout(3);
                bindMessageTankview(position, (MessageTankBean) bean);
            }else  if(bean.getBeanType() == 4){
                setLayout(4);
                bindScoreView((GameBean) bean);
            }
        }

        void setLayout(int index){
            if(index == 1 || index ==2){
                layout1.setVisibility(View.VISIBLE);
            }else {
                layout1.setVisibility(View.GONE);
            }
            layout3.setVisibility(index == 3?View.VISIBLE:View.GONE);
            layout4.setVisibility(index == 4?View.VISIBLE:View.GONE);
        }
        private void bindPhotoview(int position,SquarePhotoBean bean){
            videoicon.setVisibility(View.GONE);
            if(bean.getPics() != null && bean.getPics().size() > 0){
                ImageLoader.getInstance().displayImage(CommonUtils.getRurl(bean.getPics().get(0).getUrl()),imageview, FootBallApplication.options);
            }

            contentV.setText(bean.getComment());
            String timeTxt = CommonUtils.getDateStr(bean.getCreateTime(),"yyyy-MM-dd HH:mm");
            long diff = System.currentTimeMillis() - bean.getCreateTime();
            if(diff > 0 ){
                long minute = diff/1000/60;
                if(minute < 5){
                    timeTxt = "刚刚";
                }
            }
            timeview.setText(timeTxt);

            bean.getLikes();
            numb1.setText(bean.getLikseNum()+"");
            numb2.setText(bean.getViewTimes() == null?"0":bean.getViewTimes());

        }

        private void bindVideoview(int position,SquareVideoBean bean){
            videoicon.setVisibility(View.VISIBLE);
            String url =bean.getScreenshot();
            ImageLoader.getInstance().displayImage(url,imageview, FootBallApplication.options);

            contentV.setText(bean.getComment());
//            String timeTxt = CommonUtils.getDateStr(bean.get,"yy-MM-dd HH:mm");
//            long diff = System.currentTimeMillis() - bean.getCreateTime();
//            if(diff > 0 ){
//               long minute = diff/1000/60;
//                if(minute < 5){
//                    timeTxt = "刚刚";
//                }
//            }
            timeview.setText("");
            numb1.setText(CommonUtils.getLiksNum(bean.getLikes())+"");
            numb2.setText(bean.getCommentCount()+"");

        }


        private void bindMessageTankview(int position,MessageTankBean tankBean){

            MessageBean bean = tankBean.getMessage();
            exercise_titletv.setText(bean.getTitle());
            exercise_contentV.setText(bean.getContent() == null?bean.getTitle():bean.getContent() );
            exercise_timeview.setText(CommonUtils.getDateStr(bean.getOpTime()));
            exercise_contentV.setText(bean.getContent());
        }

        public void bindScoreView(final GameBean gameBean){
            teamNameA.setText(gameBean.getTeamA().getTeamTitle());
            teamNameB.setText(gameBean.getTeamB().getTeamTitle());
            timeV.setText(CommonUtils.getDateStr(gameBean.getBeginTime(),"yyyy-MM-dd HH:mm"));
            address.setText(gameBean.getAddress());
            scoreA.setText(gameBean.getScoreA() == null ? " -- " : gameBean.getScoreA());
            scoreB.setText(gameBean.getScoreB() == null?" -- ":gameBean.getScoreB());
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()),imageA, FootBallApplication.circOptions);
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()),imageB, FootBallApplication.circOptions);
            boolean isMyteamIsA = true;
//            if(gameBean.getTeamA().getId() != FootBallApplication.userbean.getTeam().getId()){
//                isMyteamIsA = false;
//            }
            int scoreANum = Integer.valueOf(gameBean.getScoreA() == null?"-1":gameBean.getScoreA());
            int scoreBNum = Integer.valueOf(gameBean.getScoreB() == null?"-1":gameBean.getScoreB());


            /* 不显示胜负平
            if(scoreANum == -1 || scoreBNum ==-1 ){
                image3.setVisibility(View.INVISIBLE);
            }else{
                if(scoreANum>scoreBNum){
                    image3.setVisibility(View.VISIBLE);
                    if(isMyteamIsA){
                        image3.setImageResource(R.mipmap.win);
                    }else{
                        image3.setImageResource(R.mipmap.lose);
                    }
                }else if(scoreANum<scoreBNum){

                    if(isMyteamIsA){
                        image3.setImageResource(R.mipmap.lose);
                    }else{
                        image3.setImageResource(R.mipmap.win);
                    }
                }if(gameBean.getScoreA1()==gameBean.getScoreB1()){
                    image3.setImageResource(R.mipmap.draw);
                }
            }*/


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
