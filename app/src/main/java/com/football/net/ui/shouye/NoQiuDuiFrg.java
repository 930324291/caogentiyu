package com.football.net.ui.shouye;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.util.CommonUtils;
import com.football.net.fragment.InformFootBallFragment;
import com.football.net.manager.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/13.
 */
public class NoQiuDuiFrg extends BaseFragment {

    @BindView(R.id.title)
    TextView textView;
    @BindView(R.id.returnBtn)
    ImageView returnBtn;

    @BindView(R.id.btn1)
    TextView btn1;

    FragmentManager fragmentManager;
    ArrayList<BaseFragment> fragments;
    int currentIndex = 0;

    @Override
    public int getLayoutId() {
        return R.layout.frg_no_qiudui;
    }

    @Override
    protected void initView() {
        textView.setText("通知");
        returnBtn.setVisibility(View.GONE);

        fragments = new ArrayList<>();
        InformFootBallFragment fragment1 = new InformFootBallFragment( );

        fragments.add(fragment1);

        setBtn(0);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!fragments.get(currentIndex).isAdded()) {
            transaction.add(R.id.content, fragment1);
        }
        transaction.show(fragments.get(currentIndex)).commit();
    }

    @OnClick({R.id.btn1})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = 0;
        switch (v.getId()) {
            case R.id.btn1:
                index = 0;
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
    };
}
