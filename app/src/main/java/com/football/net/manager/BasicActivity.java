package com.football.net.manager;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 基类实现一些公共方法
 * 此类带有 公共头布局
 * 继承此类需要实现getLayoutId，getTitleRes，initView方法
 */
public abstract class BasicActivity extends BaseActivity {

    @BindView(R.id.returnBtn)
    ImageView returnBtn;
    @BindView(R.id.title)
    protected TextView title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        LinearLayout linRoot = (LinearLayout) findViewById(R.id.lin_activity_root);
        View chileView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        linRoot.addView(chileView);
        ButterKnife.bind(this);
        if (getTitleRes() != 0) {
            title.setText(getResources().getString(getTitleRes()));
        }
        initView();
    }

    //设置layout
    @LayoutRes
    public abstract int getLayoutId();

    //设置title
    @StringRes
    public abstract int getTitleRes();

    //初始化view
    protected abstract void initView();

    public void setTitle(String text) {
        title.setText(text);
    }

//    @OnClick(R.id.returnBtn)
//    public void onClick(View mView) {
//        switch (mView.getId()) {
//            case R.id.returnBtn:
//                finish();
//                break;
//        }
//    }
}
