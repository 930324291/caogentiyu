package com.football.net.fragment;

import android.content.Intent;
import android.view.View;

import com.football.net.R;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.FabuLookForTeamActivity_;
import com.football.net.ui.FabuPictureActivity_;
import com.football.net.ui.FabuVideoActivity_;

import butterknife.OnClick;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class FaBuFragment2 extends BaseFragment {


    @Override
    public int getLayoutId() {
        return R.layout.fragment_fabu2;
    }
    @Override
    protected void initView() {

    }
    @OnClick({R.id.tab1,R.id.tab2,R.id.tab3})
    void onClick(View v){
        if (CommonUtils.isFastClick()) {
            return;
        }

//        if (FootBallApplication.userbean.getAuditStatus() != 1) {
//            showMsg("您的帐号目前尚未通过审核！");
//            return;
//        }

        switch (v.getId()) {
            case R.id.tab1:
                startActivity(new Intent(getActivity(),FabuPictureActivity_.class));
                break;
            case R.id.tab2:
                startActivity(new Intent(getActivity(),FabuVideoActivity_.class));
                break;
            case R.id.tab3:
                startActivity(new Intent(getActivity(),FabuLookForTeamActivity_.class));
                break;
        }
    }
}
