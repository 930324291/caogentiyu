package com.football.net.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.PaihangbangMemberAdapter;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class PaiHangBangMemberFragment extends BaseFragment {
    boolean isInited = false;

    @BindView(R.id.id_people_txt)
    TextView mTxtPeople;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    PaihangbangMemberAdapter adapter;
    ArrayList<UserBean> dataList = new ArrayList<UserBean>();

    int page = 1;
    String orderby;
    private int mIndex;

    public static PaiHangBangMemberFragment newInstance(String text, int pos) {
        PaiHangBangMemberFragment fragment = new PaiHangBangMemberFragment();
        Bundle args = new Bundle();
        args.putString("orderby", text);
        args.putInt("index", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaiHangBangMemberFragment newInstance(String text) {
        PaiHangBangMemberFragment fragment = new PaiHangBangMemberFragment();
        Bundle args = new Bundle();
        args.putString("orderby", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_paihangbang_member;
    }

    @Override
    protected void initView() {

        if (getArguments() != null) {
            orderby = getArguments().getString("orderby");
            mIndex = getArguments().getInt("index", 0);
        }

        if (mIndex == 0) {
            mTxtPeople.setText("人气");
        } else if (mIndex == 1) {
            mTxtPeople.setText("身价");
        } else if (mIndex == 2) {
            mTxtPeople.setText("等级");
        }
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);
        adapter = new PaihangbangMemberAdapter(dataList, mIndex);
        adapter.setmOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                    Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                    intent.putExtra("beanid", dataList.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                    intent.putExtra("beanid", dataList.get(position).getId());
                    startActivity(intent);

                }
            }
        });
        recyView.setAdapter(adapter);
        recyView.setLoadMoreView(R.layout.custom_bottom_progressbar);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInited) {
            isInited = true;
            recyView.post(new Runnable() {
                @Override
                public void run() {
                    recyView.setRefreshing(true);
                }
            });
            loadData(page);
        }
    }


    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("teamType", 0);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", orderby);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {

            @Override
            public void onSuccess(int statusCode, UserBeanResult result) {
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

        }, UserBeanResult.class);
    }
}
