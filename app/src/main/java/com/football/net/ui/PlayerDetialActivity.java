package com.football.net.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.PlayerLikeBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.UserBean2Result;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.PlayerPicture7VideoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class PlayerDetialActivity extends BaseActivity {

    @BindView(R.id.layout1)
    View layout1;
    @BindView(R.id.layout3)
    ImageView layout3;
    @BindView(R.id.layout4)
    View layout4;

    @BindView(R.id.teamName)
    TextView teamName;

    @BindView(R.id.teamLogo)
    ImageView teamLogo;

    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.valueView)
    TextView valueView;
    @BindView(R.id.likeNumV)
    TextView likeNumV;

    @BindView(R.id.renqi)
    TextView renqi;
    @BindView(R.id.winNum)
    TextView winNum;
    @BindView(R.id.drawNum)
    TextView drawNum;
    @BindView(R.id.loseNum)
    TextView loseNum;
    @BindView(R.id.progressbar1)
    ProgressBar progressbar1;
    @BindView(R.id.progressbar2)
    ProgressBar progressbar2;
    @BindView(R.id.progressbar3)
    ProgressBar progressbar3;
    String id;


    UserBean userBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("beanid");

        // 如果用户已占赞，则显示灰色背景并且禁止再点击
        if (FootBallApplication.playerLikes!=null) {
            List<PlayerLikeBean> list = FootBallApplication.playerLikes;
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                PlayerLikeBean playerLike = (PlayerLikeBean) iterator.next();
                if (playerLike.getPlayer().getId().equals(id)) { // 如果登录用户球员点赞集合包含当前的球员，是置灰点赞图标
                    layout4.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                    layout4.setClickable(false);
                    break;
                }
            }
        }

        loadData(id);
    }

    public void loadData(final String id) {
        showProgress("加载中");
        SmartParams params = new SmartParams();
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "player/" + id, params, new SmartCallback<UserBean2Result>() {

            @Override
            public void onSuccess(int statusCode, UserBean2Result result) {
                dismissProgress();
                if (!result.isSuccess()) {
                    return;
                }
                userBean = result.getData();
                if (null != userBean) {
                    userName.setText(userBean.getName());
                    valueView.setText(userBean.getValue() + "万");
                    likeNumV.setText(userBean.getLikeNum() + "");
                    renqi.setText(userBean.getLikeSeq() + "");

                    winNum.setText(userBean.getWin() + "");
                    drawNum.setText(userBean.getEven() + "");
                    loseNum.setText(userBean.getLost() + "");

                    if (userBean.getTotal() > 0) {
                        progressbar1.setProgress(userBean.getWin() * 100 / userBean.getTotal());
                        progressbar2.setProgress(userBean.getEven() * 100 / userBean.getTotal());
                        progressbar3.setProgress(userBean.getLost() * 100 / userBean.getTotal());
                    }

                    if (!StringUtils.isEmpty(userBean.getIconUrl())) {
                        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(userBean.getIconUrl()), layout3, FootBallApplication.circOptions);
                    }
                    TeamBean teamBean = userBean.getTeam();
                    if (teamBean != null) {
                        teamName.setText(teamBean.getTeamTitle());
                        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), teamLogo, FootBallApplication.options);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
            }

        }, UserBean2Result.class);
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout6, R.id.layout4})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.layout1:
                if (null == userBean){
                    return;
                }
                if (null == userBean.getTeam()){
                    ToastUtil.show(mContext, "您还没有加入任何球队");
                    return;
                }
                /* 球员详情页面，左上角的球队点击不可进入，不然会死循环了
                Intent team = new Intent(mContext, TeamDetialActivity1.class);
                team.putExtra("teamBean", userBean.getTeam());
                mContext.startActivity(team);
                */
                break;
            case R.id.layout2:
                if (null == userBean) {
                    return;
                }
                Intent intent = new Intent(this, PlayerZhanJiActivity.class);
                intent.putExtra("userBean", userBean);
                startActivity(intent);
                break;
            case R.id.layout3:
                if (null == userBean) {
                    return;
                }
                Intent intent2 = new Intent(this, PlayerInfoActivity.class);
                intent2.putExtra("userBean", userBean);
                startActivity(intent2);
                break;
            case R.id.layout6:
//                Intent intent3 = new Intent(this, PlayerPictureActivity.class);
                if (null == userBean) {
                    return;
                }
                Intent intent3 = new Intent(this, PlayerPicture7VideoActivity.class);
                intent3.putExtra("userBean", userBean);
                startActivity(intent3);
                break;
            case R.id.layout4:
                if (null != userBean) {

                    // 点赞
                    if (FootBallApplication.playerLikes.size()>=FootBallApplication.like_player_max) {
                        showMsg("您一天之内只有"+FootBallApplication.like_player_max+"次给球员点赞的机会！");
                    } else {
                        boolean flag = false;

                        // 如果用户已占赞，则显示灰色背景并且禁止再点击
                        if (FootBallApplication.playerLikes!=null) {
                            List<PlayerLikeBean> list = FootBallApplication.playerLikes;
                            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                                PlayerLikeBean playerLike = (PlayerLikeBean) iterator.next();
                                if (playerLike.getPlayer().getId().equals(id)) { // 如果登录用户球员点赞集合包含当前的球员，是置灰点赞图标
                                    flag = true;
                                    likeNumV.setText((userBean.getLikeNum())+1 + "");
                                    layout4.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                                    layout4.setClickable(false);
                                    break;
                                }
                            }
                        }

                        if (!flag) {
                            commit();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 点赞 http://47.89.46.215/app/likeTeam?giverId=75&teamId=25 HTTP/1.1
     */
    public void commit() {
        showProgress("提交中...");
        SmartParams params = new SmartParams();
        params.put("giverId", FootBallApplication.userbean.getId());
        params.put("playerId", id);
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "likePlayer", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("点赞成功");
                userBean.setLikeNum(userBean.getLikeNum() + 1);
                likeNumV.setText(userBean.getLikeNum() + "");

                layout4.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                layout4.setClickable(false);

                // 将点赞的记录填写到全局playerlikes中
                PlayerLikeBean bean = new PlayerLikeBean(userBean,FootBallApplication.userbean,1);
                FootBallApplication.playerLikes.add(bean);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("点赞失败");
            }

        }, Result.class);
    }

}
