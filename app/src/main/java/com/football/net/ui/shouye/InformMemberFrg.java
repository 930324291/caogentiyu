package com.football.net.ui.shouye;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.util.CommonUtils;
import com.football.net.fragment.InformEventASignFragment;
import com.football.net.fragment.InformFootBallFragment;
import com.football.net.fragment.InformLookForPeopleFragment;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.DensityUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/13.
 */
public class InformMemberFrg extends BaseFragment {


    @BindView(R.id.rel_header)
    RelativeLayout mRelaTitle;
    @BindView(R.id.id_content_1_rela)
    RelativeLayout mRelaContent1;
    @BindView(R.id.content)
    RelativeLayout mRelaContent2;

    @BindView(R.id.title)
    TextView textView;
    @BindView(R.id.right_txt)
    TextView textRight;
    @BindView(R.id.returnBtn)
    ImageView returnBtn;

    @BindView(R.id.btn1)
    TextView btn1;
    @BindView(R.id.btn2)
    TextView btn2;
    @BindView(R.id.btn3)
    TextView btn3;
    @BindView(R.id.btn4)
    TextView btn4;

    FragmentManager fragmentManager;
    ArrayList<BaseFragment> fragments;
    int currentIndex = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inform_member;
    }

    @Override
    protected void initView() {
        int width = DensityUtils.getScreenW(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, DensityUtils.dip2px(mContext, 48));
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(width, DensityUtils.dip2px(mContext, 40));
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        mRelaTitle.setLayoutParams(params);
        mRelaContent1.setLayoutParams(params1);
        mRelaContent2.setLayoutParams(params2);

        textView.setText("通知");
        textRight.setVisibility(View.GONE);
        returnBtn.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        InformFootBallFragment fragment1 = new InformFootBallFragment();
        InformEventASignFragment fragment2 = InformEventASignFragment.newInstance("signMessage");
        InformEventASignFragment fragment3 = InformEventASignFragment.newInstance("innerMessage");
        InformLookForPeopleFragment fragment4 = new InformLookForPeopleFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        setBtn(0);
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded()) {
            transaction.add(R.id.content, fragment1);
        }
        transaction.show(fragments.get(currentIndex)).commit();

    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = 0;
        switch (v.getId()) {
            case R.id.btn1:
                index = 0;
                break;
            case R.id.btn2:
                index = 1;
                break;
            case R.id.btn3:
                index = 2;
                break;
            case R.id.btn4:
                index = 3;
                break;
        }
        if (currentIndex != index) {

            if (index > 0 && FootBallApplication.userbean.getAuditStatus() != 1) {
                showMsg("您的帐号目前尚未通过审核！");
                return;
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments.get(currentIndex));
            if (!fragments.get(index).isAdded()) {
                transaction.add(R.id.content, fragments.get(index));
            }
            transaction.show(fragments.get(index)).commit();
            currentIndex = index;
            setBtn(index);
        }
    }

    private void setBtn(int index) {
        btn1.setSelected(index == 0);
        btn2.setSelected(index == 1);
        btn3.setSelected(index == 2);
        btn4.setSelected(index == 3);
    }

    ;

}
