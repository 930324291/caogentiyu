package com.football.net.ui.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.football.net.R;
import com.football.net.bean.FieldBean;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.FieldBeanResult;
import com.football.net.http.reponse.impl.TeamBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.FootBallGroundDetailActivity;
import com.football.net.ui.FootBallTeamInfoActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andy Rao on 2017/1/10.
 * 球场
 */
public class FootballCourtFragment extends BaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    int page = 1;
    ArrayList<FieldBean> dataList = new ArrayList<FieldBean>();
    FiledAdapter adapter;

    public static FootballCourtFragment newInstance() {
        Bundle args = new Bundle();
        FootballCourtFragment fragment = new FootballCourtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_court;
    }
    @Override
    protected void initView() {

        recyView.setHasFixedSize(false);
        GridLayoutManager manager = new GridLayoutManager(mContext,2);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(mContext).inflate(R.layout.refresh_listview_footer, null);
        adapter = new FiledAdapter(dataList);
        adapter.setCustomLoadMoreView(footer);
        recyView.setAdapter(adapter);

        adapter.setOnItemClickLitener(new FiledAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, FootBallGroundDetailActivity.class);
                intent.putExtra("beandata",dataList.get(position));
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
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "opTime desc");
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listField", params.toString(), new SmartCallback<FieldBeanResult>() {

            @Override
            public void onSuccess(int statusCode, FieldBeanResult result) {
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

        }, FieldBeanResult.class);
    }
}
