package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.RecruitBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.RecruitBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.SquareRecuirAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class MineRecurit4NoRoleActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;


    @BindView(R.id.title)
    TextView title;

    SquareRecuirAdapter recruitAdapter;
    ArrayList<RecruitBean>  recruitList = new ArrayList<RecruitBean>();

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_recruit;
    }

    public void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        recruitAdapter = new SquareRecuirAdapter(this, recruitList);
        recyView.setAdapter(recruitAdapter);
        recruitAdapter.setmOnItemClickLitener(new SquareRecuirAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MineRecurit4NoRoleActivity.this, RecruitPeopleDetailActivity.class);
                intent.putExtra("beandata",recruitList.get(position));
                startActivity(intent);
            }
        });
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecruit(1);
            }
        });

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });
        loadRecruit(1);
    }

    public void loadRecruit(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("orderby", "opTime desc");
        params.put("isPublic", 1);
        params.put("teamType", 0);
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listRecruit", params.toString(), new SmartCallback<RecruitBeanResult>() {

            @Override
            public void onSuccess(int statusCode, RecruitBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    recruitList.clear();
                }
                recruitList.addAll(result.getList());
                if(recruitList.size() > 0){
                    recruitAdapter.notifyDataSetChanged();
                }
//                if (videoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, RecruitBeanResult.class);
    }



}
