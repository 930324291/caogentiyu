package com.football.net.manager;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.football.net.R;
import com.football.net.common.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Base Fragment
 *添加一些公共方法和属性
 * 使用ButterKnife注解绑定view
 */
public abstract class BaseFragment extends Fragment {

    public final String TAG = getClass().getSimpleName();
    public Context mContext;

    public static final int[] COLOR_SCHEMES = {
            Color.parseColor("#ff33b5e5"),
            Color.parseColor("#ffff4444"),
            Color.parseColor("#ffffbb33"),
            Color.parseColor("#ff99cc00"),
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        RelativeLayout fragmentRoot = (RelativeLayout) view.findViewById(R.id.fragment_root);
        View chileView = inflater.inflate(getLayoutId(), null);
        fragmentRoot.addView(chileView);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    protected void hideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //布局layout id
    @LayoutRes
    public abstract int getLayoutId();
    //初始化view
    protected abstract void initView();

    /**
     * 加载动画
     */
    ProgressDialog progressDialog;

    public void showProgress(String content) {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.show();

        progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
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

    public void showMsg(String msg) {
        ToastUtil.show(mContext, msg);
    }

}
