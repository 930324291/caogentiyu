package com.football.net.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.fileIo.SharePref;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.ApplyBeanResult;
import com.football.net.http.reponse.impl.UnreadNumResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MyRatingBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import butterknife.BindView;

@EActivity(R.layout.activity_mine_center)
public class MineCenter4CaptainAtivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.bodyHeight)
    TextView bodyHeight;
    @ViewById(R.id.tvposition)
    TextView tvposition;
    @ViewById(R.id.ratingbar)
    MyRatingBar ratingbar;
    @ViewById(R.id.levelV)
    TextView levelV;
    @ViewById(R.id.bodyWeight)
    TextView bodyWeight;
    @ViewById(R.id.teamName)
    TextView teamName;
    @ViewById(R.id.header)
    ImageView header;
    @ViewById(R.id.imageMyinfo)
    ImageView imageMyinfo;

    @ViewById(R.id.tvNum1)
    TextView tvNum1;

    @AfterViews
    void initView() {
        title.setText("用户中心");

        UserBean bean = FootBallApplication.userbean;
        name.setText(bean.getName());
        bodyHeight.setText("身高：" + (bean.getHeight() == null ? "暂无" : bean.getHeight() + "CM"));
        tvposition.setText("擅长位置：" + CommonUtils.getPositionStr(bean.getPosition()));
        ratingbar.setRating(bean.getOfficial());
        levelV.setText("Lv" + bean.getOfficial());
        bodyWeight.setText("体重：" + (bean.getWeight() == null ? "暂无" : bean.getWeight() + "KG"));
        if (bean.getTeam() != null) {
            teamName.setText("所在球队：" + bean.getTeam().getTeamTitle());
        } else {
            teamName.setText("所在球队：暂无");
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), header, FootBallApplication.circOptions);
        // imageMyinfo.setVisibility(View.GONE);
        if (FootBallApplication.unreadNumResult == null) {
            loadData();
        } else {
            int num1 = FootBallApplication.unreadNumResult.getUnReadCnt1();
            if (num1 > 0) {
                tvNum1.setVisibility(View.VISIBLE);
                tvNum1.setText(num1 + "");
            }
        }
    }

    public void loadData() {
        long lastReadTime = SharePref.getLastReadTime(this);
        SmartParams params = new SmartParams();
        params.put("lastReadTime", lastReadTime);
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + " getUnreadCnt", params, new SmartCallback<UnreadNumResult>() {

            @Override
            public void onSuccess(int statusCode, UnreadNumResult result) {
                SharePref.setLastReadTime(MineCenter4CaptainAtivity.this, System.currentTimeMillis());
                int num1 = result.getUnReadCnt1();
                if (num1 > 0) {
                    tvNum1.setVisibility(View.VISIBLE);
                    tvNum1.setText(num1 + "");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, UnreadNumResult.class);
    }

    @Click({R.id.myteam, R.id.imageMyinfo, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout6, R.id.layout7, R.id.layout8, R.id.layout9})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.myteam:

                if (FootBallApplication.userbean.getTeam() != null) {
                   /* Intent intent0 = new Intent(this, FootBallTeamInfoActivity.class);
                    intent0.putExtra("beandata",FootBallApplication.userbean.getTeam());
                    startActivity(intent0);*/
                    Intent intent0 = new Intent(this, MineTeamAtivity_.class);
                    this.startActivity(intent0);
                } else {
                    showMsg("您还没有加入任何球队！");
                }
                break;
            case R.id.imageMyinfo:
                Intent intent = new Intent(this, MinePersonalInfoAtivity_.class);
                this.startActivity(intent);
                break;
            case R.id.layout1: {
                if (FootBallApplication.unreadNumResult != null) {
                    FootBallApplication.unreadNumResult.setUnReadCnt1(0);
                }
                tvNum1.setVisibility(View.GONE);
                Intent intent1 = new Intent(this, MineRecuritActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout2: {
                Intent intent1 = new Intent(this, MineSigninMessageActivity.class);
                intent1.putExtra("type", 2);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout3: {
                Intent intent1 = new Intent(this, MineSigninMessageActivity.class);
                intent1.putExtra("type", 1);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout4: {
                Intent intent1 = new Intent(this, MinePicture7VideoActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout5: {
                Intent intent1 = new Intent(this, MineGameListActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout6: {
                Intent intent1 = new Intent(this, MineScoreListActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout7: {
                Intent intent1 = new Intent(this, HelpActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout8: {
                SharePref.setIsautoLoging(this, false);
                exitApp();
                break;
            }
            case R.id.layout9:
                //用户须知
                Intent intent1 = new Intent(this,UserKnowAty.class);
                this.startActivity(intent1);
                break;

        }
    }
}
