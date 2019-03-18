package com.football.net.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.MineTeamMemberAdapter;
import com.football.net.adapter.MineTeamScoreAdapter;
import com.football.net.bean.GameBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.TeamBean2Result;
import com.football.net.http.reponse.impl.TeamBeanResult;
import com.football.net.http.reponse.impl.UserBean2Result;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.FirstNewsAdapter;
import com.football.net.ui.shouye.FirstNewsGroundAdapter;
import com.football.net.widget.NoScrollGridView;
import com.football.net.widget.NoScrollListview;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@EActivity(R.layout.activity_mine_team)
public class MineTeamAtivity extends BaseActivity {

    @ViewById(R.id.firstnew_gridview)
    NoScrollGridView gridView;
    @ViewById(R.id.firstnews_news)
    NoScrollListview gameListView;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.iv_duihui)
    ImageView ivDuihui;
    @ViewById(R.id.teamType)
    ImageView teamType;
    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.tv_shenglv)
    TextView tvShenglv;
    @ViewById(R.id.levelV)
    TextView levelV;
    @ViewById(R.id.peopleNum)
    TextView peopleNum;
    @ViewById(R.id.progressbar1)
    ProgressBar progressbar1;
    @ViewById(R.id.progressbar2)
    ProgressBar progressbar2;
    @ViewById(R.id.progressbar3)
    ProgressBar progressbar3;
    @ViewById(R.id.winNum)
    TextView winNum;
    @ViewById(R.id.drawNum)
    TextView drawNum;
    @ViewById(R.id.loseNum)
    TextView loseNum;

    @ViewById(R.id.moreMember)
    TextView moreMember;


    ArrayList<UserBean> playerList = new ArrayList<UserBean>();
    MineTeamMemberAdapter playerAdapter;

    MineTeamScoreAdapter scoreAdapter;
    ArrayList<GameBean> scoreList = new ArrayList<GameBean>();


    @AfterViews
    void initView() {
        title.setText("我的球队");
        moreMember.setVisibility(View.GONE);
        playerAdapter = new MineTeamMemberAdapter(this, playerList);
        gridView.setAdapter(playerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserBean bean = playerList.get(position);
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                    Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                    intent.putExtra("beanid", bean.getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                    intent.putExtra("beanid", bean.getId());
                    startActivity(intent);

                }
            }
        });
        scoreAdapter = new MineTeamScoreAdapter(this, scoreList);
        gameListView.setAdapter(scoreAdapter);

        TeamBean teamBean = FootBallApplication.userbean.getTeam();
        if (teamBean != null) {
            loadTeamData(teamBean.getId() + "");
            loadPlayerData(teamBean.getId());
            loadGameListData(teamBean.getId());


            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), ivDuihui, FootBallApplication.options);
            teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
            name.setText(teamBean.getTeamTitle());
            tvShenglv.setText("胜率：" + CommonUtils.getGamePercent(teamBean.getWin(), teamBean.getTotal()) + "%");
            levelV.setText("等级：" + teamBean.getKind() + "");
            peopleNum.setText("成员：" + playerList.size());

            winNum.setText(teamBean.getWin() + "");
            drawNum.setText(teamBean.getEven() + "");
            loseNum.setText(teamBean.getLost() + "");

            if (teamBean.getTotal() > 0) {
                progressbar1.setProgress(teamBean.getWin() * 100 / teamBean.getTotal());
                progressbar2.setProgress(teamBean.getEven() * 100 / teamBean.getTotal());
                progressbar3.setProgress(teamBean.getLost() * 100 / teamBean.getTotal());
            }
        }
    }

    public void loadTeamData(final String id) {
        SmartParams params = new SmartParams();
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "team/" + id, params, new SmartCallback<TeamBean2Result>() {

            @Override
            public void onSuccess(int statusCode, TeamBean2Result result) {
                if (!result.isSuccess()) {
                    return;
                }
                TeamBean teamBean = result.getData();

                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), ivDuihui, FootBallApplication.options);
                teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
                name.setText(teamBean.getTeamTitle());
                tvShenglv.setText("胜率：" + CommonUtils.getGamePercent(teamBean.getWin(), teamBean.getTotal()) + "%");
                levelV.setText("等级：" + teamBean.getKind() + "");
                peopleNum.setText("成员：" + playerList.size());

                winNum.setText(teamBean.getWin() + "");
                drawNum.setText(teamBean.getEven() + "");
                loseNum.setText(teamBean.getLost() + "");

                if (teamBean.getTotal() > 0) {
                    progressbar1.setProgress(teamBean.getWin() * 100 / teamBean.getTotal());
                    progressbar2.setProgress(teamBean.getEven() * 100 / teamBean.getTotal());
                    progressbar3.setProgress(teamBean.getLost() * 100 / teamBean.getTotal());
                }


            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, TeamBean2Result.class);
    }

    public void loadGameListData(final int teamId) {
        RequestParam params = new RequestParam();
        params.put("currentPage", 1);
        params.put("teamId", teamId);
        params.put("status", 1);
        params.put("isEnabled", 1);
        params.put("orderby", "beginTime desc");

//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                scoreList.clear();
                scoreList.addAll(result.getList());
                if (scoreList.size() > 0) {
                    scoreAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, GameBeanListResult.class);
    }

    public void loadPlayerData(final int teamId) {
        RequestParam params = new RequestParam();
        params.put("teamId", teamId);
        params.put("orderby", "isCaptain asc,u.createTime desc");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {

            @Override
            public void onSuccess(int statusCode, UserBeanResult result) {
                playerList.addAll(result.getList());
                if (playerList.size() > 0) {
                    playerAdapter.notifyDataSetChanged();
                    peopleNum.setText("成员：" + playerList.size());
                    if (playerList.size() > 6) {
                        moreMember.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, UserBeanResult.class);
    }

    @Click({R.id.iv_duihui, R.id.image3, R.id.moreMember, R.id.id_yue_zhan_img})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_duihui:
                //点击头像
                if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() == null) {
                    ToastUtil.show(mContext, "您还没有加入任何球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", FootBallApplication.userbean.getTeam());
                mContext.startActivity(intent);
                break;
            case R.id.image3:
                Intent intent0 = new Intent(this, TeamAchievesActivity.class);
                this.startActivity(intent0);
                break;
            case R.id.moreMember:

                Intent intent1 = new Intent(this, MineMoreMemberActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.id_yue_zhan_img:
                //约占
                Intent a = new Intent(mContext, FabuMatchActivity.class);
                a.putExtra(IntentKey.General.KEY_TYPE, 1);
                mContext.startActivity(a);
                hideKeyboard();
                break;
        }
    }
}
