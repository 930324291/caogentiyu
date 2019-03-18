package com.football.net.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.MineBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MyRatingBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/13.
 */
public class Mine4NoRoleAty extends BasicActivity {

//    @BindView(R.id.title)
//    TextView textView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.bodyHeight)
    TextView bodyHeight;
    @BindView(R.id.tvposition)
    TextView tvposition;
    @BindView(R.id.ratingbar)
    MyRatingBar ratingbar;
    @BindView(R.id.levelV)
    TextView levelV;
    @BindView(R.id.bodyWeight)
    TextView bodyWeight;
    @BindView(R.id.teamName)
    TextView teamName;
    @BindView(R.id.header)
    ImageView header;
//    @BindView(R.id.returnBtn)
//    ImageView returnBtn;


    int count = 0;
    ArrayList<MineBean> dataList = new ArrayList<MineBean>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine_no_role;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine;
    }

    protected void initView() {
//        textView.setText("我");
//        returnBtn.setVisibility(View.GONE);
        UserBean bean = FootBallApplication.userbean;
        name.setText(bean.getName());
        bodyHeight.setText("身高：" + (bean.getHeight() == null ? "暂无" : bean.getHeight() + "CM"));
        tvposition.setText("擅长位置：" + CommonUtils.getPositionStr(bean.getPosition()));
        ratingbar.setRating(bean.getOfficial());
        levelV.setText("Lv" + bean.getOfficial());
        bodyWeight.setText("体重：" + (bean.getWeight() == null ? "暂无" : bean.getWeight() + "KG"));
        if (bean.getTeam() != null) {
            teamName.setText("所在球队：" + bean.getTeam().getTeamTitle());
        } else {
            teamName.setText("所在球队：暂无");
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), header, FootBallApplication.circOptions);


    }


    @OnClick({R.id.userCenter, R.id.header, R.id.myinfo})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.userCenter:
                mContext.startActivity(new Intent(mContext, MineCenter4NoRoleAtivity_.class));

                break;
            case R.id.header:
//                mContext.startActivity(new Intent(mContext,MineInfoActivity.class));
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                    Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                    intent.putExtra("beanid", FootBallApplication.userbean.getId());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                    intent.putExtra("beanid", FootBallApplication.userbean.getId());
                    mContext.startActivity(intent);
                }
                break;
            case R.id.myinfo:
                Intent intent0 = new Intent(mContext, MinePersonalInfoAtivity_.class);
                mContext.startActivity(intent0);
                break;
        }
    }

}
