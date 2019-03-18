package com.football.net.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.fragment.FootBallTeamMemberFragment;
import com.football.net.fragment.FootBallTeamRecordFragment;
import com.football.net.fragment.FootBallTeamZhanjiFragment;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.TeamBean2Result;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class HelpActivity extends BasicActivity {



    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine_help;
    }

    @Override
    protected void initView() {

    }



}
