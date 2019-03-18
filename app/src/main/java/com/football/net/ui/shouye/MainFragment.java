package com.football.net.ui.shouye;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.CommStatus;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.SearchResultActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class MainFragment extends BaseFragment {

    @BindView(R.id.home_tablayout)
    TabLayout homeTablayout; // 导航栏
    @BindView(R.id.searchEdit)
    EditText searchEdit; // 搜索框
    @BindView(R.id.home_viewpager)
    ViewPager homeViewpager; // 主页显示页

    private String[] titleArr;
    private int[] iconArr;
    private ArrayList<BaseFragment> fragmentList;
    private HomePagerAdapter pagerAdapter;

    //记录当前位置
    public int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shouye;
    } // 首页布局

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initData();
        tabViewSetView();
        resetTablayout();
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String str = searchEdit.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        showMsg("请输入内容");
                    } else {
                        Intent intent = new Intent(mContext, SearchResultActivity.class);
                        intent.putExtra("condition", str);
                        mContext.startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void resetTablayout() {
        /**
         * 使用tablayout + viewpager时注意 如果设置了setupWithViewPager
         * 则需要重新执行下方对每个条目赋值
         * 否则会出现icon文字不显示的bug
         */
//        for (int i = 0; i < titleArr.length; i++) {
//            homeTablayout.getTabAt(i).setText(titleArr[i]);
//        }

        for (int i = 0; i < homeTablayout.getTabCount(); i++) {
            TabLayout.Tab tab = homeTablayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
                if (i == 0) {
                    currentPosition = i;
                    tab.getCustomView().findViewById(R.id.tv_home_table).setSelected(true);
                    tab.getCustomView().findViewById(R.id.iv_home_table).setSelected(true);
                }
            }
        }
        homeViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.searchBtn})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.searchBtn:
                String str = searchEdit.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    showMsg("请输入内容");
                    return;
                }
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("condition", str);
                mContext.startActivity(intent);
                break;
        }
    }


    private void tabViewSetView() {
        homeTablayout.setTabMode(TabLayout.MODE_FIXED);
        pagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        homeViewpager.setAdapter(pagerAdapter);
        homeTablayout.setupWithViewPager(homeViewpager);
        homeTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_home_table).setSelected(true);
                tab.getCustomView().findViewById(R.id.iv_home_table).setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_home_table).setSelected(false);
                tab.getCustomView().findViewById(R.id.iv_home_table).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initData() {
        titleArr = UIUtils.getStringArray(R.array.home_title_tab);
        iconArr = new int[]{R.drawable.home_title_tab_football_selector,
                R.drawable.home_title_tab_square_selector,
                R.drawable.home_title_tab_team_selector,
                R.drawable.home_title_tab_player_selector,
                R.drawable.home_title_tab_match_selector,
                R.drawable.home_title_tab_transfer_selector,
                R.drawable.home_title_tab_footballground_selector};

        fragmentList = new ArrayList<BaseFragment>();
        /** 头条 */
        FirstNewsFragment firstNewsFragment = FirstNewsFragment.newInstance();
        /** 广场 */
        SquareFragment squareFragment = SquareFragment.newInstance();
        /** 球队 */
        FootballTeamFragment1 footballTeamFragment = FootballTeamFragment1.newInstance();
        /** 球员 */
        FootballPlayerFragment1 footballPlayerFragment = FootballPlayerFragment1.newInstance();
        /** 赛事 */
        FootballGameFragment footballGameFragment = FootballGameFragment.newInstance();
        /** 转会 */
        ChangeTeamFragment changeTeamFragment = ChangeTeamFragment.newInstance();
        /** 球场 */
        FootballCourtFragment footballCourtFragment = FootballCourtFragment.newInstance();
        fragmentList.add(firstNewsFragment);
        fragmentList.add(squareFragment);
        fragmentList.add(footballTeamFragment);
        fragmentList.add(footballPlayerFragment);
        fragmentList.add(footballGameFragment);
        fragmentList.add(changeTeamFragment);
        fragmentList.add(footballCourtFragment);
    }

    public class HomePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<BaseFragment> fragments;

        public HomePagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragmentList) {
            super(fm);
            fragments = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        public View getTabView(final int position) {
            currentPosition = position;
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.home_tab_title_view, null);
            TextView tv = (TextView) v.findViewById(R.id.tv_home_table);
            tv.setText(titleArr[position]);
            ImageView img = (ImageView) v.findViewById(R.id.iv_home_table);
            img.setImageResource(iconArr[position]);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeViewpager.setCurrentItem(position, false);
                    CommStatus.JUMP_GAME = -1;
                    CommStatus.MNEWSGAMEID = -1;
                    CommStatus.MHISGAMEID = -1;
                    BaseEvent news = new BaseEvent();
                    news.data = IntentKey.FootGame.KEY_REFRESH_GAME;
                    EventBus.getDefault().post(news);
                }
            });
            return v;
        }
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
        if (TextUtils.equals(data, IntentKey.FootGame.KEY_NEWS_GAME)) {
            //跳转最新赛事
            setCurrentPos(4);
        } else if (TextUtils.equals(data, IntentKey.FootGame.KEY_HIS_GAME)) {
            //跳转历史赛事
            setCurrentPos(4);
        }
    }

    public void setCurrentPos(int pos) {
        currentPosition = pos;
        homeViewpager.setCurrentItem(pos, false);

        TabLayout.Tab tab = homeTablayout.getTabAt(pos);
        if (tab != null) {
            if (null == tab.getCustomView()) {
                tab.setCustomView(pagerAdapter.getTabView(pos));
            } else {
                tab.getCustomView().findViewById(R.id.tv_home_table).setSelected(true);
                tab.getCustomView().findViewById(R.id.iv_home_table).setSelected(true);
            }
        }
    }
}
