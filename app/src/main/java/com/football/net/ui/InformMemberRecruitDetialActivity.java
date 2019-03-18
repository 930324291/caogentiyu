package com.football.net.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.RecruitBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class InformMemberRecruitDetialActivity extends BasicActivity {

    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.content)
    TextView contentV;
    @BindView(R.id.timeV)
    TextView timeV;
    @BindView(R.id.button1)
    TextView button1;
    @BindView(R.id.button2)
    TextView button2;

    @BindView(R.id.status)
    TextView status;

    RecruitBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inform_member_recruit_detial;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_inform_member_recruit_detial;
    }

    @Override
    protected void initView() {
        bean = (RecruitBean) getIntent().getSerializableExtra("RecruitBean");
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+ CommonUtils.getRurl(bean.getTeam().getIconUrl()), image1, FootBallApplication.options);
        name.setText(bean.getTeam().getTeamTitle());
        contentV.setText(bean.getTeam().getTeamTitle()+"邀请你加入球队！");
        timeV.setText(CommonUtils.getDateStr(bean.getOpTime()));

        String statusStr = "";
        if (bean.getConfirmStatus()==null) {
            statusStr = "待加入";
        } else if (bean.getConfirmStatus()==1) {
            statusStr = "已加入";
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        } else {
            statusStr = "已拒绝";
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }
        status.setText(statusStr);
    }

    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.button1:
                showProgress("请求中...");
                loadData(1);
                break;
            case R.id.button2:
                showProgress("请求中...");
                loadData(2);
                break;
        }
    }

    //http://47.89.46.215:8181/app/confirmRecruit?confirmStatus=2&recruitId=53

    public void loadData(final int confirm) {
//        RequestParam params = new RequestParam();
        SmartParams params = new SmartParams();
        params.put("confirmStatus", confirm);
        params.put("recruitId", bean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "confirmRecruit", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if(result.isSuccess()){
                    showMsg("提交成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showMsg("提交失败！");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("提交失败");
            }
        }, Result.class);
    }
}
