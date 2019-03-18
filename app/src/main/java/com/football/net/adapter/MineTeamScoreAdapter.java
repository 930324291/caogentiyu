package com.football.net.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.FootBallTeamInfoActivity;
import com.football.net.ui.TeamDetialActivity;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Andy Rao on 2017/1/9.
 */
public class MineTeamScoreAdapter extends BaseAdapter {

    private Context mContext;
    private final LayoutInflater inflater;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();

    public MineTeamScoreAdapter(Context context, ArrayList<GameBean> scoreList) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        dataList = scoreList;
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mine_team_score, null);
            vHolder = new ViewHolder();
            vHolder.imageA = (ImageView) convertView.findViewById(R.id.imageA);
            vHolder.imageB = (ImageView) convertView.findViewById(R.id.imageB);
            vHolder.image3 = (ImageView) convertView.findViewById(R.id.image3);
            vHolder.teamNameA = (TextView) convertView.findViewById(R.id.teamNameA);
            vHolder.teamNameB = (TextView) convertView.findViewById(R.id.teamNameB);
            vHolder.timeV = (TextView) convertView.findViewById(R.id.timeV);
            vHolder.address = (TextView) convertView.findViewById(R.id.address);
            vHolder.scoreA = (TextView) convertView.findViewById(R.id.scoreA);
            vHolder.scoreB = (TextView) convertView.findViewById(R.id.scoreB);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.mipmap.item_bg1);
        } else {
            convertView.setBackgroundResource(R.mipmap.item_bg2);
        }

        final GameBean gameBean = dataList.get(position);
        vHolder.teamNameA.setText(gameBean.getTeamA().getTeamTitle());
        vHolder.teamNameB.setText(gameBean.getTeamB().getTeamTitle());
        vHolder.timeV.setText(CommonUtils.getDateStr(gameBean.getBeginTime(), "yyyy-MM-dd HH:mm"));
        vHolder.address.setText(gameBean.getAddress());
   /*     vHolder.scoreA.setText(gameBean.getScoreA() == null ? " -- " : gameBean.getScoreA());
        vHolder.scoreB.setText(gameBean.getScoreB() == null?" -- ":gameBean.getScoreB());*/
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()), vHolder.imageA, FootBallApplication.options);
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamB().getIconUrl()), vHolder.imageB, FootBallApplication.options);
        boolean isMyteamIsA = false;
//        if (gameBean.getTeamA().getId() == FootBallApplication.userbean.getTeam().getId()) {
//            isMyteamIsA = true;
//        }


        int scoreANum = Integer.valueOf(gameBean.getScoreA() == null ? "-1" : gameBean.getScoreA());
        int scoreBNum = Integer.valueOf(gameBean.getScoreB() == null ? "-1" : gameBean.getScoreB());
        if (scoreANum == -1 || scoreBNum == -1) {
            vHolder.image3.setVisibility(View.INVISIBLE);
            vHolder.scoreB.setText("审核发布  " + "-- : --");
        } else {
            if (scoreANum > scoreBNum) {
                vHolder.image3.setVisibility(View.VISIBLE);
                if (isMyteamIsA) {
                    vHolder.image3.setImageResource(R.mipmap.win);
                } else {
                    vHolder.image3.setImageResource(R.mipmap.lose);
                }
            } else if (scoreANum < scoreBNum) {
                vHolder.image3.setVisibility(View.VISIBLE);
                if (isMyteamIsA) {
                    vHolder.image3.setImageResource(R.mipmap.lose);
                } else {
                    vHolder.image3.setImageResource(R.mipmap.win);
                }
            } else if (scoreANum == scoreBNum) {
                vHolder.image3.setVisibility(View.VISIBLE);
                vHolder.image3.setImageResource(R.mipmap.draw);
            }
            vHolder.scoreB.setText("审核发布  " + scoreANum + " : " + scoreBNum);
        }
        if (isMyteamIsA) {
            vHolder.scoreA.setText("本队发布  " + (gameBean.getScoreA() == null ? "--" : gameBean.getScoreA()) + " : " + gameBean.getScoreB());
        } else {
            vHolder.scoreA.setText("本队发布  " + (gameBean.getScoreA() == null ? "--" : gameBean.getScoreA()) + " : " + gameBean.getScoreB());
        }
        vHolder.imageA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == gameBean.getTeamA()) {
                    ToastUtil.show(mContext, "没有球队");
                    return;
                }
//                Intent intent = new Intent(mContext,TeamDetialActivity.class);
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", gameBean.getTeamA());
                mContext.startActivity(intent);
            }
        });
        vHolder.imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == gameBean.getTeamB()) {
                    ToastUtil.show(mContext, "没有球队");
                    return;
                }
//                Intent intent = new Intent(mContext,TeamDetialActivity.class);
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", gameBean.getTeamB());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        ImageView imageA, imageB, image3;
        TextView teamNameA, teamNameB, timeV, address, scoreA, scoreB;
    }
}
