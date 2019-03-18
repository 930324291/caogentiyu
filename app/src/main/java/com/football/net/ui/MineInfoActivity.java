package com.football.net.ui;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.PlayerInfoAdapter;
import com.football.net.adapter.PlayerTeamHisAdapter;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.bean.UserInfoBean;
import com.football.net.bean.UserInfoBean2;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.NoScrollListview;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class MineInfoActivity extends BasicActivity {


    @BindView(R.id.lv_player_info)
    NoScrollListview lvPlayerInfo;
    @BindView(R.id.lv_player_team)
    NoScrollListview lvPlayerTeam;
    @BindView(R.id.img_teamicon)
    ImageView imgIcon;
    @BindView(R.id.tv_teamName)
    TextView tvName;
    UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_info;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine_info;
    }

    @Override
    protected void initView() {
        userBean = FootBallApplication.userbean;

        TeamBean teamBean = FootBallApplication.userbean.getTeam();

        ArrayList<UserInfoBean> list1 = new ArrayList<>();
        list1.add(new UserInfoBean(R.mipmap.name_icon, "姓名：", userBean.getName()));
        list1.add(new UserInfoBean(R.mipmap.nickname_icon, "昵称：", userBean.getNickname()));
        list1.add(new UserInfoBean(R.mipmap.sex_icon, "性别：", (userBean.getGender() == 1 ? "男" : "女")));
        list1.add(new UserInfoBean(R.mipmap.height_icon, "身高：", userBean.getHeight() == null ? "暂无身高" : userBean.getHeight() + "CM"));
        list1.add(new UserInfoBean(R.mipmap.weight_icon, "体重：", userBean.getWeight() == null ? "体重暂无" : userBean.getWeight() + "KG"));
        list1.add(new UserInfoBean(R.mipmap.number_icon_1, "场上号码：", userBean.getUniformNumber()));
        list1.add(new UserInfoBean(R.mipmap.phone_icon_1, "电话：", "暂无"));
        list1.add(new UserInfoBean(R.mipmap.qq_icon, "QQ：", "暂无"));
        list1.add(new UserInfoBean(R.mipmap.wechatz_icon, "微信号：", "暂无"));
        lvPlayerInfo.setAdapter(new PlayerInfoAdapter(this, list1));

        ArrayList<UserInfoBean2> list2 = new ArrayList<UserInfoBean2>();
        TeamBean teamBean1 = userBean.getTeam();
        if (teamBean1 != null) {
            UserInfoBean2 teaminfor = new UserInfoBean2();
            teaminfor.setTeamName(teamBean1.getTeamTitle());
            teaminfor.setUrl(CommonUtils.getRurl(teamBean1.getIconUrl()));
            teaminfor.setFromTime(teamBean1.getRegistTime());
            list2.add(teaminfor);
        }
        lvPlayerTeam.setAdapter(new PlayerTeamHisAdapter(this, list2));
        tvName.setText(teamBean1.getTeamTitle());
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(teamBean1.getIconUrl()),imgIcon, FootBallApplication.options);
    }

}
