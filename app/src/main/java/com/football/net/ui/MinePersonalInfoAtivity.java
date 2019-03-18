package com.football.net.ui;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.LogUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

@EActivity(R.layout.activity_mine_personal_info)
public class MinePersonalInfoAtivity extends BaseActivity {
    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.headView)
    ImageView headView;
    @ViewById(R.id.name)
    TextView nameView;
    @ViewById(R.id.nickname)
    TextView nickname;
    @ViewById(R.id.gender)
    TextView gender;
    @ViewById(R.id.birthday)
    TextView birthday;
    @ViewById(R.id.bodyHeight)
    TextView bodyHeight;
    @ViewById(R.id.bodyWeight)
    TextView bodyWeight;
    @ViewById(R.id.number)
    TextView number;

    @ViewById(R.id.positionInTeam)
    TextView positionInTeam;
    @ViewById(R.id.joinTime)
    TextView joinTime;
    @ViewById(R.id.status)
    TextView status;
    @ViewById(R.id.homeaddress)
    TextView homeaddress;
    @ViewById(R.id.qqNum)
    TextView qqNum;

    @ViewById(R.id.weixinNum)
    TextView weixinNum;
    @ViewById(R.id.email)
    TextView email;
    @ViewById(R.id.phoneNum)
    TextView phoneNum;
    @ViewById(R.id.blogs)
    TextView blogs;
    @ViewById(R.id.introduce)
    TextView introduce;

    private UserBean bean;

    @AfterViews
    void initView() {
        EventBus.getDefault().register(this);
        title.setText("个人资料");

        bean = FootBallApplication.userbean;
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), headView, FootBallApplication.circOptions);
        nameView.setText(bean.getName());

        gender.setText(bean.getGender() == 1 ? "男" : "女");
        birthday.setText(CommonUtils.getDateStr(bean.getBirth(), "yyyy-MM-dd"));

        positionInTeam.setText(CommonUtils.getPositionStr(bean.getPosition()));
        joinTime.setText(bean.getVerifyTime() == 0 ? "暂无" : CommonUtils.getDateStr(bean.getVerifyTime(), "yyyy-MM-dd"));
        status.setText("暂无");
        homeaddress.setText("暂无");

        email.setText("暂无");
        phoneNum.setText(bean.getMobile());
        blogs.setText("暂无");
        introduce.setText("暂无");
        setUserInfo();
    }

    public void setUserInfo() {
        nickname.setText(bean.getNickname());
        bodyHeight.setText(bean.getHeight() == null ? "暂无" : bean.getHeight() + "CM");
        bodyWeight.setText(bean.getWeight() == null ? "暂无" : bean.getWeight() + "KG");
        number.setText(bean.getUniformNumber() == null ? "暂无" : bean.getUniformNumber());
        qqNum.setText(bean.getQq());
        weixinNum.setText(bean.getWechat());
    }

    @Click({R.id.id_modify_name_linear, R.id.id_modify_height_linear, R.id.id_modify_weight_linear, R.id.id_ball_linear, R.id.id_modify_qq_linear, R.id.id_modify_wx_linear})
    public void onClick(View mView) {
        switch (mView.getId()) {
            case R.id.id_modify_name_linear:
                //昵称
                Intent intent = new Intent(mContext, ModifyUserAty.class);
                intent.putExtra("type", 1);
                intent.putExtra("data", bean.getNickname());
                startActivity(intent);
                break;
            case R.id.id_modify_height_linear:
                //身高
                Intent height = new Intent(mContext, ModifyUserAty.class);
                height.putExtra("type", 2);
                height.putExtra("data", bean.getHeight());
                startActivity(height);
                break;
            case R.id.id_modify_weight_linear:
                //体重
                Intent weight = new Intent(mContext, ModifyUserAty.class);
                weight.putExtra("type", 3);
                weight.putExtra("data", bean.getWeight());
                startActivity(weight);
                break;
            case R.id.id_ball_linear:
                //场上号码
                Intent num = new Intent(mContext, ModifyUserAty.class);
                num.putExtra("type", 4);
                num.putExtra("data", bean.getUniformNumber());
                startActivity(num);
                break;
            case R.id.id_modify_qq_linear:
                //qq
                Intent qq = new Intent(mContext, ModifyUserAty.class);
                qq.putExtra("type", 5);
                qq.putExtra("data", bean.getQq());
                startActivity(qq);
                break;
            case R.id.id_modify_wx_linear:
                //wx
                Intent wx = new Intent(mContext, ModifyUserAty.class);
                wx.putExtra("type", 6);
                wx.putExtra("data", bean.getWechat());
                startActivity(wx);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        String data = event.data;
        int flag = event.flag;
        if (flag == 1) {
            bean.setNickname(data);
        } else if (flag == 2) {
            bean.setHeight(data);
        } else if (flag == 3) {
            bean.setWeight(data);
        } else if (flag == 4) {
            bean.setUniformNumber(data);
        } else if (flag == 5) {
            bean.setQq(data);
        } else if (flag == 6) {
            bean.setWeight(data);
        }
        commit();
    }

    public void commit() {
        SmartParams params = new SmartParams();
        params.put("playerId", bean.getId());
        params.put("nickname", bean.getNickname());
        params.put("weight", bean.getWeight());
        params.put("height", bean.getHeight());
        params.put("uniformNumber", bean.getUniformNumber());
        params.put("qq", bean.getQq());
        params.put("wechat", bean.getWechat());
        //3.请求数据
        new SmartClient(mContext).get(HttpUrlConstant.APP_SERVER_URL + "player/updateInfo", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("修改成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUserInfo();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                Log.d("huang", statusCode + "=====修改失败" + message);
                showMsg("修改失败");
            }

        }, Result.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
