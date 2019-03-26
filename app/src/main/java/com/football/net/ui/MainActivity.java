package com.football.net.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.fileIo.SharePref;
import com.football.net.common.util.CommonUtils;
import com.football.net.fragment.FaBuFragment;
import com.football.net.fragment.FaBuFragment2;
import com.football.net.fragment.PaiHangBangFragment;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.UnreadNumResult;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.FootballTeamFragment1;
import com.football.net.ui.shouye.InformCaptainFrg;
import com.football.net.ui.shouye.InformMemberFrg;
import com.football.net.ui.shouye.MainFragment;
import com.football.net.ui.shouye.SquareFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 定义一个Activity，使用androidannotations注解
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.viewpager)
    ViewPager viewPager;

    @ViewById(R.id.rel_shouye)
    View rel_shouye;
    @ViewById(R.id.iv_shouye)
    ImageView iv_shouye;
    @ViewById(R.id.tv_shouye)
    TextView tv_shouye;

    @ViewById(R.id.rel_paihangbang)
    View rel_paihangbang;
    @ViewById(R.id.iv_paihangbang)
    ImageView iv_paihangbang;
    @ViewById(R.id.tv_paihangbang)
    TextView tv_paihangbang;

    @ViewById(R.id.rel_fabu)
    View rel_fabu;
    @ViewById(R.id.iv_fabu)
    ImageView iv_fabu;
    @ViewById(R.id.tv_fabu)
    TextView tv_fabu;

    @ViewById(R.id.rel_wo)
    View rel_wo;
    @ViewById(R.id.iv_wo)
    ImageView iv_wo;
    @ViewById(R.id.tv_wo)
    TextView tv_wo;

    @ViewById(R.id.add)
    ImageView add;
    @ViewById(R.id.dot)
    ImageView dot;

    Fragment[] fragments;
    int currentPosition;
//    private MainFragment mainFragment;
    private FootballTeamFragment1 mainFragment;

    //androidannotations注解, 初始化view
    @AfterViews
    void initView() {
        setResult(12);

//        mainFragment = new MainFragment();
        mainFragment = FootballTeamFragment1.newInstance();
        // 排行榜界面
        PaiHangBangFragment phbFragment = new PaiHangBangFragment();

        // 发布界面
        Fragment faBuFragment = new FaBuFragment2(); // 默认队员--发布模块
        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) { // 判断是否为队长
            faBuFragment = new FaBuFragment(); // 队长--发布模块
        }

        // 通知界面
        Fragment mineFragment = new InformMemberFrg();
        // 队员在未审核或者审核不通过情况下(刚注册时)，通知界面只显示草根足球界面，其它界面提示账号待审核
        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) {
            // 队长
            if (FootBallApplication.userbean.getAuditStatus() == 1) {
                mineFragment = new InformCaptainFrg();

//                if (FootBallApplication.userbean.getTeam() == null) { //创建球队
//                    mineFragment = new CreateFootballTeamFrg();
//                } else {
//                }
            }
        }

//        else if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
//            // 队员
//            if (FootBallApplication.userbean.getAuditStatus() == 1) {
//                mineFragment = new InformMemberFrg();

//                if (FootBallApplication.userbean.getTeam() == null) { //创建球队
//                    mineFragment = new CreateFootballTeamFrg();
//                } else {
//                }
//            }
//        }

//        fragments = new Fragment[]{mainFragment, phbFragment, faBuFragment, mineFragment};
        fragments = new Fragment[]{mainFragment, faBuFragment};
        viewPager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        currentPosition = 0;
        setBottomTab(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setBottomTab(position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        viewPager.setOffscreenPageLimit(4);
        if (FootBallApplication.userbean != null && FootBallApplication.userbean.getTeam() != null && !TextUtils.isEmpty(FootBallApplication.userbean.getTeam().getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(FootBallApplication.userbean.getTeam().getIconUrl()), add, FootBallApplication.circOptions);
        }
        loadDotData();
    }

    public class MainFragmentAdapter extends FragmentPagerAdapter {

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    @Click({R.id.rel_shouye, R.id.rel_paihangbang, R.id.rel_fabu, R.id.rel_wo, R.id.add})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.rel_shouye:
                setCurrentViewPager(0);
                break;
            case R.id.rel_paihangbang:
                setCurrentViewPager(1);
                break;
            case R.id.rel_fabu:
                setCurrentViewPager(2);
                break;
            case R.id.rel_wo:
                setCurrentViewPager(3);
                break;
            case R.id.add:
                // 默认队员--我模块
                Intent intent = new Intent(this, Mine4MemberAty.class);
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) { // 判断是否为队长
                    intent = new Intent(this, Mine4CaptainAty.class);  // 队长--我模块
                }
                if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() == null) { // no join team so no role
                    intent = new Intent(this, Mine4NoRoleAty.class); // 当未加入任何球队时--我模块
                }
                startActivity(intent);
                break;
        }
    }

    private void setCurrentViewPager(int i) {
        if (i != currentPosition) {
            viewPager.setCurrentItem(i, false);
            currentPosition = i;
        }
    }

    private void setBottomTab(int position) {
        iv_shouye.setSelected(position == 0);
        tv_shouye.setSelected(position == 0);
        iv_paihangbang.setSelected(position == 1);
        tv_paihangbang.setSelected(position == 1);
        iv_fabu.setSelected(position == 2);
        tv_fabu.setSelected(position == 2);
        iv_wo.setSelected(position == 3);
        tv_wo.setSelected(position == 3);
        if (position == 3) {
            dot.setVisibility(View.GONE);
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (currentPosition != 0) {
            setCurrentViewPager(0);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showMsg("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                exitApp();
            }
        }
    }

    //一般load开头的都是加载数据方法，加载完成后在回调方法中刷新相应界面
    public void loadDotData() {
//        long lastReadTime = SharePref.getLastReadTime(this);
//        SmartParams params = new SmartParams();
//        params.put("lastReadTime", lastReadTime);
//        params.put("playerId", FootBallApplication.userbean.getId());
//        //3.请求数据
//        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + " getUnreadCnt", params, new SmartCallback<UnreadNumResult>() {
//
//            @Override
//            public void onSuccess(int statusCode, UnreadNumResult result) {
//                SharePref.setLastReadTime(MainActivity.this, System.currentTimeMillis());
//                FootBallApplication.unreadNumResult = result;
//                setUnreaNumTag(result);
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//            }
//
//        }, UnreadNumResult.class);
    }

    private void setUnreaNumTag(UnreadNumResult result) {
        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN && FootBallApplication.userbean.getTeam() != null) {
            if (result != null) {
                if (result.getUnReadCnt2() > 0 || result.getUnReadCnt3() > 0) {
                    dot.setVisibility(View.VISIBLE);
                } else {
                    dot.setVisibility(View.GONE);
                }
            }
        } else if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember && FootBallApplication.userbean.getTeam() != null) {
            if (result != null) {
                if (result.getUnReadCnt4() > 0 || result.getUnReadCnt7() > 0) {
                    dot.setVisibility(View.VISIBLE);
                } else {
                    dot.setVisibility(View.GONE);
                }
            }
        }

    }
}
