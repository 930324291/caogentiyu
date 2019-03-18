package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.adapter.SigninMessageAdapter;
import com.football.net.bean.MessageBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.MessageBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class MineSigninMessageActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    SigninMessageAdapter adapter;
    ArrayList<MessageBean>  dataList = new ArrayList<MessageBean>();

    int type = 1; //2签到信  ； 1站内信

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }

    @Override
    public int getTitleRes() {
            return R.string.title_signein_message;
    }

    public void initView() {
        type = getIntent().getIntExtra("type",1);
        if(type == 2){
            setTitle(getResources().getString(R.string.title_signein_message));
        }else if(type == 1){
            setTitle(getResources().getString(R.string.title_inner_message));
        }
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new SigninMessageAdapter(this, dataList);
        if(type == 2){
            adapter.setmOnItemClickLitener(new SigninMessageAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(MineSigninMessageActivity.this,InformSignDetailActivity.class);
                    intent.putExtra("beanId",dataList.get(position).getId());
                    intent.putExtra("ifHideBottomButton",false);
                    startActivity(intent);
                }
            });
        }
        recyView.setAdapter(adapter);
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaddata(1);
            }
        });

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });
        loaddata(1);
    }


    public void loaddata(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 10);
        params.put("type", type);
        params.put("orderby", "opTime desc");
        if(FootBallApplication.userbean.getTeam() != null){
            params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        }
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listMessage", params.toString(), new SmartCallback<MessageBeanResult>() {

            @Override
            public void onSuccess(int statusCode, MessageBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if(dataList.size() > 0){
                    adapter.notifyDataSetChanged();
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

        }, MessageBeanResult.class);
    }



}
