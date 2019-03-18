package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.ScoreTeamListAdapter;
import com.football.net.adapter.SelectTeamListAdapter;
import com.football.net.bean.ScoreListBean;
import com.football.net.bean.SimpleTeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartListCallback;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class SelectTeamActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    SelectTeamListAdapter adapter;
    ArrayList<SimpleTeamBean> dataList = new ArrayList<SimpleTeamBean>();

    @BindView(R.id.title)
    TextView title;

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_select_team;
    }

    public void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new SelectTeamListAdapter(dataList);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("SimpleTeamBean",dataList.get(position));
                setResult(RESULT_OK,intent);
                finish();
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
//    GET http://47.89.46.215/app/team/search?exId=17&pageSize=1000&teamType=3 HTTP/1.1
        SmartParams params = new SmartParams();
        params.put("pageSize",30);
        if(FootBallApplication.userbean.getTeam() != null){

            params.put("teamType",FootBallApplication.userbean.getTeam().getTeamType());
        }
        //3.请求数据
        new SmartClient(this).getList(HttpUrlConstant.APP_SERVER_URL + "team/search", params, new SmartListCallback<SimpleTeamBean>() {

            @Override
            public void onSuccess(int statusCode, ArrayList<SimpleTeamBean> result) {
                recyView.setRefreshing(false);
                if(result !=null && result.size()>0){
                    dataList.clear();
                    dataList.addAll(result);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        },SimpleTeamBean.class);
    }


}
