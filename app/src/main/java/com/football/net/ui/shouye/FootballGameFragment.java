package com.football.net.ui.shouye;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.football.net.R;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.CommStatus;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/10.
 * 赛事
 */
public class FootballGameFragment extends BaseFragment {

    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    FragmentManager fragmentManager;
    ArrayList<BaseFragment> fragments;
    int currentIndex = 0;
    private ArrayList<View> tvList;
    //存储id
    private int mSaveId;

    public void setmSaveId(int mSaveId) {
        this.mSaveId = mSaveId;

    }

    public static FootballGameFragment newInstance() {
        Bundle args = new Bundle();
        FootballGameFragment fragment = new FootballGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_game;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        fragments = new ArrayList<>();

        Bundle newBundle = new Bundle();
        newBundle.putInt(FootBallNewAndHisGameFragment.TYPE, 0);
        FootBallNewAndHisGameFragment newFragment = FootBallNewAndHisGameFragment.newInstance(newBundle);  // 创建最新比赛模块

        Bundle hisBundle = new Bundle();
        hisBundle.putInt(FootBallNewAndHisGameFragment.TYPE, 1);
        FootBallNewAndHisGameFragment hisFragment = FootBallNewAndHisGameFragment.newInstance(hisBundle);  // 创建历史比赛模块

        fragments.add(newFragment);  // 将最新比赛模块添加进模块集合
        fragments.add(hisFragment);  // 将历史比赛模块添加进模块集合

        tvList = new ArrayList<>();  // 创建一个ImageView下划图标集合
        tvList.add(line1);
        tvList.add(line2);
        line1.setVisibility(View.VISIBLE);  // 初始化时只显示最新赛事的下划图标
        line2.setVisibility(View.INVISIBLE);  // 初始化时不显示历史赛事的下划图标

        fragmentManager = getActivity().getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded()) {
            transaction.add(R.id.content, newFragment);  // 默认情况下如果未加入最新赛事模块，则主动添加
        }
        transaction.show(fragments.get(currentIndex)).commit();  // 显示最新赛事模块

        jumpGmae();
    }

    @OnClick({R.id.game_leftbtn, R.id.game_rightbtn})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = currentIndex;
        switch (v.getId()) {
            case R.id.game_leftbtn:
                index = 0;
                CommStatus.JUMP_GAME = -1;
                CommStatus.MNEWSGAMEID = -1;
                CommStatus.MHISGAMEID = -1;
                BaseEvent news = new BaseEvent();
                news.data = IntentKey.FootGame.KEY_REFRESH_GAME;
                EventBus.getDefault().post(news);

                // leftBtn.setVisibility(View.VISIBLE);
                // rightBtn.setVisibility(View.INVISIBLE);

                break;
            case R.id.game_rightbtn:
                index = 1;
                CommStatus.JUMP_GAME = -1;
                CommStatus.MNEWSGAMEID = -1;
                CommStatus.MHISGAMEID = -1;
                BaseEvent news1 = new BaseEvent();
                news1.data = IntentKey.FootGame.KEY_REFRESH_GAME;
                EventBus.getDefault().post(news1);
                // leftBtn.setVisibility(View.INVISIBLE);
                // rightBtn.setVisibility(View.VISIBLE);
                break;
        }
        if (currentIndex != index) {
            tvList.get(currentIndex).setVisibility(View.INVISIBLE);  // 切换选项卡时，设置当前选项卡的下划图标不可见
            tvList.get(index).setVisibility(View.VISIBLE);  // 切换选项卡时，设置新选项卡的下划图标可见

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments.get(currentIndex));  // 隐藏当前选项卡对应的赛事模块
            if (!fragments.get(index).isAdded()) {
                transaction.add(R.id.content, fragments.get(index));  // 新选项卡对应的赛事模块如果未添加则进行添加进事务
            }
            transaction.show(fragments.get(index)).commit();  // 显示新选项卡对应的赛事模块
        }
        currentIndex = index;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        String data = event.data;
        int index = currentIndex;
        if (TextUtils.equals(data, IntentKey.FootGame.KEY_NEWS_GAME)) {
            //跳转最新赛事
            index = 0;
        } else if (TextUtils.equals(data, IntentKey.FootGame.KEY_HIS_GAME)) {
            //跳转历史赛事
            index = 1;
        }
        if (currentIndex != index) {
            tvList.get(currentIndex).setVisibility(View.INVISIBLE);  // 切换选项卡时，设置当前选项卡的下划图标不可见
            tvList.get(index).setVisibility(View.VISIBLE);  // 切换选项卡时，设置新选项卡的下划图标可见

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments.get(currentIndex));  // 隐藏当前选项卡对应的赛事模块
            if (!fragments.get(index).isAdded()) {
                transaction.add(R.id.content, fragments.get(index));  // 新选项卡对应的赛事模块如果未添加则进行添加进事务
            }
            transaction.show(fragments.get(index)).commit();  // 显示新选项卡对应的赛事模块
        }
        currentIndex = index;
    }

    public void jumpGmae() {
        int index = CommStatus.JUMP_GAME;
        if (index != -1){
            if (currentIndex != index) {
                tvList.get(currentIndex).setVisibility(View.INVISIBLE);  // 切换选项卡时，设置当前选项卡的下划图标不可见
                tvList.get(index).setVisibility(View.VISIBLE);  // 切换选项卡时，设置新选项卡的下划图标可见

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(fragments.get(currentIndex));  // 隐藏当前选项卡对应的赛事模块
                if (!fragments.get(index).isAdded()) {
                    transaction.add(R.id.content, fragments.get(index));  // 新选项卡对应的赛事模块如果未添加则进行添加进事务
                }
                transaction.show(fragments.get(index)).commit();  // 显示新选项卡对应的赛事模块
            }
            currentIndex = index;
        }
    }
}
