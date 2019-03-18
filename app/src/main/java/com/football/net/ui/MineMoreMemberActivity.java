package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.MineMoreMemberAdapter;
import com.football.net.adapter.ScoreTeamListAdapter;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.PlayerBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class MineMoreMemberActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    MineMoreMemberAdapter adapter;
    ArrayList<UserBean> dataList = new ArrayList<UserBean>();

    @BindView(R.id.title)
    TextView title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_football_info_duiyaun;
    }

    protected void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new MineMoreMemberAdapter(dataList);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
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
        params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        params.put("pageResult.pageSize", 1000);
        params.put("orderby", "isCaptain asc,u.createTime desc");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<PlayerBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, PlayerBeanListResult result) {
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

        }, PlayerBeanListResult.class);
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
