package com.football.net.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.InformAppointmentdapter;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.GameBean;
import com.football.net.bean.RecruitBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnButtonClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class InformAppointmentFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    ListView mListV;
    private CommonAdapter<GameBean> mOrderAdp;

//    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView ultimateRecyclerView;

    //    InformAppointmentdapter adapter;
    ArrayList<GameBean> dataList5 = new ArrayList<GameBean>();
    int page = 1;
    boolean isInit = false;

    @Override
    public int getLayoutId() {
        return R.layout.frg_comm_refresh_listv;
    }

    @Override
    protected void initView() {

        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mOrderAdp = new CommonAdapter<GameBean>(mContext, dataList5, R.layout.item_inform_appointment) {
            @Override
            public void convert(final ViewHolder helper, final GameBean bean) {
                if (helper.getPosition() % 2 == 0) {
                    helper.getViewById(R.id.layout3).setBackgroundResource(R.mipmap.item_bg1);
                } else {
                    helper.getViewById(R.id.layout3).setBackgroundResource(R.mipmap.item_bg2);
                }
                ImageView image1 = (ImageView) helper.getViewById(R.id.image1);
                ImageView image2 = (ImageView) helper.getViewById(R.id.id_img_2);
                ImageView teamType = (ImageView) helper.getViewById(R.id.teamType);
                TextView nameA = (TextView) helper.getViewById(R.id.nameA);
                TextView timeV = (TextView) helper.getViewById(R.id.timeV);
                TextView address = (TextView) helper.getViewById(R.id.address);
                TextView nameB = (TextView) helper.getViewById(R.id.nameB);
                TextView gameStatus = (TextView) helper.getViewById(R.id.gameStatus);
                TextView answerBtn = (TextView) helper.getViewById(R.id.answerBtn);
                TextView rejectBtn = (TextView) helper.getViewById(R.id.rejectBtn);

                if (null != bean.getTeamA()) {
                    String url = CommonUtils.getRurl(bean.getTeamA().getIconUrl());
                    ImageLoader.getInstance().displayImage( url, image1, FootBallApplication.options);
                    image1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                                Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                                intent.putExtra("beanid", bean.getTeamA().getId());
                                mContext.startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                                intent.putExtra("beanid", bean.getTeamA().getId());
                                mContext.startActivity(intent);
                            }
                        }
                    });
                }
                if (null != bean.getTeamB()) {
                    String url2 = CommonUtils.getRurl(bean.getTeamB().getIconUrl());
                    Glide.with(mContext).load(url2).error(R.mipmap.icon_unknow).into(image2);
//                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + url2, image2, FootBallApplication.options);
                    image2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                                Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                                intent.putExtra("beanid", bean.getTeamB().getId());
                                mContext.startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                                intent.putExtra("beanid", bean.getTeamB().getId());
                                mContext.startActivity(intent);
                            }
                        }
                    });
                }
                nameA.setText(bean.getTeamA().getTeamTitle());
                timeV.setText(CommonUtils.getDateStr(bean.getBeginTime(), "yyyy-MM-dd HH:mm"));
                teamType.setImageResource(CommonUtils.getTeamTypeImage(bean.getTeamType()));
                address.setText(bean.getAddress());

                // 显示B队名称
                if (bean.getTeamB() != null) {
                    nameB.setText(bean.getTeamB().getTeamTitle());
                } else {
                    nameB.setText("未知");
                    // ImageLoader.getInstance().displayImage("http://football001.com/web/img/icon_unknow.png", image2, FootBallApplication.options);
                    Glide.with(mContext).load(R.mipmap.icon_unknow).crossFade().into(image2);

                }

                // 判断当前比赛的状态，并展示在TextView gameStatus中
                gameStatus.setText(CommonUtils.getGameStatus(bean));
                gameStatus.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

                answerBtn.setVisibility(View.GONE);
                rejectBtn.setVisibility(View.GONE);

                // 判断同意按钮的出现
                // 如何显示出应战的按钮
                if (FootBallApplication.userbean != null && FootBallApplication.userbean.getTeam() != null && FootBallApplication.userbean.getIsCaptain() == 1) {

                    if (FootBallApplication.userbean.getTeam().getId() != bean.getTeamA().getId()) {

                        // B队是当前登录队长所在球队，方可进行同意和拒绝操作
                        if (bean.getTeamB() != null) {
                            if (bean.getTeamB().getId() == FootBallApplication.userbean.getTeam().getId()
                                    && bean.getTeamBOperation() == null
                                    && bean.getBeginTime() > System.currentTimeMillis()) {

                                answerBtn.setVisibility(View.VISIBLE);
                                answerBtn.setBackgroundResource(R.color.txt_7dd9fd);
                                answerBtn.setText("应战");

                                answerBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        GameBean bean = dataList5.get(helper.getPosition());
                                        commit(bean.getId(), FootBallApplication.userbean.getTeam().getId(), 1);
                                    }
                                });

                                rejectBtn.setVisibility(View.VISIBLE);
                                rejectBtn.setBackgroundResource(R.color.bg_f15661);
                                rejectBtn.setText("拒绝");
                                rejectBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        GameBean bean = dataList5.get(helper.getPosition());
                                        commit(bean.getId(), FootBallApplication.userbean.getTeam().getId(), 2);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        };
        mListV.setAdapter(mOrderAdp);

//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadData5(page);
//            }
//        });
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                loadData5(page);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInit) {
            isInit = true;
            mSwipyRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipyRefreshLayout.setRefreshing(true);
                }
            });
            page = 1;
            loadData5(page);
        }
    }

    public void loadData5(final int page) {
        RequestParam params = new RequestParam();
        params.put("currentPage", page);
//        params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        params.put("pageSize", 12);
        params.put("orderby", "beginTime desc");
        params.put("isEnabled", 1);
        params.put("fromDate", System.currentTimeMillis());
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    dataList5.clear();
                }
                dataList5.addAll(result.getList());
                mOrderAdp.notifyDataSetChanged();
                if (page > 1) {
                    if (result.getList() == null || result.getList().isEmpty())
                        ToastUtil.show(mContext, "没有更多数据");
                }
//                if (dataList5.size() < result.getTotalRecord()) {
//                    mSwipyRefreshLayout.reenableLoadmore();
//                } else {
//                    mSwipyRefreshLayout.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, GameBeanListResult.class);
    }


    //GET http://47.89.46.215/app/respondDare?gameId=83&operation=2 HTTP/1.1
    public void commit(int gameId, int teamId, int operation) {
        showProgress("提交中...");
        SmartParams params = new SmartParams();
        params.put("operation", operation); // 1-应战，2-拒战
        params.put("gameId", gameId);
        params.put("teamBId ", teamId);
        //3.请求数据
        new SmartClient(mContext).get(HttpUrlConstant.APP_SERVER_URL + "respondDare", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("操作成功");
                loadData5(page);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("操作失败");
            }

        }, Result.class);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            loadData5(1);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loadData5(page);
        }
    }

}
