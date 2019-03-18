package com.football.net.ui.shouye;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.PaihangbangMemberAdapter;
import com.football.net.bean.TeamMemberBean;
import com.football.net.bean.TransferBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.TeamMemberBeanResult;
import com.football.net.http.reponse.impl.TransferBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/10.
 * 转会
 */
public class ChangeTeamFragment extends BaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.line4)
    View line4;
    @BindView(R.id.line5)
    View line5;


    int currentIndex = 0;
    private ArrayList<View> tvList;
    int teamType =0;

    ChangeTeamAdapter adapter;
    ArrayList<TransferBean> dataList = new ArrayList<TransferBean>();
    int page = 1;
    boolean isInited = false;

    public static ChangeTeamFragment newInstance() {
        Bundle args = new Bundle();
        ChangeTeamFragment fragment = new ChangeTeamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_change_team;
    }

    @Override
    protected void initView() {
        tvList = new ArrayList<>();
        tvList.add(line1);
        tvList.add(line2);
        tvList.add(line3);
        tvList.add(line4);
        tvList.add(line5);
        tvList.get(currentIndex).setVisibility(View.VISIBLE);


        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);
//        View footer = LayoutInflater.from(mContext).inflate(R.layout.refresh_listview_footer, null);
        adapter = new ChangeTeamAdapter(mContext,dataList);
//        adapter.setCustomLoadMoreView(footer);
        recyView.setLoadMoreView(R.layout.custom_bottom_progressbar);
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


    }


    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("teamType", teamType);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "toTime desc");
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listTransfer", params.toString(), new SmartCallback<TransferBeanResult>() {

            @Override
            public void onSuccess(int statusCode, TransferBeanResult result) {
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

        }, TransferBeanResult.class);
    }

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

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = currentIndex;
        switch (v.getId()) {
            case R.id.layout1:
                index = 0;
                teamType =0;
                break;
            case R.id.layout2:
                index = 1;
                teamType =3;
                break;
            case R.id.layout3:
                index = 2;
                teamType =5;
                break;
            case R.id.layout4:
                index = 3;
                teamType =7;
                break;
            case R.id.layout5:
                index = 4;
                teamType =11;
                break;
        }
        if(currentIndex != index) {
            tvList.get(currentIndex).setVisibility(View.INVISIBLE);
            tvList.get(index).setVisibility(View.VISIBLE);
            currentIndex = index;
            page = 1;
           recyView.setRefreshing(true);

            loadData(page);
        }
    }


}
