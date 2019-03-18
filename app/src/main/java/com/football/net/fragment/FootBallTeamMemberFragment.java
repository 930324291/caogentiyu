package com.football.net.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.football.net.R;
import com.football.net.adapter.FootballTeamMemberAdapter;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.FootBallTeamInfoActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class FootBallTeamMemberFragment extends BaseFragment {

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView; // 球队成员刷新视图
    FootballTeamMemberAdapter adapter; // 球队成员适配器
    ArrayList<UserBean> dataList = new ArrayList<UserBean>(); // 球员集合

    FootBallTeamInfoActivity.DataLoadListener dataLoadListener;

    public void setDataLoadListener(FootBallTeamInfoActivity.DataLoadListener dataLoadListener) {
        this.dataLoadListener = dataLoadListener;
    }

    public static FootBallTeamMemberFragment newInstance(int teamId) {
        Bundle args = new Bundle();
        args.putInt("teamId",teamId);
        FootBallTeamMemberFragment fragment = new FootBallTeamMemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_team_member;
    }  // 球队成员布局

    @Override
    protected void initView() {
        int teamId = (int) getArguments().get("teamId");
        ultimateRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        ultimateRecyclerView.setLayoutManager(manager);
        adapter = new FootballTeamMemberAdapter(mContext, dataList);
        ultimateRecyclerView.setAdapter(adapter);
        loadData(teamId);
    }

    public void loadData(final int teamId) {
        RequestParam params = new RequestParam();
        params.put("teamId", teamId);
        params.put("orderby", "isCaptain asc,u.createTime desc");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {

            @Override
            public void onSuccess(int statusCode, UserBeanResult result) {
                ultimateRecyclerView.setRefreshing(false);
                dataList.addAll(result.getList());
                if(dataList.size() > 0){
                    adapter.notifyDataSetChanged();
                }
                if (dataList.size() < result.getTotalRecord()) {
                    ultimateRecyclerView.reenableLoadmore();
                } else {
                    ultimateRecyclerView.disableLoadmore();
                }
                if(dataLoadListener != null){
                    dataLoadListener.onDataLoadFinished(dataList);
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                ultimateRecyclerView.setRefreshing(false);
            }

        }, UserBeanResult.class);
    }

}
