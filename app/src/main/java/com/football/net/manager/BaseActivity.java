package com.football.net.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.football.net.bean.UserBean;
import com.football.net.common.util.LogUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.ui.StartActivity;


/**
 * 基类实现一些公共方法
 * BaseActivity不带 公共头布局
 */
public class BaseActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();
    public Context mContext;
    InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        FootBallApplication.mActivities.add(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
//        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
//    }

    public void showMsg(String msg) {
        ToastUtil.show(this, msg);
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        LogUtils.d("退出应用");
        FootBallApplication.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void finish(View view) {
        hideKeyboard();
        finish();
    }

    /**
     * 加载动画
     */
    ProgressDialog progressDialog;

    public void showProgress(String content) {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.show();

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(content);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FootBallApplication.mActivities.remove(this);
    }
}
