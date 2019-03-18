package com.football.net.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.football.net.R;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;

/**
 * Created by Andy Rao on 2017/1/11.
 */
public class ActivityDetailActivity extends BasicActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_activity_detail;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_activity_detail;
    }

    @Override
    protected void initView() {

    }
}
