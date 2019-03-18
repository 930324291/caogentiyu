package com.football.net.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.ApplyBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.ApplyBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.SquareApplyAdapter;
import com.football.net.ui.shouye.SquarePhotoAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class MineApplyActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;


    @BindView(R.id.title)
    TextView title;

    SquareApplyAdapter applyAdapter;
    ArrayList<ApplyBean>  applyList = new ArrayList<ApplyBean>();

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_apply;
    }

    public void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        applyAdapter = new SquareApplyAdapter(this, applyList);
        recyView.setAdapter(applyAdapter);
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadApply(1);
            }
        });

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });
        loadApply(1);
    }


//{"playerId":75,"isEnabled":1,"isOpen":1,"condition":" and u.confirmStatus is null "}

    public void loadApply(final int page) {
        RequestParam params = new RequestParam();
        params.put("currentPage", page);
        params.put("isEnabled", 1);
        params.put("orderby", "applyTime desc");
        params.put("playerId", FootBallApplication.userbean.getId());
        params.put("pageSize", 10);
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listApply", params.toString(), new SmartCallback<ApplyBeanResult>() {

            @Override
            public void onSuccess(int statusCode, ApplyBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    applyList.clear();
                }
                applyList.addAll(result.getList());
                if(applyList.size() > 0){
                    applyAdapter.notifyDataSetChanged();
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

        }, ApplyBeanResult.class);
    }


}
