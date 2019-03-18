package com.football.net.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.football.net.bean.TeamLikeBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
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
import com.football.net.widget.MyRatingBar;
import com.football.net.widget.NoScrollGridView;
import com.football.net.widget.SpaceItemDecoration;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class TeamDetialActivity extends BaseActivity {

    @BindView(R.id.gridview)
    NoScrollGridView grideview; // 球场上网格视图
    @BindView(R.id.teamImage)
    ImageView teamImage;  // 球队队徽
    @BindView(R.id.id_modify_book_txt)
    TextView mTxtModify;
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

    ArrayList<UserBean> dataList;  // 存储球队成员的集合

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView; // 队长才显示队员列表

    GalleryAdapter2 mAdapter;  // 相册适配器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        ButterKnife.bind(this);

        teamBean = (TeamBean) getIntent().getSerializableExtra("teamBean"); // 获取传入的球队信息

        // 球队详情不再从球队档案中跳转，直接从球队列表跳转，因此没有球队成员传入
        // dataList = (ArrayList<UserBean>) getIntent().getSerializableExtra("dataList"); // 获取传入的成员信息

        add_to_team.setVisibility(View.INVISIBLE);  // 加入到球队的按钮隐藏

        // 显示队徽
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), teamImage, FootBallApplication.options);
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

        if (dataList != null) {
            // peopleNum.setText("成员（"+dataList.size()+")");
        } else {
            loadData(teamBean.getId());   // 成员信息靠获取
        }

        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) {  // 若为队长显示成员列表视图
            mTxtModify.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            int spacingInPixels = UIUtils.dip2px(5);
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        } else {  // 队员
            mTxtModify.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);  // 隐藏成员列表视图
            if (FootBallApplication.userbean.getTeam() == null) {
                activeApply(); // 判断是否显示申请入队按钮
            }
        }

        // 点赞按钮点击事件
        dianzanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamBean.setLikeNum(teamBean.getLikeNum() + 1);
                likeNumV.setText(teamBean.getLikeNum() + "");
                dianzanView.setClickable(false);

                // 将点赞的记录填写到全局teamlikes中
                TeamLikeBean bean = new TeamLikeBean(teamBean,FootBallApplication.userbean,1);
                FootBallApplication.teamLikes.add(bean);

//                if (FootBallApplication.teamLikes.size()>=FootBallApplication.like_team_max) {
//                    showMsg("您一天之内只有"+FootBallApplication.like_team_max+"次给球队点赞的机会！");
//                } else {
//                    boolean flag = false;
//
//                    // 如果用户已占赞，则显示灰色背景并且禁止再点击
//                    if (FootBallApplication.teamLikes!=null) {
//                        List<TeamLikeBean> list = FootBallApplication.teamLikes;
//                        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//                            TeamLikeBean teamLike = (TeamLikeBean) iterator.next();
//                            if (teamLike.getTeam().getId()==teamBean.getId()) { // 如果登录用户球队点赞集合包含当前的球队，是置灰点赞图标
//                                flag = true;
//                                likeNumV.setText((teamBean.getLikeNum()+1) + "");
//                                dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
//                                dianzanView.setClickable(false);
//                                break;
//                            }
//                        }
//                    }
//
//                    if (!flag) {
//                        commit();
//                    }
//                }
            }
        });

        // 如果用户已占赞，则显示灰色背景并且禁止再点击
//        if (FootBallApplication.teamLikes!=null) {
//            List<TeamLikeBean> list = FootBallApplication.teamLikes;
//            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//                TeamLikeBean teamLike = (TeamLikeBean) iterator.next();
//                if (teamLike.getTeam().getId()==teamBean.getId()) { // 如果登录用户球队点赞集合包含当前的球队，是置灰点赞图标
//                    dianzanView.setBackgroundResource(R.drawable.shape_praise_grey_bg);
//                    dianzanView.setClickable(false);
//                    break;
//                }
//            }
//        }

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

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.add_to_team, R.id.id_modify_book_txt})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
        switch (v.getId()) {
            case R.id.id_modify_book_txt:
                //编辑
                Intent intent1 = new Intent(mContext, ModifyPlayBookAty1.class);
                intent1.putExtra("teamId",teamBean.getId());
                startActivity(intent1);
                break;
            case R.id.layout1:
                intent.putExtra("beandata", teamBean);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                break;

            case R.id.layout2:
                intent.putExtra("beandata", teamBean);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                break;

            case R.id.layout3:
                intent.putExtra("beandata", teamBean);
                intent.putExtra("currentIndex", 2);
                startActivity(intent);
                break;

            case R.id.add_to_team:
                showMsg("提交中");
                addtoTeam(teamBean.getId());
                break;
        }
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
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {

            @Override
            public void onSuccess(int statusCode, UserBeanResult result) {
                dataList = new ArrayList<UserBean>();
                dataList.addAll(result.getList());
                MyAdapter adapter = new MyAdapter(TeamDetialActivity.this, dataList);
                grideview.setAdapter(adapter);

                peopleNum.setText("成员（" + dataList.size() + ")"); // 设置成员数量

                //设置适配器
                mAdapter = new GalleryAdapter2(TeamDetialActivity.this, dataList);
                mRecyclerView.setAdapter(mAdapter);
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
                                isContainTeam = true;
                                break;
                            }
                        }
                        if (isContainTeam) {
                            add_to_team.setVisibility(View.INVISIBLE);
                        } else {
                            add_to_team.setVisibility(View.VISIBLE);
                        }
                    } else {
                        add_to_team.setVisibility(View.VISIBLE);
                    }
                } else {
                    add_to_team.setVisibility(View.VISIBLE);
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

}
