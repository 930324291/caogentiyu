package com.football.net.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.football.net.R;
import com.football.net.fragment.MinePicturecFragment;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.ui.shouye.FootballTeamFragment;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/7/30.
 */

public class TeamListActivity extends BasicActivity{

    @BindView(R.id.rootLayout)
    RelativeLayout rootLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_team_list;
    }

    @Override
    public int getTitleRes() {
        return R.string.txt_join_team;
    }

    @Override
    protected void initView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FootballTeamFragment footballTeamFragment = FootballTeamFragment.newInstance();
        if(!footballTeamFragment.isAdded()) {
            transaction.add(R.id.rootLayout,footballTeamFragment);
        }
        transaction.show(footballTeamFragment).commit();
    }
}
