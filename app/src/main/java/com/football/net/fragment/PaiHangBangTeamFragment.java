package com.football.net.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.PaihangbangTeamAdapter;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.TeamBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.FootBallTeamInfoActivity;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class PaiHangBangTeamFragment extends BaseFragment {

    @BindView(R.id.id_people_txt)
    TextView mTxtPeople;
    boolean isInited = false;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    PaihangbangTeamAdapter adapter;
    ArrayList<TeamBean> list = new ArrayList<TeamBean>();
    int page = 1;
    String orderby;
    private int mIndex;

    public static PaiHangBangTeamFragment newInstance(String text, int pos) {
        PaiHangBangTeamFragment fragment = new PaiHangBangTeamFragment();
        Bundle args = new Bundle();
        args.putString("orderby", text);
        args.putInt("index", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaiHangBangTeamFragment newInstance(String text) {
        PaiHangBangTeamFragment fragment = new PaiHangBangTeamFragment();
        Bundle args = new Bundle();
        args.putString("orderby", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_paihangbang_team;
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

        adapter = new PaihangbangTeamAdapter(list, mIndex);
        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (null == list.get(position)) {
                    ToastUtil.show(mContext, "没有球队");
                    return;
                }
//                Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
//                intent.putExtra("beandata",list.get(position));
//                mContext.startActivity(intent);
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", list.get(position));
//                mContext.startActivity(intent);
                startActivityForResult(intent, 11);
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

//    //判断Fragment是否可视的重载方法
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//    }


    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("teamType", 0);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", orderby);
        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listTeam", params.toString(), new SmartCallback<TeamBeanResult>() {

            @Override
            public void onSuccess(int statusCode, TeamBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    list.clear();
                }
                list.addAll(result.getList());
                if (list.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
                if (list.size() < result.getTotalRecord()) {
                    recyView.reenableLoadmore();
                } else {
                    recyView.disableLoadmore();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, TeamBeanResult.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            recyView.post(new Runnable() {
                @Override
                public void run() {
                    recyView.setRefreshing(true);
                }
            });
            page = 1;
            loadData(page);
        }
    }
}
