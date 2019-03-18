package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.adapter.ScoreTeamListAdapter;
import com.football.net.bean.ScoreListBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class FabuScoreTeamListActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    ScoreTeamListAdapter adapter;
    ArrayList<ScoreListBean> dataList = new ArrayList<ScoreListBean>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_fabu_score_team_list;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_score_list;
    }

    protected void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new ScoreTeamListAdapter(dataList);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new ScoreTeamListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(FabuScoreTeamListActivity.this,FabuScoreCommitActivity_.class);
                intent.putExtra("ScoreListBean",dataList.get(position));
                startActivityForResult(intent,10);
            }
        });
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
//        RequestParam params = new RequestParam();
        SmartParams params = new SmartParams();
        params.put("teamId", FootBallApplication.userbean.getTeam() == null?"":FootBallApplication.userbean.getTeam().getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "game/toSocreGames", params, new SmartCallback<ScoreListBeanResult>() {

            @Override
            public void onSuccess(int statusCode, ScoreListBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                if(result.getData() != null && !result.getData().isEmpty()) {
                    dataList.addAll(result.getData());
                }
                if(dataList.size() > 0){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, ScoreListBeanResult.class);
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
