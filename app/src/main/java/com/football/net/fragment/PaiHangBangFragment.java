package com.football.net.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Fragment
 */

public class PaiHangBangFragment extends BaseFragment {

    @BindView(R.id.contentPanel)
    LinearLayout contentPanel;
    @BindView(R.id.leftbtn)
    TextView leftbtn;
    @BindView(R.id.wrightbtn)
    TextView wrightbtn;
    @BindView(R.id.btn1)
    TextView btn1;
    @BindView(R.id.btn2)
    TextView btn2;
    @BindView(R.id.btn3)
    TextView btn3;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;

    FragmentManager fragmentManager;
    Fragment[][] fragments = new Fragment[2][3];
    int index = 0, index2 = 0;
    ;

    //    public void setFragmentManager(FragmentManager mag){
//        this.fragmentManager = mag;
//    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_paihangbang;
    }

    @Override
    protected void initView() {
        fragments[0][0] = PaiHangBangTeamFragment.newInstance("likeNum desc");

        leftbtn.setSelected(true);
        wrightbtn.setSelected(false);
        btn1.setSelected(true);
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.VISIBLE);
        fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragments[0][0];
        transaction.hide(fragment);
        if (!fragment.isAdded()) {
            transaction.add(R.id.contentPanel, fragment);
        }
        transaction.show(fragment).commit();


    }

    private void createFragment(int i, int j) {
        if (i == 0 && j == 0) {
            fragments[0][0] = PaiHangBangTeamFragment.newInstance("likeNum desc", 0);
        } else if (i == 0 && j == 1) {
            fragments[0][1] = PaiHangBangTeamFragment.newInstance("value desc", 1);
        } else if (i == 0 && j == 2) {
            fragments[0][2] = PaiHangBangTeamFragment.newInstance("point desc", 2);
        } else if (i == 1 && j == 0) {
            fragments[1][0] = PaiHangBangMemberFragment.newInstance("likeNum desc", 0);
        } else if (i == 1 && j == 1) {
            fragments[1][1] = PaiHangBangMemberFragment.newInstance("value desc", 1);
        } else if (i == 1 && j == 2) {
            fragments[1][2] = PaiHangBangMemberFragment.newInstance("point desc", 2);
        }
    }


    @OnClick({R.id.leftbtn, R.id.wrightbtn, R.id.btn1, R.id.btn2, R.id.btn3})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragments[index][index2];
        transaction.hide(fragment);
        switch (v.getId()) {
            case R.id.leftbtn:
                index = 0;
                leftbtn.setSelected(true);
                wrightbtn.setSelected(false);
                break;
            case R.id.wrightbtn:
                index = 1;
                leftbtn.setSelected(false);
                wrightbtn.setSelected(true);
                break;
            case R.id.btn1:
                index2 = 0;
                btn1.setSelected(true);
                btn2.setSelected(false);
                btn3.setSelected(false);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                break;
            case R.id.btn2:
                index2 = 1;
                btn1.setSelected(false);
                btn2.setSelected(true);
                btn3.setSelected(false);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn3:
                index2 = 2;
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(true);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                break;
        }
        Fragment fragment2 = fragments[index][index2];
        if (fragments[index][index2] == null) {
            createFragment(index, index2);
            fragment2 = fragments[index][index2];
        }
        if (!fragment2.isAdded()) {
            transaction.add(R.id.contentPanel, fragment2);
        }
        transaction.show(fragment2).commit();
    }

}
