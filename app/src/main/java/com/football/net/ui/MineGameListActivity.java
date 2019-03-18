package com.football.net.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.adapter.MineGameListAdapter;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class MineGameListActivity extends BasicActivity {

    public static final String TYPE = "type";

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    int page=1;

    ArrayList<GameBean> dataList = new ArrayList<GameBean>();
    MineGameListAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_new_and_his_game;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine_gamelist;
    }

    @Override
    protected void initView() {

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new MineGameListAdapter(this,dataList);
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

//        loadData(page);
    }

    boolean isInited = false;
    @Override
    public void onResume() {
        super.onResume();
        if(!isInited){
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
        params.put("isEnabled", 1);
        params.put("orderby", "beginTime desc");
        params.put("currentPage", page);
        params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        params.put("pageSize", 10);

//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if(dataList.size() > 0){
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
