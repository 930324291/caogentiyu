package com.football.net.fragment;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.TeamBean;
import com.football.net.manager.BaseFragment;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class FootBallTeamRecordFragment extends BaseFragment{

    @BindView(R.id.captainName)
    TextView captainName;  // 队长名称
    @BindView(R.id.phone)
    TextView phone;  // 手机
    @BindView(R.id.qqNum)
    TextView qqNum;  // QQ
    @BindView(R.id.weixinNum)
    TextView weixinNum;  // 微信
    @BindView(R.id.joinTime)
    TextView joinTime;  // 加入时间
    @BindView(R.id.introduce)
    TextView introduce;  // 球队简介

    String captainNameStr = "";
    String phoneStr = "";
    String qqNumStr = "";
    String weixinNumStr = "";
    String joinTimeStr = "";

    public static FootBallTeamRecordFragment newInstance(TeamBean bean) {
        
        Bundle args = new Bundle();
        args.putSerializable("teamBean",bean);
        FootBallTeamRecordFragment fragment = new FootBallTeamRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_team_record;
    }

    @Override
    protected void initView() {
        TeamBean bean = (TeamBean) getArguments().getSerializable("teamBean");
        captainName.setText("队长："+captainNameStr);
        phone.setText("手机："+phoneStr);
        qqNum.setText("QQ："+qqNumStr);
        weixinNum.setText("微信号："+weixinNumStr);
        joinTime.setText("加入日期："+joinTimeStr.split(" ")[0]);
        introduce.setText(Html.fromHtml(bean.getIntroduce() == null? "":bean.getIntroduce()));
    }

    public void setCaptainName(String name){
        captainNameStr = name;
        if(captainName!=null){
            captainName.setText("队长：" + captainNameStr);
        }
    }

    public void setPhone(String tel) {
        phoneStr = tel;
        if (this.phone!=null) {
            this.phone.setText("手机："+phoneStr);
        }
    }

    public void setQqNum(String num) {
        qqNumStr = num;
        if (this.qqNum!=null) {
            this.qqNum.setText("QQ："+qqNumStr);
        }
    }

    public void setWeixinNum(String num) {
        weixinNumStr = num;
        if (this.weixinNum!=null) {
            this.weixinNum.setText("微信号："+weixinNumStr);
        }
    }

    public void setJoinTime(String time) {
        joinTimeStr = time;
        if (this.joinTime!=null) {
            this.joinTime.setText("加入日期："+joinTimeStr.split(" ")[0]);
        }
    }
}
