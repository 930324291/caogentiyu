package com.football.net.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.MineAdapter;
import com.football.net.adapter.MineNewAdapter;
import com.football.net.adapter.PaihangbangMemberAdapter;
import com.football.net.bean.MineBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.MineApplyActivity;
import com.football.net.ui.MineCenterAtivity;
import com.football.net.ui.MineCenterAtivity;
import com.football.net.ui.MineInfoActivity;
import com.football.net.ui.MinePicturectivity;
import com.football.net.ui.MineRecuritActivity;
import com.football.net.ui.MineVideoActivity;
import com.football.net.widget.MyRatingBar;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class MineFragment extends BaseFragment {

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


    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;


    @Override
    protected void initView() {
        textView.setText("我");
        returnBtn.setVisibility(View.GONE);
        UserBean bean = FootBallApplication.userbean;
        name.setText(bean.getName());
        bodyHeight.setText("身高："+(bean.getHeight() == null? "暂无":bean.getHeight()+"CM"));
        tvposition.setText("位置："+CommonUtils.getPositionStr(bean.getPosition()));
        ratingbar.setRating(bean.getOfficial());
        levelV.setText("Lv"+bean.getOfficial());
        bodyWeight.setText("体重："+(bean.getWeight() ==null? "暂无":bean.getWeight()+"KG"));
        if(bean.getTeam() != null){
            teamName.setText("所在球队："+bean.getTeam().getTeamTitle());
        }else{
            teamName.setText("所在球队：暂无");
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getIconUrl()),header,FootBallApplication.circOptions);

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);


        MineNewAdapter adapter = new MineNewAdapter(initData());
        recyView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN){
                    if(position == 0){
                        Intent intent = new Intent(mContext,MinePicturectivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 1){
                        Intent intent = new Intent(mContext,MineVideoActivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 2){
                        Intent intent = new Intent(mContext,MineRecuritActivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 3){
                        Intent intent = new Intent(mContext,MineRecuritActivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 4){
                        Intent intent = new Intent(mContext,MineRecuritActivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 5){
                        Intent intent = new Intent(mContext,MineRecuritActivity.class);
                        mContext.startActivity(intent);
                    }
                }else {
                    if(position == 0){
                        Intent intent = new Intent(mContext,MinePicturectivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 1){
                        Intent intent = new Intent(mContext,MineVideoActivity.class);
                        mContext.startActivity(intent);
                    }else if(position == 2){
                        Intent intent = new Intent(mContext,MineApplyActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            }
        });

    }

    ArrayList initData(){
        ArrayList list = new ArrayList();
        if(FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN){
            HashMap map1 = new HashMap();
            map1.put("imageSrc",R.mipmap.fabu_picture);
            map1.put("name","图片");
            list.add(map1);
            HashMap map2 = new HashMap();
            map2.put("imageSrc",R.mipmap.fabu_video);
            map2.put("name","视频");
            list.add(map2);
            HashMap map3 = new HashMap();
            map3.put("imageSrc",R.mipmap.fabu_zhaoren);
            map3.put("name","招人");
            list.add(map3);
            HashMap map4 = new HashMap();
            map4.put("imageSrc",R.mipmap.fabu_bifen);
            map4.put("name","比分");
            list.add(map4);
            HashMap map5 = new HashMap();
            map5.put("imageSrc",R.mipmap.fabu_signin);
            map5.put("name","签到信");
            list.add(map5);
            HashMap map6 = new HashMap();
            map6.put("imageSrc",R.mipmap.fabu_inomessage);
            map6.put("name","站内信");
            list.add(map6);
        }else{
            HashMap map1 = new HashMap();
            map1.put("imageSrc",R.mipmap.fabu_picture);
            map1.put("name","图片");
            list.add(map1);
            HashMap map2 = new HashMap();
            map2.put("imageSrc",R.mipmap.fabu_video);
            map2.put("name","视频");
            list.add(map2);
            HashMap map3 = new HashMap();
            map3.put("imageSrc",R.mipmap.recruit_icon);
            map3.put("name","找队");
            list.add(map3);
        }
        return list;
    }

    @OnClick({R.id.userCenter,R.id.header})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.userCenter:
                mContext.startActivity(new Intent(mContext,MineCenterAtivity.class));
                break;
            case R.id.header:
                mContext.startActivity(new Intent(mContext,MineInfoActivity.class));
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }
}
