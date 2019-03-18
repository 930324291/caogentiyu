package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.MineScoreListAdapter;
import com.football.net.adapter.ScoreTeamListAdapter;
import com.football.net.bean.GameBean;
import com.football.net.bean.ScoreListBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import butterknife.BindView;

public class MineScoreListActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    MineScoreListAdapter adapter;
    ArrayList<GameBean> dataList = new ArrayList<GameBean>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine_score;
    }

    protected void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new MineScoreListAdapter(this,dataList);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
//        recyView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadData(page);
//            }
//        });
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });
        loadData(1);
    }




    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled",1);
        params.put("currentPage",page);
        params.put("pageSize",10);
        params.put("teamId", FootBallApplication.userbean.getTeam() == null?"":FootBallApplication.userbean.getTeam().getId());
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listScore", params.toString(), new SmartCallback<GameBeanListResult>() {

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
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, GameBeanListResult.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && requestCode == 20){
            recyView.post(new Runnable() {
                @Override
                public void run() {
                    recyView.setRefreshing(true);
                }
            });
            loadData(1);
        }
    }
}
