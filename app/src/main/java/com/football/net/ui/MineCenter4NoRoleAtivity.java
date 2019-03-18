package com.football.net.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.fileIo.SharePref;
import com.football.net.common.util.CommonUtils;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MyRatingBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_mine_center4norole)
public class MineCenter4NoRoleAtivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.bodyHeight)
    TextView bodyHeight;
    @ViewById(R.id.tvposition)
    TextView tvposition;
    @ViewById(R.id.ratingbar)
    MyRatingBar ratingbar;
    @ViewById(R.id.levelV)
    TextView levelV;
    @ViewById(R.id.bodyWeight)
    TextView bodyWeight;
    @ViewById(R.id.teamName)
    TextView teamName;
    @ViewById(R.id.header)
    ImageView header;

    @AfterViews
    void initView() {
        title.setText("用户中心");

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

    @Click({R.id.imageMyinfo,R.id.layout1,R.id.layout2,R.id.layout3,R.id.layout4,R.id.layout8})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.imageMyinfo: {
                Intent intent = new Intent(this, MineInfoActivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.layout1:{
                Intent intent1 = new Intent(this,MinePicture7VideoActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout2:{
                Intent intent1 = new Intent(this,MineRecurit4NoRoleActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout3:{
                Intent intent1 = new Intent(this,HelpActivity.class);
                this.startActivity(intent1);
                break;
            }
            case R.id.layout4:
                //用户须知
                Intent intent1 = new Intent(this,UserKnowAty.class);
                this.startActivity(intent1);
                break;

            case R.id.layout8:{
                SharePref.setIsautoLoging(this, false);
                exitApp();
                break;
            }
        }

    }
}
