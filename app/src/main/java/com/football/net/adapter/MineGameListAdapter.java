package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import static com.football.net.R.id.iv_result;

/**
 * Created by Administrator on 2015/8/27.
 */
public class MineGameListAdapter extends UltimateViewAdapter<MineGameListAdapter.ViewHolder> {

    private OnItemClickListener mOnitemClickListener;
    private Context mContext;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();

    public MineGameListAdapter(Context context, ArrayList<GameBean> data) {
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
                .inflate(R.layout.item_football_game, parent, false);
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

        RelativeLayout linRoot;
        TextView tvBiFen1, tvBiFen2,nameA,nameB,timeTv,address,equaltag, gameStatus;
        ImageView imageA,imageB,image3,lingdang;

        public ViewHolder(View itemView) {
            super(itemView);
            linRoot = (RelativeLayout) itemView.findViewById(R.id.lin_root);
            tvBiFen1 = (TextView) itemView.findViewById(R.id.tv_bifen1);
            tvBiFen2 = (TextView) itemView.findViewById(R.id.tv_bifen2);
            nameA = (TextView) itemView.findViewById(R.id.nameA);
            nameB = (TextView) itemView.findViewById(R.id.nameB);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            address = (TextView) itemView.findViewById(R.id.address);
            equaltag = (TextView) itemView.findViewById(R.id.equaltag);
            gameStatus = (TextView) itemView.findViewById(R.id.gameStatus);

            imageA = (ImageView) itemView.findViewById(R.id.imageA);
            imageB = (ImageView) itemView.findViewById(R.id.imageB);
            image3 = (ImageView) itemView.findViewById(iv_result);
            lingdang = (ImageView) itemView.findViewById(R.id.lingdang);
        }

        private void bindView(final GameBean gameBean){

            // 判断当前比赛的状态，并展示在TextView gameStatus中
            String gameStatusStr = CommonUtils.getGameStatus(gameBean);
            gameStatus.setText(gameStatusStr);
            gameStatus.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

            if (gameStatusStr.equals("已结束")) {
                image3.setVisibility(View.VISIBLE);
            }

            nameA.setText(gameBean.getTeamA().getTeamTitle());
            TeamBean teamB = gameBean.getTeamB();
            if(teamB != null){
                nameB.setVisibility(View.VISIBLE);
                tvBiFen1.setVisibility(View.VISIBLE);
                tvBiFen2.setVisibility(View.VISIBLE);
                equaltag.setVisibility(View.VISIBLE);
                lingdang.setVisibility(View.GONE);
                nameB.setText(teamB.getTeamTitle());

//                tvBiFen1.setText(gameBean.getScoreA1()+"");
//                tvBiFen2.setText(gameBean.getScoreB1()+"");
//                ivResult.setVisibility(View.VISIBLE);
//                if(Integer.valueOf(gameBean.getScoreA1())> Integer.valueOf(gameBean.getScoreB1())) {
//                    ivResult.setImageResource(R.mipmap.win);
//                }else if(Integer.valueOf(gameBean.getScoreA1()) == Integer.valueOf(gameBean.getScoreB1())) {
//                    ivResult.setImageResource(R.mipmap.draw);
//                }else {
//                    ivResult.setImageResource(R.mipmap.lose);
//                }

                tvBiFen1.setText(gameBean.getScoreA() == null ? " -- " : gameBean.getScoreA());
                tvBiFen2.setText(gameBean.getScoreB() == null ? " -- " : gameBean.getScoreB());
                boolean isMyteamIsA = true;
//                if(gameBean.getTeamA().getId() != FootBallApplication.userbean.getTeam().getId()){
//                    isMyteamIsA = false;
//                }
                int scoreANum = Integer.valueOf(gameBean.getScoreA() == null?"-1":gameBean.getScoreA());
                int scoreBNum = Integer.valueOf(gameBean.getScoreB() == null?"-1":gameBean.getScoreB());
                if(scoreANum == -1 || scoreBNum ==-1 ){
                    image3.setVisibility(View.INVISIBLE);
                }else{
                    if(scoreANum>scoreBNum){
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
                    image3.setVisibility(View.VISIBLE);
                }


                imageB.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()),imageB, FootBallApplication.circOptions);
            }else{
                nameB.setText("未知");
                tvBiFen1.setText("?");
                tvBiFen2.setText("?");
                nameB.setVisibility(View.GONE);
                tvBiFen1.setVisibility(View.GONE);
                tvBiFen2.setVisibility(View.GONE);
                equaltag.setVisibility(View.GONE);
                lingdang.setVisibility(View.VISIBLE);

                image3.setVisibility(View.INVISIBLE);
                imageB.setImageResource(R.mipmap.icon_unknow);
                imageB.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            timeTv.setText(CommonUtils.getDateStr(gameBean.getBeginTime(),"yyyy-MM-dd HH:mm"));
            address.setText(gameBean.getAddress());
            ImageLoader.getInstance().displayImage(CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()),imageA, FootBallApplication.circOptions);

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
