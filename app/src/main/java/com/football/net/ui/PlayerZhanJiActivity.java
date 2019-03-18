package com.football.net.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.GameLogAdapter;
import com.football.net.bean.GameLogBean;
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
import com.football.net.http.reponse.impl.GameLogBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/15.
 */
public class PlayerZhanJiActivity extends BasicActivity {

    @BindView(R.id.recyclerview)
    UltimateRecyclerView recyView;

    @BindView(R.id.img_duiwei)
    ImageView img_duiwei;
    @BindView(R.id.imageView1)
    ImageView imageView1;
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

    @BindView(R.id.layout_like)
    View layout_like;

    ArrayList<GameLogBean> dataList = new ArrayList<GameLogBean>();
    GameLogAdapter adapter;

    int page = 1;
    UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_player_picture;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_player_zhanji;
    }

    @Override
    protected void initView() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
        if (null == userBean) {
            return;
        }
        likeNumV.setText(userBean.getLikeNum() + "");
        valueView.setText("身价：" + userBean.getValue() + "万");

        winNum.setText(userBean.getWin() + "");
        drawNum.setText(userBean.getEven() + "");
        loseNum.setText(userBean.getLost() + "");

        if (userBean.getTotal() > 0) {
            progressbar1.setProgress(userBean.getWin() * 100 / userBean.getTotal());
            progressbar2.setProgress(userBean.getEven() * 100 / userBean.getTotal());
            progressbar3.setProgress(userBean.getLost() * 100 / userBean.getTotal());
            if (userBean.getWin() == 0) {
                winPercent.setText("胜率：0%");
            } else {
                BigDecimal bd = new BigDecimal(userBean.getWin() / userBean.getTotal() * 100);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                winPercent.setText("胜率：" + bd + "%");
            }
        } else {
            winPercent.setText("胜率：0%");
        }

        TeamBean teamBean = userBean.getTeam();
        if (teamBean != null) {
            teamName.setText(teamBean.getTeamTitle());
            teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
        }
        userName.setText(userBean.getName());
        String postionStr = CommonUtils.getPositionStr(userBean.getPosition());
        if (TextUtils.isEmpty(postionStr)) {
            postionStr = "号码";
        }
        positionInTeam.setText(postionStr);

        if (!StringUtils.isEmpty(userBean.getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(userBean.getIconUrl()), imageView1, FootBallApplication.options);
        } else {
            Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(imageView1);
        }
        if (null != teamBean && !StringUtils.isEmpty(teamBean.getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(teamBean.getIconUrl()), img_duiwei, FootBallApplication.options);
        } else {
            Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(img_duiwei);
        }

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new GameLogAdapter(this, dataList, 1);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
        recyView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                page++;
                loadData(page);
            }
        });
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData(page);
            }
        });

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

        loadData(page);

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

    public void loadData(final int page) {
        RequestParam params = new RequestParam();
//        params.put("search.id", userBean.getId());
        params.put("playerId", userBean.getId());
        params.put("pageSize", 12);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listGamelog", params.toString(), new SmartCallback<GameLogBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameLogBeanListResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if (dataList.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
                if (dataList.size() < result.getTotalRecord()) {
                    recyView.reenableLoadmore();
                } else {
                    recyView.disableLoadmore();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, GameLogBeanListResult.class);
    }

    /* 限制在战绩页面点个人信息
    @OnClick(R.id.imageView1)
    public void onClick(View mview) {
        switch (mview.getId()) {
            case R.id.imageView1:
                if (null == userBean) {
                    return;
                }
                Intent intent2 = new Intent(this, PlayerInfoActivity.class);
                intent2.putExtra("userBean", userBean);
                startActivity(intent2);
                break;
        }
    }*/
}
