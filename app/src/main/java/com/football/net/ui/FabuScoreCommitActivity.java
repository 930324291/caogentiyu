package com.football.net.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.ScoreListBean;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_fabu_score_commit)
public class FabuScoreCommitActivity extends BaseActivity {
    //    @ViewById(R.id.account)
//    EditText account;
//    @ViewById(R.id.verification)
//    EditText verification;
//    @ViewById(R.id.verificationBtn)
//    TextView verificationBtn;
//    @ViewById(R.id.password)
//    EditText password;
    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.timeV)
    TextView timeV;
    @ViewById(R.id.address)
    TextView address;
    @ViewById(R.id.teamNameA)
    TextView teamNameA;
    @ViewById(R.id.teamImageA)
    ImageView teamImageA;
    @ViewById(R.id.score1)
    TextView score1;
    @ViewById(R.id.teamNameB)
    TextView teamNameB;
    @ViewById(R.id.teamImageB)
    ImageView teamImageB;
    @ViewById(R.id.score2)
    TextView score2;
    ScoreListBean bean;

    @AfterViews
    void initView() {
        title.setText("比分录入");
        score1.setText("0");
        score2.setText("0");
        bean = (ScoreListBean) getIntent().getSerializableExtra("ScoreListBean");
        timeV.setText("时间：" + CommonUtils.getDateStr(bean.getBeginTime(), "yyyy-MM-dd HH:mm"));
        address.setText("地点：" + bean.getAddress());
        TeamBean teamA = bean.getTeamA();
        if (null != teamA) {
            teamNameA.setText(teamA.getTeamTitle());
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamA.getIconUrl()), teamImageA, FootBallApplication.options);
        } else {
            teamNameA.setText("未知");
        }
        TeamBean teamB = bean.getTeamB();
        if (null != teamB) {
            teamNameB.setText(teamB.getTeamTitle());
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamB.getIconUrl()), teamImageB, FootBallApplication.options);
        } else {
            teamNameB.setText("未知");
        }
    }

    int score1Num = 0, score2Num = 0;

    @Click({R.id.commit, R.id.add, R.id.subtract, R.id.add2, R.id.subtract2})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commit:
                loadData();
                break;
            case R.id.add:
                if (score1Num < 11) {
                    score1Num++;
                    score1.setText(score1Num + "");
                } else {
                    showMsg("已达到最大值");
                }
                break;
            case R.id.subtract:
                if (score1Num > 0) {
                    score1Num--;
                    score1.setText(score1Num + "");
                } else {
                    showMsg("已达到最小值");
                }
                break;
            case R.id.add2:
                if (score2Num < 11) {
                    score2Num++;
                    score2.setText(score2Num + "");
                } else {
                    showMsg("已达到最大值");
                }
                break;
            case R.id.subtract2:
                if (score2Num > 0) {
                    score2Num--;
                    score2.setText(score2Num + "");
                } else {
                    showMsg("已达到最小值");
                }
                break;
        }
    }

    //GET http://47.89.46.215:8181/app/game/inputScore?gameId=71&scoreA=1&scoreB=2&teamId=19 HTTP/1.1
    public void loadData() {
        showProgress("上传中...");
        SmartParams params = new SmartParams();
        params.put("gameId", bean.getId());
        params.put("scoreA", score1Num);
        params.put("scoreB", score2Num);
        params.put("teamId", FootBallApplication.userbean.getTeam() == null ? "" : FootBallApplication.userbean.getTeam().getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "game/inputScore", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if (result.isSuccess()) {
                    showMsg("录入成功");
                    setResult(20);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("录入失败");
            }

        }, Result.class);
    }
}
