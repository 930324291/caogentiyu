package com.football.net.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.MessageOutBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.manager.BasicActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/11.
 */
public class MemberSignDetailActivity extends BasicActivity {

    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.lin_signtime)
    View lin_signtime;
    @BindView(R.id.signTimeLabel)
    TextView signTimeLabel;
    @BindView(R.id.signDesc)
    TextView signDesc;
    @BindView(R.id.signTime)
    TextView signTime;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;

    MessageOutBean bean;

    public static int RESULT_CODE = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_member_sign_detail;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_member_sign_detail;
    }

    @Override
    protected void initView() {
        bean = (MessageOutBean) getIntent().getSerializableExtra("MessageOutBean");
        // titleV.setText(CommonUtils.getDateStr(bean.getMessage().getBeginTime(),"yyyy-MM-dd HH:mm") + "," + bean.getMessage().getTitle());
        // contentV.setText(bean.getMessage().getContent());

        timeTv.setText(CommonUtils.getFullTime(bean.getMessage().getBeginTime()));
        address.setText(bean.getMessage().getAddress());
        detail.setText(bean.getMessage().getContent());

        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);

        Integer confirmStatus = bean.getConfirmStatus();
        if (confirmStatus==null) {
            signDesc.setText("未签到");
            lin_signtime.setVisibility(View.GONE);
            if (bean.getMessage().getBeginTime()>System.currentTimeMillis()) {
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
            }
        } else {
            if (confirmStatus==1) {
                signDesc.setText("已签到");
                signTimeLabel.setText("签到时间：");
                signTime.setText(CommonUtils.getFullTime(bean.getConfirmTime()));

                if (bean.getMessage().getBeginTime()>System.currentTimeMillis()) {
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                }
            } else if (confirmStatus==2) {
                signDesc.setText("已请假");
                signTimeLabel.setText("请假时间：");
                signTime.setText(CommonUtils.getFullTime(bean.getConfirmTime()));

                if (bean.getMessage().getBeginTime()>System.currentTimeMillis()) {
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.GONE);
                }
            } else {
                signDesc.setText("未知");
            }
        }
    }

    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.button1:
                commit(1);
                break;
            case R.id.button2:
                commit(2);
                break;
        }
    }

    //GET http://47.89.46.215:8181/app/signIn?confirmStatus=2&tankId=82 HTTP/1.1
    public void commit(int status ) {
        showProgress("提交中...");
        SmartParams params = new SmartParams();
        params.put("confirmStatus", status);
        params.put("tankId", bean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "signIn", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if(result.isSuccess()){
                    showMsg("提交成功");
                    setResult(RESULT_CODE);// 设置resultCode，onActivityResult()中能获取到
                    finish();
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
