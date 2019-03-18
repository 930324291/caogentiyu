package com.football.net.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.GalleryAdapter2;
import com.football.net.bean.ActiveApplyBean;
import com.football.net.bean.TankInTeamBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.TimeUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.ActiveApplyBeanResult;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MyRatingBar;
import com.football.net.widget.NoScrollGridView;
import com.football.net.widget.SpaceItemDecoration;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class TeamAchievesActivity extends BasicActivity {

    @BindView(R.id.captainName)
    TextView captainName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.qqNum)
    TextView qqNum;
    @BindView(R.id.weixinNum)
    TextView weixinNum;
    @BindView(R.id.joinTime)
    TextView joinTime;
    @BindView(R.id.introduce)
    TextView introduce;

    @Override
    public int getLayoutId() {
        return R.layout.activity_football_team_record;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_football_info_dangan;
    }

    @Override
    protected void initView() {
        TeamBean bean = FootBallApplication.userbean.getTeam();
        UserBean userBean = FootBallApplication.userbean;
        if (null == bean) {
            return;
        }
        captainName.setText("队长：" + userBean.getName());
        phone.setText("手机：" + userBean.getMobile());
        qqNum.setText("QQ：" + userBean.getQq());
        weixinNum.setText("微信号：" + userBean.getWechat());
        joinTime.setText("加入日期：" + TimeUtils.transferLongToDate("yyyy-MM-dd", userBean.getCreateTime()));
        introduce.setText(Html.fromHtml(StringUtils.isEmpty(bean.getIntroduce()) ? "暂无" : bean.getIntroduce()));
    }


}
