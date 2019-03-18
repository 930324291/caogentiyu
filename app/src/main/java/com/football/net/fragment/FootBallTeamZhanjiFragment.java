package com.football.net.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.shouye.FootballGameShouyeZhangjiAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class FootBallTeamZhanjiFragment extends BaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();
    FootballGameShouyeZhangjiAdapter adapter;

    int page = 1;

    public static FootBallTeamZhanjiFragment newInstance(int teamId) {
        Bundle args = new Bundle();
        args.putInt("teamId", teamId);
        FootBallTeamZhanjiFragment fragment = new FootBallTeamZhanjiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_team_member;
    }

    @Override
    protected void initView() {
        final int teamId = (int) getArguments().get("teamId");
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);
        adapter = new FootballGameShouyeZhangjiAdapter(mContext, dataList, 1, teamId);
        recyView.setAdapter(adapter);

        recyView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                page++;
                loadData(page, teamId);
            }
        });
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // page = 1;
                // dealMeathord(currentfirstIndex, currentsecondIndex, true);
            }
        });

        loadData(1, teamId);
    }

    public void loadData(final int page, int teamId) {
        RequestParam params = new RequestParam();
        params.put("teamId", teamId);
        params.put("status", 1);
        params.put("isEnabled", 1);
        params.put("pageSize", 5);
        params.put("currentPage", page);
        params.put("orderby", "beginTime desc");

//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                recyView.setRefreshing(false);
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

        }, GameBeanListResult.class);
    }
}
