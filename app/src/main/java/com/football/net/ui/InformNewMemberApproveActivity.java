package com.football.net.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.ApplyBean;
import com.football.net.bean.ApplyBean2;
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
public class InformNewMemberApproveActivity extends BasicActivity {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.timeview)
    TextView timeview;
    @BindView(R.id.image1)
    ImageView image1;
    ApplyBean2 bean2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inform_new_member_approvel;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_inform_new_member_approve;
    }

    @Override
    protected void initView() {

        bean2 = (ApplyBean2) getIntent().getSerializableExtra("itemBean");
        ApplyBean apply = bean2.getApply();
        name.setText(apply.getPlayer().getName());
        text2.setText("\u0003\u0003" + apply.getTitle());
        timeview.setText(CommonUtils.getFullTime(bean2.getCreateTime()));
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(apply.getPlayer().getIconUrl()),image1, FootBallApplication.circOptions);
    }

    @OnClick({R.id.aggree, R.id.disaggree})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = 0;
        switch (v.getId()) {
            case R.id.aggree:
                submit(1,bean2.getId());
                break;
            case R.id.disaggree:
                submit(2,bean2.getId());
                break;
        }
    }


    public void submit( int status,int tankid) {
        showProgress("提交中....");
        SmartParams params = new SmartParams();
        params.put("auditStatus",status);
        params.put("tankId",tankid);
        params.put("captainId",FootBallApplication.userbean.getId());

        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "audit", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if(result.isSuccess()){
                    showMsg("提交成功");
                    setResult(RESULT_OK);
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
