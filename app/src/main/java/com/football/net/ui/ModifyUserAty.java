package com.football.net.ui;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.football.net.R;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.manager.BasicActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/15.
 */
public class ModifyUserAty extends BasicActivity {

    @BindView(R.id.id_modify_info_edit)
    EditText mEditModify;

    //1昵称 2 身高 3体重 4场上号码 5qq 6wx
    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.aty_modify_userinfo;
    }

    @Override
    public int getTitleRes() {
        return 0;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", -1);
        String data = getIntent().getStringExtra("data");
        if (type == 1) {
            setTitle("修改昵称");
            if (StringUtils.isEmpty(data)) {
                mEditModify.setHint("修改昵称");
            } else {
                mEditModify.setHint(data);
            }
        } else if (type == 2) {
            setTitle("修改身高");
            if (StringUtils.isEmpty(data)) {
                mEditModify.setHint("修改身高");
            } else {
                mEditModify.setHint(data);
            }
            mEditModify.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (type == 3) {
            setTitle("修改体重");
            if (StringUtils.isEmpty(data)) {
                mEditModify.setHint("修改体重");
            } else {
                mEditModify.setHint(data);
            }
            mEditModify.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (type == 4) {
            setTitle("修改场上号码");
            if (StringUtils.isEmpty(data)) {
                mEditModify.setHint("修改场上号码");
            } else {
                mEditModify.setHint(data);
            }
            mEditModify.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (type == 5) {
            setTitle("修改QQ号");
            if (StringUtils.isEmpty(data)) {
                mEditModify.setHint("修改QQ号");
            } else {
                mEditModify.setHint(data);
            }
            mEditModify.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (type == 6) {
            setTitle("修改微信号");
            if (StringUtils.isEmpty(data)) {
                mEditModify.setHint("修改微信号");
            } else {
                mEditModify.setHint(data);
            }
        }

    }

    @OnClick(R.id.id_sure_btn)
    public void onClick(View mView) {
        switch (mView.getId()) {
            case R.id.id_sure_btn:
                //确定
                String data = mEditModify.getText().toString().trim();
                if (StringUtils.isEmpty(data)) {
                    ToastUtil.show(mContext, "请输入相关信息");
                    return;
                }
                BaseEvent event = new BaseEvent();
                event.data = data;
                event.flag = type;
                EventBus.getDefault().post(event);
                finish();
                break;
        }
    }
}
