package com.football.net.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;

import com.football.net.R;
import com.football.net.adapter.SearchResultAdapter;
import com.football.net.bean.SearchBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.PlayerBeanListResult;
import com.football.net.http.reponse.impl.TeamBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class SearchResultActivity extends BasicActivity {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    ArrayList<SearchBean> dataList = new ArrayList<SearchBean>();
    SearchResultAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_search_result;
    }

    @Override
    protected void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        adapter = new SearchResultAdapter(this,dataList);
        recyView.setAdapter(adapter);

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });



        String condition = getIntent().getStringExtra("condition");
        loadDataTeam(condition);
        loadUserberData(condition);
    }

    int count;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    if(count>= 2){
                        recyView.setRefreshing(false);
                        if(dataList.size() > 0){
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };

    public void loadDataTeam(String snippet) {
        RequestParam params = new RequestParam();
        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        params.put("teamType", 0);
        params.put("currentPage", 1);
        params.put("orderby", "u.point desc");
        params.put("snippet", snippet);
        params.put("pageSize", 10);
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listTeam", params.toString(), new SmartCallback<TeamBeanResult>() {

            @Override
            public void onSuccess(int statusCode, TeamBeanResult result) {
                dataList.addAll(result.getList());
                count++;
                handler.sendEmptyMessage(100);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(100);
            }

        }, TeamBeanResult.class);
    }

       public void loadUserberData(String snippet) {
        RequestParam params = new RequestParam();
        params.put("teamType", 0);
        params.put("currentPage", 1);
        params.put("pageSize", 10);
        params.put("orderby", "createTime desc");
        params.put("snippet", snippet);
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<PlayerBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, PlayerBeanListResult result) {
                dataList.addAll(result.getList());
                count++;
                handler.sendEmptyMessage(100);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(100);
            }

        }, PlayerBeanListResult.class);
    }



}
