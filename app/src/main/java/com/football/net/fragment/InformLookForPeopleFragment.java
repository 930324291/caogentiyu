package com.football.net.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.MessageOutBean;
import com.football.net.bean.RecruitBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.RecruitBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.InformMemberRecruitDetialActivity;
import com.football.net.ui.shouye.SquareRecuirAdapter;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class InformLookForPeopleFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    ListView mListV;
    private CommonAdapter<RecruitBean> mOrderAdp;

    //    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView ultimateRecyclerView;
//    SquareRecuirAdapter recruitAdapter;
    ArrayList<RecruitBean> recruitList = new ArrayList<RecruitBean>();
    int page = 1;

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
        mOrderAdp = new CommonAdapter<RecruitBean>(mContext, recruitList, R.layout.item_square_recruit) {
            @Override
            public void convert(ViewHolder helper, RecruitBean bean) {
                if (helper.getPosition() % 2 == 0) {
                    helper.getViewById(R.id.rel_root).setBackgroundResource(R.mipmap.item_bg1);
                } else {
                    helper.getViewById(R.id.rel_root).setBackgroundResource(R.mipmap.item_bg2);
                }
                if (!StringUtils.isEmpty(bean.getTeam().getIconUrl())) {
                    ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getTeam().getIconUrl()), (ImageView) helper.getView(R.id.imageview), FootBallApplication.options);
                } else {
                    // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic2.png", imageview, FootBallApplication.options);
                    Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into((ImageView) helper.getView(R.id.imageview));
                }

                helper.setText(R.id.titletv, bean.getTitle());
                helper.setImageResource(R.id.teamType, CommonUtils.getTeamTypeImage(bean.getTeam().getTeamType()));
                helper.setText(R.id.timeview, CommonUtils.getFullTime(bean.getOpTime()));
                helper.setText(R.id.contentV, (bean.getContent() == null ? "" : bean.getContent()));
                String statusStr = "";
                if (bean.getConfirmStatus() == null) {
                    statusStr = "等待球员回复";
                } else if (bean.getConfirmStatus() == 1) {
                    statusStr = "已入队";
                } else {
                    statusStr = "已放弃入队";
                }
                helper.setText(R.id.status, statusStr);
            }
        };
        mListV.setAdapter(mOrderAdp);

        mListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, InformMemberRecruitDetialActivity.class);
                RecruitBean bean = recruitList.get(position);
                intent.putExtra("RecruitBean", bean);
                mContext.startActivity(intent);
            }
        });

        mSwipyRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipyRefreshLayout.setRefreshing(true);
            }
        });
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadRecruit(1,0);
//            }
//        });
//
//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadRecruit(page,0);
//            }
//        });

        loadRecruit(1, 0);
    }

    public void loadRecruit(final int page, int teamType) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("playerId", FootBallApplication.userbean.getId());
//        params.put("orderby", "opTime desc");
//        params.put("isPublic", 1);
//        params.put("teamType", teamType);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listRecruit", params.toString(), new SmartCallback<RecruitBeanResult>() {

            @Override
            public void onSuccess(int statusCode, RecruitBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    recruitList.clear();
                }
                recruitList.addAll(result.getList());
                mOrderAdp.notifyDataSetChanged();
                if (page > 1) {
                    if (result.getList() == null || result.getList().isEmpty())
                        ToastUtil.show(mContext, "没有更多数据");
                }
//                if (videoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, RecruitBeanResult.class);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            loadRecruit(1, 0);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loadRecruit(page, 0);
        }
    }
}
