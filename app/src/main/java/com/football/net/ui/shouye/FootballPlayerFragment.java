package com.football.net.ui.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.PlayerBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.widget.GridSpacingItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/10.
 * 球员
 */
public class FootballPlayerFragment extends BaseFragment {

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

    int page = 1;
    int currentIndex = 0;
    private ArrayList<View> tvList;
    int teamType =0;

    ArrayList<UserBean> dataList = new ArrayList<UserBean>();
    FootballPlayerAdapter adapter;

    public static FootballPlayerFragment newInstance() {
        Bundle args = new Bundle();
        FootballPlayerFragment fragment = new FootballPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_team;
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
        GridLayoutManager manager = new GridLayoutManager(mContext,4);
        recyView.setLayoutManager(manager);
        recyView.addItemDecoration(new GridSpacingItemDecoration(4, UIUtils.dip2px(10), true));

//        View footer = LayoutInflater.from(mContext).inflate(R.layout.refresh_listview_footer, null);
        adapter = new FootballPlayerAdapter(dataList);
//        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);
        recyView.setLoadMoreView(R.layout.custom_bottom_progressbar);

        adapter.setOnItemClickLitener(new FootballPlayerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember){
                    Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                    intent.putExtra("beanid",dataList.get(position).getId());
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                    intent.putExtra("beanid",dataList.get(position).getId());
                    startActivity(intent);

                }
            }
        });
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

//                if(position == 0) {
//                    startActivity(new Intent(mContext, PlayerPictureActivity.class));
//                }else if(position == 1) {
//                    startActivity(new Intent(mContext, PlayerInfoActivity.class));
//                }else if(position == 2) {
//                    startActivity(new Intent(mContext, PlayerZhanJiActivity.class));
//                }
//                startActivity(new Intent(mContext, PlayerDetialActivity.class));
//        recyView.post(new Runnable() {
//            @Override
//            public void run() {
//                recyView.setRefreshing(true);
//            }
//        });
//        loadData(1);
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
            loadData(1);
        }
    }

    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("teamType", teamType);
        params.put("currentPage", page);
        params.put("pageSize", 24);
        params.put("orderby", "createTime desc");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<PlayerBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, PlayerBeanListResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                adapter.notifyDataSetChanged();
//                if(dataList.size() > 0){
//                }
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

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int index = currentIndex;
        switch (v.getId()) {
            case R.id.layout1:
                index = 0;
                teamType = 0;
                break;
            case R.id.layout2:
                index = 1;
                teamType = 3;
                break;
            case R.id.layout3:
                index = 2;
                teamType = 5;
                break;
            case R.id.layout4:
                index = 3;
                teamType =7;
                break;
            case R.id.layout5:
                index = 4;
                teamType = 11;
                break;
        }
        if (currentIndex != index) {
            tvList.get(currentIndex).setVisibility(View.INVISIBLE);
            tvList.get(index).setVisibility(View.VISIBLE);
            currentIndex = index;
            recyView.setRefreshing(true);
            loadData(1);
        }

    }

}
