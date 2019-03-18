package com.football.net.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.PlayerLikeBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class PlayerInfoActivity extends BasicActivity {

    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.img_duiwei)
    ImageView img_duiwei;
    @BindView(R.id.teamType)
    ImageView teamType;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.teamName)
    TextView teamName;
    @BindView(R.id.positionInTeam)
    TextView positionInTeam;
    @BindView(R.id.winPercent)
    TextView winPercent;
    @BindView(R.id.likeNumV)
    TextView likeNumV;
    @BindView(R.id.valueView)
    TextView valueView;
    @BindView(R.id.winNum)
    TextView winNum;
    @BindView(R.id.drawNum)
    TextView drawNum;
    @BindView(R.id.loseNum)
    TextView loseNum;
    @BindView(R.id.progressbar1)
    ProgressBar progressbar1;
    @BindView(R.id.progressbar2)
    ProgressBar progressbar2;
    @BindView(R.id.progressbar3)
    ProgressBar progressbar3;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.attendance)
    TextView attendance;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.wortn)
    TextView wortn;
    @BindView(R.id.height)
    TextView height;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.weight)
    TextView weight;
    @BindView(R.id.qq)
    TextView qq;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.wechat)
    TextView wechat;
    @BindView(R.id.place)
    TextView place;
    @BindView(R.id.tv_duiming)
    TextView duiming;
    @BindView(R.id.tv_fromtime)
    TextView fromtime;
    @BindView(R.id.img_duihui)
    ImageView duihui;

    @BindView(R.id.layout_like)
    View layout_like;

    UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_player_info;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_player_info;
    }

    @Override
    protected void initView() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
        if (null == userBean) {
            return;
        }
        TeamBean teamBean = userBean.getTeam();
        if (teamBean != null) {
            teamName.setText(teamBean.getTeamTitle());
            teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));

            winNum.setText(teamBean.getWin() + "");
            drawNum.setText(teamBean.getEven() + "");
            loseNum.setText(teamBean.getLost() + "");

            if (teamBean.getTotal() > 0) {
                progressbar1.setProgress(teamBean.getWin() * 100 / teamBean.getTotal());
                progressbar2.setProgress(teamBean.getEven() * 100 / teamBean.getTotal());
                progressbar3.setProgress(teamBean.getLost() * 100 / teamBean.getTotal());
                if (teamBean.getWin() == 0) {
                    winPercent.setText("胜率：0%");
                } else {
                    BigDecimal bd = new BigDecimal(teamBean.getWin() / teamBean.getTotal() * 100);
                    bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
                    winPercent.setText("胜率：" + bd + "%");
                }
            } else {
                winPercent.setText("胜率：0%");
            }
            if (!StringUtils.isEmpty(teamBean.getIconUrl())) {
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), img_duiwei, FootBallApplication.options);
            }
        }
        userName.setText(userBean.getName());
        valueView.setText("身价：" + userBean.getValue() + "万");
        String postionStr = CommonUtils.getPositionStr(userBean.getPosition());
        if (TextUtils.isEmpty(postionStr)) {
            postionStr = "号码";
        }
        positionInTeam.setText(postionStr);
        likeNumV.setText(userBean.getLikeNum() + "");

        if (!StringUtils.isEmpty(userBean.getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(userBean.getIconUrl()), imageView1, FootBallApplication.options);
        }

//
//        ArrayList<UserInfoBean> list1 = new ArrayList<>();
//        list1.add(new UserInfoBean(R.mipmap.name_icon,userBean.getName()));
//        list1.add(new UserInfoBean(R.mipmap.nickname_icon,TextUtils.isEmpty(userBean.getNickname())?"无昵称": userBean.getNickname()));
//        list1.add(new UserInfoBean(R.mipmap.sex_icon,userBean.getGender() == 1?"男":"女"));
//        list1.add(new UserInfoBean(R.mipmap.date_icon,CommonUtils.getDateStr(userBean.getBirth(),"yyyy-MM-dd")));
//        list1.add(new UserInfoBean(R.mipmap.height_icon,userBean.getHeight() == null?"暂无身高":userBean.getHeight() +"CM"));
//        list1.add(new UserInfoBean(R.mipmap.weight_icon,userBean.getWeight()==null?"暂无体重":userBean.getWeight()+"KG"));
//        list1.add(new UserInfoBean(R.mipmap.number_icon_1,"场上号码:"+(userBean.getUniformNumber() == null?"暂无":userBean.getUniformNumber())));
//        list1.add(new UserInfoBean(R.mipmap.place_icon_1,CommonUtils.getPositionStr(userBean.getPosition())));
//        list1.add(new UserInfoBean(R.mipmap.attendance_icon,"出勤：暂无"));
//        list1.add(new UserInfoBean(R.mipmap.wortn_icon,"$"+userBean.getValue()));
//        list1.add(new UserInfoBean(R.mipmap.phone_icon_1,"电话：暂无"));
//        list1.add(new UserInfoBean(R.mipmap.qq_icon,"QQ：暂无"));
//        list1.add(new UserInfoBean(R.mipmap.wechatz_icon,"微信号：暂无"));

        nickname.setText(TextUtils.isEmpty(userBean.getNickname()) ? "无昵称" : userBean.getNickname());
        attendance.setText(userBean.getAttendTimes());
        sex.setText(userBean.getGender() == 1 ? "男" : "女");
        wortn.setText(userBean.getValue() + "万");
        height.setText(TextUtils.isEmpty(userBean.getHeight()) ? "暂无身高" : userBean.getHeight() + "CM");
        phone.setText("暂无");
        weight.setText(TextUtils.isEmpty(userBean.getWeight()) ? "暂无体重" : userBean.getWeight() + "KG");
        qq.setText("暂无");
        number.setText(userBean.getUniformNumber() == null ? "暂无" : userBean.getUniformNumber());
        wechat.setText("暂无");
        place.setText(CommonUtils.getPositionStr(userBean.getPosition()));

        if (null != teamBean && !StringUtils.isEmpty(teamBean.getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), duihui, FootBallApplication.options);
        }
        if (null != teamBean) {
            duiming.setText(teamBean.getTeamTitle());
            fromtime.setText(CommonUtils.getDateDay(teamBean.getRegistTime()) + "~至今");
        }

        layout_like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // 点赞
                if (FootBallApplication.playerLikes.size()>=FootBallApplication.like_player_max) {
                    showMsg("您一天之内只有"+FootBallApplication.like_player_max+"次给球员点赞的机会！");

                } else {
                    if (null != userBean) {
                        commit();
                    }
                }
            }
        });

        // 如果用户已占赞，则显示灰色背景并且禁止再点击
        if (FootBallApplication.playerLikes!=null) {
            List<PlayerLikeBean> list = FootBallApplication.playerLikes;
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                PlayerLikeBean playerLike = (PlayerLikeBean) iterator.next();
                if (playerLike.getPlayer().getId().equals(userBean.getId())) { // 如果登录用户球员点赞集合包含当前的球员，是置灰点赞图标
                    layout_like.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                    layout_like.setClickable(false);
                    break;
                }
            }
        }

//        ArrayList<UserInfoBean2> list2 = new ArrayList<UserInfoBean2>();
//        TeamBean teamBean1=userBean.getTeam();
//        if(teamBean1 != null){
//            UserInfoBean2 teaminfor = new UserInfoBean2();
//            teaminfor.setTeamName(teamBean1.getTeamTitle());
//            teaminfor.setUrl(CommonUtils.getRurl(teamBean1.getIconUrl()));
//            teaminfor.setFromTime(teamBean1.getRegistTime());
//            list2.add(teaminfor);
//        }
    }

    /**
     * 点赞 http://47.89.46.215/app/likeTeam?giverId=75&teamId=25 HTTP/1.1
     */
    public void commit() {
        showProgress("提交中...");
        SmartParams params = new SmartParams();
        params.put("giverId", FootBallApplication.userbean.getId());
        params.put("playerId", userBean.getId());

        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "likePlayer", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("点赞成功");
                userBean.setLikeNum(userBean.getLikeNum() + 1);
                likeNumV.setText(userBean.getLikeNum() + "");

                layout_like.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                layout_like.setClickable(false);

                // 将点赞的记录填写到全局playerlikes中
                PlayerLikeBean bean = new PlayerLikeBean(userBean,FootBallApplication.userbean,1);
                FootBallApplication.playerLikes.add(bean);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("点赞失败");
            }

        }, Result.class);
    }

}
