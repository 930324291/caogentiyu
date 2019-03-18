package com.football.net.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.MineAdapter;
import com.football.net.bean.MessageTankBean;
import com.football.net.bean.MineBean;
import com.football.net.bean.ScoreListBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.MessageTankBeanResult;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.reponse.impl.SquareVideoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.MineCenter4NoRoleAtivity;
import com.football.net.ui.MineInfoActivity;
import com.football.net.ui.MinePersonalInfoAtivity;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.football.net.widget.MyRatingBar;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class Mine4NoRoleFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView textView;
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
    @BindView(R.id.returnBtn)
    ImageView returnBtn;


    int count = 0;
    ArrayList<MineBean> dataList = new ArrayList<MineBean> ();

    @Override
    protected void initView() {
        textView.setText("我");
        returnBtn.setVisibility(View.GONE);
        UserBean bean = FootBallApplication.userbean;
        name.setText(bean.getName());
        bodyHeight.setText("身高："+(bean.getHeight() == null? "暂无":bean.getHeight()+"CM"));
        tvposition.setText("擅长位置："+CommonUtils.getPositionStr(bean.getPosition()));
        ratingbar.setRating(bean.getOfficial());
        levelV.setText("Lv"+bean.getOfficial());
        bodyWeight.setText("体重："+(bean.getWeight() ==null? "暂无":bean.getWeight()+"KG"));
        if(bean.getTeam() != null){
            teamName.setText("所在球队："+bean.getTeam().getTeamTitle());
        }else{
            teamName.setText("所在球队：暂无");
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getIconUrl()),header,FootBallApplication.circOptions);


    }


    @OnClick({R.id.userCenter,R.id.header,R.id.myinfo,R.id.user_foot})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.userCenter:
                    mContext.startActivity(new Intent(mContext,MineCenter4NoRoleAtivity.class));

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
                Intent intent0 = new Intent(mContext,MinePersonalInfoAtivity.class);
                mContext.startActivity(intent0);
                break;
            case R.id.user_foot:
                //进入战术板
                if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() == null) {
                    ToastUtil.show(mContext, "您还没有加入任何球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", FootBallApplication.userbean.getTeam());
                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine_no_role;
    }
}
