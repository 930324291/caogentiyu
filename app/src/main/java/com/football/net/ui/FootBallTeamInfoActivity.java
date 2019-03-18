package com.football.net.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.TeamBean;
import com.football.net.bean.TeamLikeBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.TimeUtils;
import com.football.net.fragment.FootBallTeamMemberFragment;
import com.football.net.fragment.FootBallTeamRecordFragment;
import com.football.net.fragment.FootBallTeamZhanjiFragment;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.TeamBean2Result;
import com.football.net.http.reponse.impl.UserBean3Result;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class FootBallTeamInfoActivity extends BasicActivity {
    @BindView(R.id.rootLayout)
    View rootLayout; // 球队档案线性布局
    @BindView(R.id.iv_duihui)
    ImageView ivDuihui;  // 队徽
    @BindView(R.id.teamType)
    ImageView teamType;  // 几人制
    @BindView(R.id.name)
    TextView name;  // 球队名称
    @BindView(R.id.tv_shenglv)
    TextView tvShenglv;  // 胜率
    @BindView(R.id.levelV)
    TextView levelV;  // 等级
    @BindView(R.id.peopleNum)
    TextView peopleNum;  // 成员
    @BindView(R.id.likesNum)
    TextView likesNum;  // 点赞数量

    @BindView(R.id.progressbar1)
    ProgressBar progressbar1;  // 进度条-胜
    @BindView(R.id.progressbar2)
    ProgressBar progressbar2;  // 进度条-平
    @BindView(R.id.progressbar3)
    ProgressBar progressbar3;  // 进度条-负
    @BindView(R.id.winNum)
    TextView winNum;  // 胜
    @BindView(R.id.drawNum)
    TextView drawNum; // 平
    @BindView(R.id.loseNum)
    TextView loseNum; // 负
    @BindView(R.id.dianzanView)
    View dianzanView; // 点赞视图

    @BindView(R.id.line1)
    View line1; // 球队成员下划线
    @BindView(R.id.line2)
    View line2;  // 球队档案下划线
    @BindView(R.id.line3)
    View line3;  // 战绩下划线

    @BindView(R.id.rel_football_info_content)
    RelativeLayout relFootballInfoContent;  // 内容布局

    ArrayList<BaseFragment> fragmentList;
    int currentIndex = 0;
    private FragmentManager fragmentManager;

    TeamBean teamBean;

    public interface DataLoadListener{
        public void onDataLoadFinished(ArrayList<UserBean> dataList);
    }
    ArrayList<UserBean> dataList = new ArrayList<UserBean>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_football_team_info;
    }  // 球队档案视图

    @Override
    public int getTitleRes() {
        return R.string.title_football_info_dangan;
    }

    @Override
    protected void initView() {
        rootLayout.setVisibility(View.INVISIBLE);
        teamBean = (TeamBean) getIntent().getSerializableExtra("beandata"); // 获取来自FootballTeamFragment中Intent传送的beandata数据
        currentIndex = (Integer) getIntent().getSerializableExtra("currentIndex"); // 获取跳转序号

        fragmentList = new ArrayList<>();
        FootBallTeamMemberFragment memFragment = FootBallTeamMemberFragment.newInstance(teamBean.getId()); // 球队成员模块

        final FootBallTeamRecordFragment recFragment = FootBallTeamRecordFragment.newInstance(teamBean); // 球队基本信息模块

        if (currentIndex==1 || currentIndex ==2) { // 如果由档案或者战绩页签进入，则需要单独取出队长信息
            findPlayersByTeam(true, teamBean.getId());
        }

        memFragment.setDataLoadListener(new DataLoadListener() {
            @Override
            public void onDataLoadFinished(ArrayList<UserBean> list) {
                dataList = list;
                if(dataList != null){
                    peopleNum.setText("成员："+dataList.size());  // 设置成员数量
                    for (UserBean bean: dataList){  // 遍历球队成员集合
                        if(bean.getIsCaptain() ==1){   // 如果为队长，则设置球队基本信息中的队长名称
                            recFragment.setCaptainName(bean.getName());
                            recFragment.setPhone(bean.getMobile());
                            recFragment.setQqNum(bean.getQq());
                            recFragment.setWeixinNum(bean.getWechat());
                            recFragment.setJoinTime(TimeUtils.transferLongToDate("yyyy-MM-dd HH:mm:ss",bean.getCreateTime()));
                            break;
                        }
                    }
                }
            }
        });

        FootBallTeamZhanjiFragment zhanJiFragment = FootBallTeamZhanjiFragment.newInstance(teamBean.getId()); // 球队战绩模块

        fragmentList.add(memFragment);
        fragmentList.add(recFragment);
        fragmentList.add(zhanJiFragment);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragmentList.get(currentIndex).isAdded()) {
            transaction.add(R.id.rel_football_info_content, fragmentList.get(currentIndex));
        }
        transaction.show(fragmentList.get(currentIndex)).commit();
        setCurrentTable(); // 设置当前模块
        getData(); // 获取数据

    }

    /**
     * 获取球队的成员
     */
    public void findPlayersByTeam(Boolean isCaptain, long teamId) {

        SmartParams params = new SmartParams();
        params.put("isCaptain", isCaptain);
        params.put("teamId", teamId);

        //3.请求数据 GET http://47.89.46.215:8181/app/findPlayersByTeam?isCaptain=true&teamId=17 HTTP/1.1
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "findPlayersByTeam", params, new SmartCallback<UserBean3Result>() {

            @Override
            public void onSuccess(int statusCode, UserBean3Result result) {
                if(result.isSuccess()){
                    ArrayList<UserBean> list = result.getData();
                    if (list.size()>0) {
                        UserBean bean = list.get(0);
                        ((FootBallTeamRecordFragment)fragmentList.get(1)).setCaptainName(bean.getName());
                        ((FootBallTeamRecordFragment)fragmentList.get(1)).setPhone(bean.getMobile());
                        ((FootBallTeamRecordFragment)fragmentList.get(1)).setQqNum(bean.getQq());
                        ((FootBallTeamRecordFragment)fragmentList.get(1)).setWeixinNum(bean.getWechat());
                        ((FootBallTeamRecordFragment)fragmentList.get(1)).setJoinTime(TimeUtils.transferLongToDate("yyyy-MM-dd HH:mm:ss",bean.getCreateTime()));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, UserBean3Result.class);
    }

    /**
     * 获取球队信息
     */
    public void getData() {
        SmartParams params = new SmartParams();
        params.put("giverId", FootBallApplication.userbean.getId());
        params.put("teamId", teamBean.getId());

        //3.请求数据 GET请求：http://120.24.89.9:80/app/team/19
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "team/"+ teamBean.getId(), null, new SmartCallback<TeamBean2Result>() {

            @Override
            public void onSuccess(int statusCode, TeamBean2Result result) {
                rootLayout.setVisibility(View.VISIBLE);
                teamBean = result.getData();

                /* 战术板为二级页面，球队档案属于三级页面，禁止死循环跳转
                ivDuihui.setOnClickListener(new View.OnClickListener() {  // 点击队徽进入战术板
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FootBallTeamInfoActivity.this,TeamDetialActivity.class);
                        intent.putExtra("teamBean",teamBean);
                        intent.putExtra("dataList",(Serializable)dataList);
                        startActivity(intent);
                    }
                }); */


                // 获取队徽图片并显示在ivDuihui
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(teamBean.getIconUrl()), ivDuihui, FootBallApplication.options);
                // 根据球队类型显示不同的几人制图片
                teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
                // 设置球队名称
                name.setText(teamBean.getTeamTitle());
                // 计算胜率
                tvShenglv.setText("胜率："+CommonUtils.getGamePercent(teamBean.getWin(),teamBean.getTotal())+"%");
                // 获取等级
                levelV.setText("等级："+teamBean.getKind()+"");
                peopleNum.setText("成员：暂无");

                winNum.setText(teamBean.getWin()+"");
                drawNum.setText(teamBean.getEven()+"");
                loseNum.setText(teamBean.getLost()+"");

                // 设置胜平负进度条显示
                if(teamBean.getTotal() > 0){
                    progressbar1.setProgress(teamBean.getWin()*100/teamBean.getTotal());
                    progressbar2.setProgress(teamBean.getEven()*100/teamBean.getTotal());
                    progressbar3.setProgress(teamBean.getLost()*100/teamBean.getTotal());
                }
                // 设置点赞次数
                likesNum.setText(teamBean.getLikeNum()+"");

                // 点赞按钮点击事件
                dianzanView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMsg("点赞成功");

                        teamBean.setLikeNum(teamBean.getLikeNum()+1);
                        likesNum.setText(teamBean.getLikeNum()+"");

                        dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                        dianzanView.setClickable(false);

                        // 将点赞的记录填写到全局teamlikes中
                        TeamLikeBean bean = new TeamLikeBean(teamBean,FootBallApplication.userbean,1);
                        FootBallApplication.teamLikes.add(bean);

//                        if (FootBallApplication.teamLikes.size()>=FootBallApplication.like_team_max) {
//                            showMsg("您一天之内只有"+FootBallApplication.like_team_max+"次给球队点赞的机会！");
//                        } else {
//                            commit();
//                        }
                    }
                });

                // 如果用户已占赞，则显示灰色背景并且禁止再点击
                if (FootBallApplication.teamLikes!=null) {
                    List<TeamLikeBean> list = FootBallApplication.teamLikes;
                    for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                        TeamLikeBean teamLike = (TeamLikeBean) iterator.next();
                        if (teamLike.getTeam().getId()==teamBean.getId()) { // 如果登录用户球队点赞集合包含当前的球队，是置灰点赞图标
                            dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                            dianzanView.setClickable(false);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                showMsg(message);
            }

        }, TeamBean2Result.class);
    }

    /**
     * 点赞 http://47.89.46.215/app/likeTeam?giverId=75&teamId=25 HTTP/1.1
     */
    public void commit() {
        showProgress("提交中...");
        SmartParams params = new SmartParams();
        params.put("giverId", FootBallApplication.userbean.getId());
        params.put("teamId", teamBean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "likeTeam", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("点赞成功");

                teamBean.setLikeNum(teamBean.getLikeNum()+1);
                likesNum.setText(teamBean.getLikeNum()+"");

                dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                dianzanView.setClickable(false);

                // 将点赞的记录填写到全局teamlikes中
                TeamLikeBean bean = new TeamLikeBean(teamBean,FootBallApplication.userbean,1);
                FootBallApplication.teamLikes.add(bean);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("点赞失败");
            }

        }, Result.class);
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = currentIndex;
        switch (v.getId()) {
            case R.id.layout1:
                index = 0;
                break;
            case R.id.layout2:
                index = 1;
                break;
            case R.id.layout3:
                index = 2;
                break;
        }
        if (currentIndex != index) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragmentList.get(currentIndex));
            if (!fragmentList.get(index).isAdded()) {
                transaction.add(R.id.rel_football_info_content, fragmentList.get(index));
            }
            transaction.show(fragmentList.get(index)).commit();
        }
        currentIndex = index;
        setCurrentTable();
    }

    public void setCurrentTable() {
        line1.setVisibility(currentIndex == 0 ? View.VISIBLE:View.INVISIBLE);
        line2.setVisibility(currentIndex == 1 ? View.VISIBLE:View.INVISIBLE);
        line3.setVisibility(currentIndex == 2 ? View.VISIBLE:View.INVISIBLE);
    }
}
