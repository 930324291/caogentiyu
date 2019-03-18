package com.football.net.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.fragment.MinePicturecFragment;
import com.football.net.fragment.MineVideoFragment;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.SquarePhotoAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MinePicture7VideoActivity extends BaseActivity {
    @BindView(R.id.contentPanel)
    LinearLayout contentPanel;
    @BindView(R.id.leftbtn)
    TextView leftbtn;
    @BindView(R.id.wrightbtn)
    TextView wrightbtn;

    FragmentManager fragmentManager;
    MinePicturecFragment picturecFragment;
    MineVideoFragment videoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_video);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        picturecFragment = new MinePicturecFragment();
//        transaction.hide(picturecFragment);
        if(!picturecFragment.isAdded()) {
            transaction.add(R.id.contentPanel,picturecFragment);
        }
        transaction.show(picturecFragment).commit();
        leftbtn.setSelected(true);
        wrightbtn.setSelected(false);
    }

    @OnClick({R.id.leftbtn,R.id.wrightbtn})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.leftbtn:
                leftbtn.setSelected(true);
                wrightbtn.setSelected(false);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(videoFragment);
                if(!picturecFragment.isAdded()) {
                    transaction.add(R.id.contentPanel,picturecFragment);
                }
                transaction.show(picturecFragment).commit();
                break;
            case R.id.wrightbtn:
                leftbtn.setSelected(false);
                wrightbtn.setSelected(true);
                if(videoFragment == null){
                    videoFragment = new MineVideoFragment();
                }
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.hide(picturecFragment);
                if(!videoFragment.isAdded()) {
                    transaction2.add(R.id.contentPanel,videoFragment);
                }
                transaction2.show(videoFragment).commit();
                break;
        }
    }

}
