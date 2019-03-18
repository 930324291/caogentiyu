package com.football.net.ui.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.TeamBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.TeamDetialActivity;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.football.net.widget.GridSpacingItemDecoration;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/10.
 * 球队
 */
public class FootballTeamFragment extends BaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView; // 内容刷新视图
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

    int page = 1;
    ArrayList<TeamBean> dataList = new ArrayList<TeamBean>();
    FootballTeamAdapter adapter;
    int teamType = 0;

    public static FootballTeamFragment newInstance() {
        Bundle args = new Bundle();
        FootballTeamFragment fragment = new FootballTeamFragment();
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
        line1.setVisibility(View.VISIBLE);

//        recyView.setAdapter(new FootballTeamAdapter(dataList));
//        recyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(mContext, FootBallTeamInfoActivity.class));
//            }
//        });


        recyView.setHasFixedSize(false);

        GridLayoutManager manager = new GridLayoutManager(mContext,4);
        recyView.setLayoutManager(manager); // 设置刷新视图的布局管理器
        recyView.addItemDecoration(new GridSpacingItemDecoration(4, UIUtils.dip2px(10),true));

//        View footer = LayoutInflater.from(mContext).inflate(R.layout.refresh_listview_footer, null);
        adapter = new FootballTeamAdapter(dataList);
//        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);  // 设置刷新视图的适配器
        recyView.setLoadMoreView(R.layout.custom_bottom_progressbar);

        adapter.setOnItemClickLitener(new FootballTeamAdapter.OnItemClickLitener() { // 点击网格视图中每一支球队，进入球队详情
            @Override
            public void onItemClick(View view, int position) {  // 这里打开的是球队档案模块，需要修改为球队战术板
                // Intent intent = new Intent(mContext, FootBallTeamInfoActivity.class);
                // intent.putExtra("beandata",dataList.get(position));
                // startActivity(intent);

                // 参考队员列表点击后跳转
                // Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                // intent.putExtra("beanid",dataList.get(position).getId());
                // startActivity(intent);

                // 参考球员档案如何调出战术板
                // Intent intent = new Intent(mContext,TeamDetialActivity.class);
                // intent.putExtra("teamBean",teamBean);
                // intent.putExtra("dataList",(Serializable)dataList);
                // startActivity(intent);

                // 调出球队战术板
//                Intent intent = new Intent(mContext,TeamDetialActivity.class);
                if (null == dataList.get(position)) {
                    ToastUtil.show(mContext, "没有球队");
                    return;
                }
                Intent intent = new Intent(mContext,TeamDetialActivity2.class);
                intent.putExtra("teamBean",dataList.get(position));
                startActivity(intent);

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
//        recyView.post(new Runnable() {
//            @Override
//            public void run() {
//                recyView.setRefreshing(true);
//            }
//        });
//        loadData(page);
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
            loadData(page);
        }
    }


    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("teamType", teamType);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "u.point desc");
        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listTeam", params.toString(), new SmartCallback<TeamBeanResult>() {

            @Override
            public void onSuccess(int statusCode, TeamBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                adapter.notifyDataSetChanged();
                if(dataList.size() > 0){
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

        }, TeamBeanResult.class);
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
                teamType = 7;
                break;
            case R.id.layout5:
                index = 4;
                teamType = 11;
                break;
        }
        if(currentIndex != index) {
            tvList.get(currentIndex).setVisibility(View.INVISIBLE);
            tvList.get(index).setVisibility(View.VISIBLE);
            currentIndex = index;
            page = 1;
            recyView.post(new Runnable() {
                @Override
                public void run() {
                    recyView.setRefreshing(true);
                }
            });
            loadData(page);
        }
    }

}
