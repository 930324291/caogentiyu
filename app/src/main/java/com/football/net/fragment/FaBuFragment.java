package com.football.net.fragment;

import android.content.Intent;
import android.view.View;

import com.football.net.R;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.FabuInnerMessageActivity;
import com.football.net.ui.FabuLookForPeopleActivity;
import com.football.net.ui.FabuPictureActivity;
import com.football.net.ui.FabuScoreTeamListActivity;
import com.football.net.ui.FabuSigninMessageActivity;
import com.football.net.ui.FabuVideoActivity;


import butterknife.OnClick;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class FaBuFragment extends BaseFragment {


    @Override
    public int getLayoutId() {
        return R.layout.fragment_fabu;
    }
    @Override
    protected void initView() {

    }
    @OnClick({R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4,R.id.tab5,R.id.tab6})
    void onClick(View v){
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tab1:
                startActivity(new Intent(getActivity(),FabuPictureActivity.class));
                break;
            case R.id.tab2:
                startActivity(new Intent(getActivity(),FabuVideoActivity.class));
                break;
            case R.id.tab3:
                startActivity(new Intent(getActivity(),FabuLookForPeopleActivity.class));
                break;
            case R.id.tab4:
                startActivity(new Intent(getActivity(),FabuScoreTeamListActivity.class));
                break;
            case R.id.tab5:
                startActivity(new Intent(getActivity(),FabuSigninMessageActivity.class));
                break;
            case R.id.tab6:
                startActivity(new Intent(getActivity(),FabuInnerMessageActivity.class));
                break;
        }
    }
}
