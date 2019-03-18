package com.football.net.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    public LayoutInflater inflater;
    public Context mContext;
    public Activity mActivity;
    public String TAG, DETAILTAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
        TAG = getActName();
        inflater = getLayoutInflater();
        mActivity = this;
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initLayout();
        ButterKnife.bind(this);
        initView(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //初始化布局
    public abstract void initLayout();

    public abstract void initView(Bundle savedInstanceState);

    //初始化数据
    public abstract void initData();

    //监听
    public abstract void initListener();

    // 依赖注入
    public abstract void setupActivityComponent();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回时页面效果 自定义
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return false;
    }

    public String getActName() {
        return this.getClass().getSimpleName();
    }

    public String getActDetailName() {
        return this.getClass().getName();
    }


    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
