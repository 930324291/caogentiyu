package com.football.net.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.util.CommonUtils;
import com.football.net.fragment.InformAppointmentFragment;
import com.football.net.fragment.InformEventASignCaptionFragment;
import com.football.net.fragment.InformEventASignFragment;
import com.football.net.fragment.InformFootBallFragment;
import com.football.net.fragment.InformNewMemberFragment;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.BasicActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class InformCaptainActivity extends BasicActivity {


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
        return R.layout.activity_inform_captain;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_inform;
    }

    @Override
    protected void initView() {
        fragments = new ArrayList<>();
        InformFootBallFragment fragment1 = new InformFootBallFragment( );
        InformAppointmentFragment fragment2 = new InformAppointmentFragment();
        InformEventASignCaptionFragment fragment3 = new InformEventASignCaptionFragment();
        InformNewMemberFragment fragment4 = new InformNewMemberFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        setBtn(0);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded()) {
            transaction.add(R.id.content, fragment1);
        }
        transaction.show(fragments.get(currentIndex)).commit();

    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onClick(View v) {
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

    private void setBtn(int index){
        btn1.setSelected(index == 0);
        btn2.setSelected(index == 1);
        btn3.setSelected(index == 2);
        btn4.setSelected(index == 3);
    };

}
