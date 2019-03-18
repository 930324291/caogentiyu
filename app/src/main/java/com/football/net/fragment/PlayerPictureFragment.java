package com.football.net.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.bean.PlayerLikeBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.TeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.SquarePhotoAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class PlayerPictureFragment extends BaseFragment {

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

    @BindView(R.id.layout_like)
    View layout_like;

    @BindView(R.id.recyclerview)
    UltimateRecyclerView recyclerView;
    ArrayList<SquarePhotoBean> photoList = new ArrayList<SquarePhotoBean>();
    int page = 1;
    SquarePhotoAdapter photoAdapter;

    UserBean userBean;


    @Override
    public void onResume() {
        super.onResume();
        if (null != userBean && null != recyclerView && null != photoAdapter) {
            loadPhoto(1);
        }
    }

    public static PlayerPictureFragment newInstance(UserBean userBean) {
        Bundle args = new Bundle();
        args.putSerializable("userBean", userBean);
        PlayerPictureFragment fragment = new PlayerPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_player_picture;
    }

    @Override
    protected void initView() {
        userBean = (UserBean) getArguments().getSerializable("userBean");
        if (null == userBean) {
            return;
        }
        if (!StringUtils.isEmpty(userBean.getTeam().getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(userBean.getTeam().getIconUrl()), img_duiwei, FootBallApplication.options);
        } else {
            Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into(img_duiwei);
        }

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

        userName.setText(userBean.getName());
        valueView.setText("身价：$" + userBean.getValue() + "");
        positionInTeam.setText(CommonUtils.getPositionStr(userBean.getPosition()));
//        winPercent.setText("胜率：暂无");
        likeNumV.setText(userBean.getLikeNum() + "");
        if (!StringUtils.isEmpty(userBean.getIconUrl())) {
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(userBean.getIconUrl()), imageView1, FootBallApplication.options);
        }
        TeamBean teamBean = userBean.getTeam();
        if (teamBean != null) {
            teamName.setText(teamBean.getTeamTitle());
            teamType.setImageResource(CommonUtils.getTeamTypeImage(teamBean.getTeamType()));
        }

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
//        PlayerPictureAdapter adapter = new PlayerPictureAdapter(this, null);
        photoAdapter = new SquarePhotoAdapter(mContext, photoList);
        recyclerView.setAdapter(photoAdapter);

        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                page++;
                loadPhoto(page);
            }
        });
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                photoList.clear();
                // photoAdapter.notifyDataSetChanged();
                loadPhoto(page);
            }
        });
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);
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

        loadPhoto(1);
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
        new SmartClient(mContext).get(HttpUrlConstant.APP_SERVER_URL + "likePlayer", params, new SmartCallback<Result>() {

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

    public void loadPhoto(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "createTime desc");
        params.put("playerId", userBean.getId());
        params.put("viewType", 1);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPhoto", params.toString(), new SmartCallback<SquarePhotoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBeanResult result) {
                recyclerView.setRefreshing(false);
                if (page == 1) {
                    photoList.clear();
                }
                photoList.addAll(result.getList());

                if (photoList.size() > 0) {
                    photoAdapter.notifyDataSetChanged();
                }

                if (photoList.size() < result.getTotalRecord()) {
                    recyclerView.reenableLoadmore();
                } else {
                    recyclerView.disableLoadmore();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyclerView.setRefreshing(false);
            }

        }, SquarePhotoBeanResult.class);
    }

}
