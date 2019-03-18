package com.football.net.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.ActiveApplyBean;
import com.football.net.bean.TankInTeamBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.TeamLikeBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.CommonList;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.LogUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.ActiveApplyBeanResult;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.CustomViews;
import com.football.net.widget.MyRatingBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Andy Rao on 2017/1/15.
 */
public class TeamDetialActivity2 extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.teamImage)
    ImageView teamImage;  // 球队队徽
    // @BindView(R.id.id_modify_book_txt)
    // TextView mTxtModify;
    @BindView(R.id.teamType)
    ImageView teamType; // 球队类型：几人制
    @BindView(R.id.teamName)
    TextView teamName; // 球队名称
    @BindView(R.id.ratinBar)
    MyRatingBar ratinBar;  // 评价条
    @BindView(R.id.levelV)
    TextView levelV;  // 级别

    @BindView(R.id.dianzanView)
    View dianzanView; // 点赞视图

    @BindView(R.id.likeNumV)
    TextView likeNumV; // 点赞数量
    @BindView(R.id.peopleNum)
    TextView peopleNum; // 成员数量
//    @BindView(R.id.captainA)
//    ImageView captainA;

    @BindView(R.id.progressbar1)
    ProgressBar progressbar1; // 胜
    @BindView(R.id.progressbar2)
    ProgressBar progressbar2;  // 平
    @BindView(R.id.progressbar3)
    ProgressBar progressbar3;  // 负

    @BindView(R.id.winNum)
    TextView winNum;  // 胜场次
    @BindView(R.id.drawNum)
    TextView drawNum;  // 平数量
    @BindView(R.id.loseNum)
    TextView loseNum;  // 负数量

    @BindView(R.id.add_to_team)
    ImageView add_to_team; // 添加球员图标

    TeamBean teamBean;  // 球队值对象

//    ArrayList<UserBean> dataList;  // 存储球队成员的集合

    @BindView(R.id.id_look_Custom)
    CustomViews mLookCustv;
    private List<UserBean> mTopPlayer = new ArrayList<>();
    @BindView(R.id.id_banzi_txt)
    TextView mTxtBan;
    @BindView(R.id.id_custview)
    Button mView;
    //是否可以保存
    private boolean isCanSave;
    //true 编辑，false 不可编辑
    private boolean isModify;

    @BindView(R.id.id_look_pos_gridv)
    GridView mGridV; // 战术板下方的替补球员
    private CommonAdapter<UserBean> mPlayerAdp; // 用于显示战术板下方的替补球员适配器
    // 显示下面展示的球员信息
    private List<UserBean> mBottomPlayers = new ArrayList<>();
    //存储全部球员的信息
    private List<UserBean> mAllPlayers = new ArrayList<>();

    private String hintForAddToTeam; // 点击加入到球队按钮时的提示内容


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail1);
        ButterKnife.bind(this);

        CommonList.mSaveCache.clear();
        EventBus.getDefault().register(this);

        teamBean = (TeamBean) getIntent().getSerializableExtra("teamBean"); // 获取传入的球队信息

        if (null == teamBean) {
            return;
        }
        // 球队详情不再从球队档案中跳转，直接从球队列表跳转，因此没有球队成员传入
        // dataList = (ArrayList<UserBean>) getIntent().getSerializableExtra("dataList"); // 获取传入的成员信息

        // 显示队徽
        if (!StringUtils.isEmpty(teamBean.getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), teamImage, FootBallApplication.options);
        }
        // 显示球队名称
        teamName.setText(teamBean.getTeamTitle());
        // 显示球队类型（几人制）
        teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
        // 好评率
        ratinBar.setRating(teamBean.getKind());

        winNum.setText(teamBean.getWin() + ""); // 胜数量
        drawNum.setText(teamBean.getEven() + ""); // 平数量
        loseNum.setText(teamBean.getLost() + ""); // 负数量
        levelV.setText("Lv" + teamBean.getKind()); // 级别

        if (teamBean.getTotal() > 0) {  // 胜负平进度条设置
            progressbar1.setProgress(teamBean.getWin() * 100 / teamBean.getTotal());
            progressbar2.setProgress(teamBean.getEven() * 100 / teamBean.getTotal());
            progressbar3.setProgress(teamBean.getLost() * 100 / teamBean.getTotal());
        }
        likeNumV.setText(teamBean.getLikeNum() + ""); // 点赞数

        // mTxtModify.setVisibility(View.VISIBLE); // 显示战术板按钮

        mGridV.setVisibility(View.VISIBLE); // 显示替补球员列表的网格视图

        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) {  // 若为队长显示成员列表视图
            if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() != null) {
                if (teamBean.getId() == FootBallApplication.userbean.getTeam().getId()) {
                    isCanSave = true;
                }
            }
        }

        initAdapter(); // 初始化适配器

//        if (dataList != null) {
//            // peopleNum.setText("成员（"+dataList.size()+")");
//        } else {

//        }

        // 当进入登录用户所在球队时加入到球队按钮隐藏
//        if (FootBallApplication.userbean.getTeam() != null && teamBean.getId() == FootBallApplication.userbean.getTeam().getId()) {
            add_to_team.setVisibility(View.INVISIBLE);  // 隐藏
//        } else {
//            // 如果是球员才可以显示加入球队按钮
//            if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
//                add_to_team.setVisibility(View.VISIBLE); // 显示
//                hintForAddToTeam = ""; // 提示初始化
//
//                // 获取点击加入到球队按钮时的提示内容
//                activeApply();
//            } else {
//                add_to_team.setVisibility(View.INVISIBLE); // 显示
//            }
//        }


        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) {  // 若为队长显示成员列表视图

//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            mRecyclerView.setLayoutManager(linearLayoutManager);
//            int spacingInPixels = UIUtils.dip2px(5);
//            mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        } else {  // 队员
//            if (FootBallApplication.userbean.getTeam() == null) {
//                activeApply(); // 判断是否显示申请入队按钮
//            }
        }

        // 点赞按钮点击事件
        // setOnClickListener();后会默认设置setClickable=true
        dianzanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FootBallApplication.teamLikes.size() >= FootBallApplication.like_team_max) {
                    showMsg("您一天之内只有" + FootBallApplication.like_team_max + "次给球队点赞的机会！");
                } else {
                    boolean flag = false;

                    // 如果用户已占赞，则显示灰色背景并且禁止再点击
                    if (FootBallApplication.teamLikes != null) {
                        List<TeamLikeBean> list = FootBallApplication.teamLikes;
                        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                            TeamLikeBean teamLike = (TeamLikeBean) iterator.next();
                            if (teamLike.getTeam().getId() == teamBean.getId()) { // 如果登录用户球队点赞集合包含当前的球队，是置灰点赞图标
                                flag = true;
                                likeNumV.setText((teamBean.getLikeNum() + 1) + "");
                                dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                                dianzanView.setClickable(false);
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        commit();
                    }
                }
            }
        });

        // 如果用户已占赞，则显示灰色背景并且禁止再点击
        if (FootBallApplication.teamLikes != null) {
            List<TeamLikeBean> list = FootBallApplication.teamLikes;
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                TeamLikeBean teamLike = (TeamLikeBean) iterator.next();
                if (teamLike.getTeam().getId() == teamBean.getId()) { // 如果登录用户球队点赞集合包含当前的球队，是置灰点赞图标
                    dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                    dianzanView.setClickable(false);
                    break;
                }
            }
        }
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mPlayerAdp = new CommonAdapter<UserBean>(this, mBottomPlayers, R.layout.item_player_pos) {
            @Override
            public void convert(ViewHolder helper, UserBean item) {
                TextView mTxtName = helper.getView(R.id.id_player_name_txt);
                TextView mTxtDel = helper.getView(R.id.id_remove_txt);
                ImageView mImgHead = helper.getView(R.id.id_player_head_img);
                ImageView mImgDel = (ImageView) helper.getViewById(R.id.id_del_player_img);

                String name = item.getName();
                String n = "";
                if (!StringUtils.isEmpty(name)) {
                    if (name.length() <= 3) {
                        n = name;
                    } else {
                        n = name.substring(0, 3);
                    }
                }
                mTxtName.setText(n);
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(item.getIconUrl()), mImgHead, FootBallApplication.circOptions);
                mImgDel.setVisibility(View.GONE);
                mTxtDel.setVisibility(View.GONE);
            }
        };
        mGridV.setAdapter(mPlayerAdp);
        mGridV.setOnItemClickListener(this);
        mLookCustv.setOnMoveViewListener(new CustomViews.moveViewListener() {
            @Override
            public void moveViewLeft(View child, int left, int dx) {
                for (int i = 0; i < mTopPlayer.size(); i++) {
                    if (i == child.getId()) {
                        UserBean bean = mTopPlayer.get(i);
                        bean.setLocationx(left);

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                        params.setMargins(bean.getLocationx(), bean.getLocationy(), 0, 0);
                        child.setLayoutParams(params);
                        break;
                    }
                }
            }

            @Override
            public void moveViewTop(View child, int top, int dy) {
                for (int i = 0; i < mTopPlayer.size(); i++) {
                    if (i == child.getId()) {
                        UserBean bean = mTopPlayer.get(i);
                        bean.setLocationy(top);

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                        params.setMargins(bean.getLocationx(), bean.getLocationy(), 0, 0);
                        child.setLayoutParams(params);
                        break;
                    }
                }
            }
        });
        loadData(teamBean.getId());   // 成员信息靠获取
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isModify) {
            UserBean bean = mBottomPlayers.get(position);
            mTopPlayer.add(bean);
            setTopView();

            mBottomPlayers.remove(position);
            mPlayerAdp.notifyDataSetChanged();
        }
    }

    public void commitFoot() {
        showProgress("提交中...");
        List<RequestParam> mparams = new ArrayList<>();
        for (int i = 0; i < mTopPlayer.size(); i++) {
            UserBean bean = mTopPlayer.get(i);
            RequestParam params = new RequestParam();
            params.put("id", bean.getId());
            params.put("x", bean.getLocationx());
            params.put("y", bean.getLocationy());
            mparams.add(params);
        }
        LogUtils.d(mparams.toString() + "[][][]");

//        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "player/updateCoordinate", mparams.toString(), new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                LogUtils.d(result.toString() + "[][][][][]");
                dismissProgress();
                showMsg("提交成功");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.setVisibility(View.VISIBLE);
                        isModify = false;
                        mTxtBan.setText("战术板");
                    }
                });
//                BaseEvent event = new BaseEvent();
//                event.data = IntentKey.General.KEY_REFRESH;
//                EventBus.getDefault().post(event);
//                finish();
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("提交失败");
            }
        }, Result.class);
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

                teamBean.setLikeNum(teamBean.getLikeNum() + 1);
                likeNumV.setText(teamBean.getLikeNum() + "");

                dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                dianzanView.setClickable(false);

                // 将点赞的记录填写到全局teamlikes中
                TeamLikeBean bean = new TeamLikeBean(teamBean, FootBallApplication.userbean, 1);
                FootBallApplication.teamLikes.add(bean);

//                TeamBean bean = FootBallApplication.userbean.getTeam();
//                bean.setLikeNum(teamBean.getLikeNum() + 1);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("点赞失败");
            }

        }, Result.class);
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.add_to_team, R.id.layout4})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }

        // 如果未审核提示
        if (FootBallApplication.userbean.getAuditStatus() != 1) {
            showMsg("您的帐号目前尚未通过审核！");
            return;
        }

        Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
        switch (v.getId()) {
            // 编辑战术板
            case R.id.layout4:
                if (CommonList.mSaveCache.isEmpty()) {
                    return;
                }
                if (isModify) {
                    if (isCanSave) {
                        //保存
                        if (mTopPlayer.isEmpty()) {
                            ToastUtil.show(mContext, "请编辑球员后在提交");
                            return;
                        }
                        commitFoot();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.setVisibility(View.VISIBLE);
                                isModify = false;
                                mTxtBan.setText("战术板");
                            }
                        });
                    }
                } else {
                    isModify = true;
                    mView.setVisibility(View.GONE);
                    if (isCanSave) {
                        mTxtBan.setText("保存");
                    } else {
                        mTxtBan.setText("取消");
                    }
                }
                break;
            // 成员
            case R.id.layout1:
                if (null == teamBean) {
                    return;
                }
                intent.putExtra("beandata", teamBean);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                break;
            // 档案
            case R.id.layout2:
                if (null == teamBean) {
                    return;
                }
                intent.putExtra("beandata", teamBean);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                break;
            // 战绩
            case R.id.layout3:
                if (null == teamBean) {
                    return;
                }
                intent.putExtra("beandata", teamBean);
                intent.putExtra("currentIndex", 2);
                startActivity(intent);
                break;
            // 加入球队
            case R.id.add_to_team:
                if (null == teamBean) {
                    return;
                }

                if (!StringUtils.isEmpty(hintForAddToTeam)) {
                    showMsg(hintForAddToTeam);
                    return;
                }

                // 如果hintForAddToTeam不为空则提示
                showMsg("提交中");
                addtoTeam(teamBean.getId());
                break;
        }
    }

    /**
     * 显示移动球员的位置信息
     */
    private void setMovePlayers() {
        mTopPlayer.clear();
        mBottomPlayers.clear();

        for (UserBean bean : mAllPlayers) {
            if (bean.getLocationx() >= 1 && bean.getLocationy() >= 1) {
                mTopPlayer.add(bean);
            } else {
                mBottomPlayers.add(bean);
            }
        }
        mPlayerAdp.notifyDataSetChanged();
        setTopView();
    }

    /**
     * 设置头部移动view
     */
    private void setTopView() {
        mLookCustv.removeAllViews();
        if (!mTopPlayer.isEmpty()) {
            for (int i = 0; i < mTopPlayer.size(); i++) {
                UserBean bean = mTopPlayer.get(i);
                setViewPos(bean, i);
            }
        }
    }

    /**
     * 画移动view布局
     *
     * @param bean
     * @param id
     */
    private void setViewPos(final UserBean bean, final int id) {
        View mView = getLayoutInflater().inflate(R.layout.item_player_pos, null);
        mView.setId(id);
        ImageView mHeadImg = (ImageView) mView.findViewById(R.id.id_player_head_img);
        TextView mTxtName = (TextView) mView.findViewById(R.id.id_player_name_txt);
        TextView mTxtDel = (TextView) mView.findViewById(R.id.id_remove_txt);
        ImageView mDelImg = (ImageView) mView.findViewById(R.id.id_del_player_img);
//        mDelImg.setVisibility(View.GONE);
//        mTxtDel.setVisibility(View.GONE);

        String name = bean.getName();
        String n = "";
        if (!StringUtils.isEmpty(name)) {
            if (name.length() <= 5) {
                n = name;
            } else {
                n = name.substring(0, 5);
            }
        }
        mTxtName.setText(n);
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), mHeadImg, FootBallApplication.circOptions);
        mLookCustv.addView(mView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mView.getLayoutParams();
        params.setMargins(bean.getLocationx(), bean.getLocationy(), 0, 0);
        mView.setLayoutParams(params);

        mTxtDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomPlayers.add(bean);
                mPlayerAdp.notifyDataSetChanged();

                mTopPlayer.remove(id);
                setTopView();
            }
        });
        mDelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomPlayers.add(bean);
                mPlayerAdp.notifyDataSetChanged();

                mTopPlayer.remove(id);
                setTopView();
            }
        });
    }

    private void initD1() {
        CommonList.mSaveCache.addAll(mAllPlayers);
        LogUtils.d(mAllPlayers.toString() + "55555555555555");
        setMovePlayers();
    }

    /**
     * 加载球队成员数据
     *
     * @param teamId
     */
    public void loadData(final int teamId) {
        RequestParam params = new RequestParam();
        params.put("teamId", teamId);
        params.put("orderby", "isCaptain asc,u.createTime desc");
        params.put("currentPage", "1");
        params.put("pageSize", "1000");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {

            @Override
            public void onSuccess(int statusCode, UserBeanResult result) {
                if (null != result.getList()) {
                    mAllPlayers.clear();
                    mAllPlayers.addAll(result.getList());
                    peopleNum.setText("成员（" + mAllPlayers.size() + ")"); // 设置成员数量
                    if (!result.getList().isEmpty()) {
                        likeNumV.setText(result.getList().get(0).getTeam().getLikeNum() + ""); // 点赞数
                        teamBean.setLikeNum(result.getList().get(0).getTeam().getLikeNum());
                    } else {
                        likeNumV.setText("0");
                        teamBean.setLikeNum(0);
                    }
                    initD1();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, UserBeanResult.class);
    }

    /**
     * 获取球员当前的 有效的入队申请,同一时间, 球员只有一个有效申请,tanks里面有针对多个球队的申请
     */
    public void activeApply() {
        SmartParams params = new SmartParams();
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "activeApply", params, new SmartCallback<ActiveApplyBeanResult>() {

            @Override
            public void onSuccess(int statusCode, ActiveApplyBeanResult result) {
                ActiveApplyBean data = result.getData();
                if (data != null) {
                    List<TankInTeamBean> tanks = data.getTanks();
                    if (tanks != null) {
                        boolean isContainTeam = false;
                        for (TankInTeamBean bean : tanks) {
                            if (teamBean.getId() == bean.getTeam().getId()) {
                                if (bean.getAuditStatus() == null) {
                                    hintForAddToTeam = "已申请！";
                                } else {
                                    if (bean.getAuditStatus() == 1) {
                                        if (bean.getConfirmStatus() == null) {
                                            hintForAddToTeam = "申请通过待确认！";
                                        } else {
                                            if (bean.getConfirmStatus() == 2) {
                                                hintForAddToTeam = "申请通过已放弃！";
                                            } else if (bean.getConfirmStatus() == 1) {
                                                hintForAddToTeam = "申请通过已确认！";
                                            }
                                        }
                                    } else if (bean.getAuditStatus() == 2) {
                                        hintForAddToTeam = "申请被拒绝！";

                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, ActiveApplyBeanResult.class);
    }

    /**
     * 申请入队
     *
     * @param teamId
     */
    public void addtoTeam(final int teamId) {

        SmartParams params = new SmartParams();
        params.put("teamId", teamId);
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "applyTeam", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if (result.isSuccess()) {
                    showMsg("提交成功");
                    add_to_team.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("提交失败，请重试！");
            }

        }, Result.class);
    }


    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater inflater;

        ArrayList<UserBean> userList = new ArrayList<UserBean>(40);

        private void setTeamMember(ArrayList<UserBean> dataList) {
            if (dataList != null) {
                for (UserBean bean : dataList) {
                    String position = bean.getPosition();
//                    positionMap.put("1","前场");
//                    positionMap.put("2","中场");
//                    positionMap.put("3","后场");
//                    positionMap.put("4","门将");
                    if ("1".equals(position)) {
                        if (userList.get(30) == null) {
                            userList.set(30, bean);
                        } else if (userList.get(32) == null) {
                            userList.set(32, bean);
                        } else if (userList.get(34) == null) {
                            userList.set(34, bean);
                        } else if (userList.get(31) == null) {
                            userList.set(31, bean);
                        } else if (userList.get(33) == null) {
                            userList.set(33, bean);
                        }
                    } else if ("2".equals(position)) {
                        if (userList.get(15) == null) {
                            userList.set(15, bean);
                        } else if (userList.get(17) == null) {
                            userList.set(17, bean);
                        } else if (userList.get(19) == null) {
                            userList.set(19, bean);
                        } else if (userList.get(16) == null) {
                            userList.set(16, bean);
                        } else if (userList.get(18) == null) {
                            userList.set(18, bean);
                        }
                    } else if ("3".equals(position)) {
                        if (userList.get(5) == null) {
                            userList.set(5, bean);
                        } else if (userList.get(7) == null) {
                            userList.set(7, bean);
                        } else if (userList.get(9) == null) {
                            userList.set(9, bean);
                        } else if (userList.get(6) == null) {
                            userList.set(6, bean);
                        } else if (userList.get(8) == null) {
                            userList.set(8, bean);
                        }
                    } else if ("4".equals(position)) {
                        if (userList.get(2) == null) {
                            userList.set(2, bean);
                        }
//                        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(bean.getIconUrl()),captainA, FootBallApplication.circOptions);
                    }
                }
            }
            notifyDataSetChanged();

        }

        public MyAdapter(Context context, ArrayList<UserBean> dataList) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
            for (int i = 0; i < 40; i++) {
                userList.add(null);
            }
            setTeamMember(dataList);
        }

        @Override
        public int getCount() {
            return 40;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_team_detial, null);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview);
                TextView textview = (TextView) convertView.findViewById(R.id.textview);
                LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);

//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UIUtils.dip2px(24), UIUtils.dip2px(24));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                LinearLayout.LayoutParams textLp = (LinearLayout.LayoutParams) textview.getLayoutParams();
                int line = position / 5;
                int row = position % 5;
                int marginglelt = 24 - 3 * line;
                marginglelt = UIUtils.dip2px(marginglelt);
                if (row == 2) {
                    marginglelt = UIUtils.dip2px(16);
                    lp.setMargins(marginglelt, 0, 0, 0);
                    lp.width = UIUtils.dip2px(22);
                    lp.height = UIUtils.dip2px(22);
                    imageView.setLayoutParams(lp);
                    textLp.setMargins(marginglelt, 0, 0, 0);
                    textview.setLayoutParams(textLp);
                } else if (row > 2) {
                    marginglelt = 3 * line;
                    marginglelt = UIUtils.dip2px(marginglelt);
                    lp.setMargins(marginglelt, 0, 0, 0);
                    imageView.setLayoutParams(lp);
                    textLp.setMargins(marginglelt, 0, 0, 0);
                    textview.setLayoutParams(textLp);
                } else if (row < 2) {
                    lp.setMargins(marginglelt, 0, 0, 0);
                    imageView.setLayoutParams(lp);
                    textLp.setMargins(marginglelt, 0, 0, 0);
                    textview.setLayoutParams(textLp);
                }
                if (userList.get(position) != null) {
                    ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(userList.get(position).getIconUrl()), imageView, FootBallApplication.circOptions);
                    textview.setText(userList.get(position).getName());
                } else {
                    layout.setVisibility(View.INVISIBLE);
                }

            }

            return convertView;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        String data = event.data;
        if (TextUtils.equals(data, IntentKey.General.KEY_REFRESH)) {
            //编辑成功，返回保存
            CommonList.mSaveCache.clear();
            loadData(teamBean.getId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
