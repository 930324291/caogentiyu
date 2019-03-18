package com.football.net.ui.shouye;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class FootBallNewAndHisGameFragment extends BaseFragment {

    public static final String TYPE = "type";

    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView; // 一种重要的布局

    @BindView(R.id.emptyview)
    View emptyview;  // 空布局

    private int type;
    int page = 1;

    ArrayList<GameBean> dataList = new ArrayList<GameBean>();
    ShoyeFootballGameAdapter adapter;

    public static FootBallNewAndHisGameFragment newInstance(Bundle args) {
        FootBallNewAndHisGameFragment fragment = new FootBallNewAndHisGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        type = getArguments().getInt(TYPE);   //0 最新赛事
        return R.layout.fragment_football_new_and_his_game;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);

        View footer = LayoutInflater.from(mContext).inflate(R.layout.refresh_listview_footer, null);  // 定义一个专门显示比赛列表的布局
        adapter = new ShoyeFootballGameAdapter(mContext, dataList, type); // 定义显示比赛列表的适配器
        adapter.setCustomLoadMoreView(footer); // 给该适配器设置布局

        recyView.setAdapter(adapter);  // 用专门设计的刷新视图适配器
        emptyview.setVisibility(View.GONE); // 不可见，且不占据空间

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
                emptyview.setVisibility(View.GONE);
            }
        });

//        loadData(page);
    }

    boolean isInited = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isInited) {
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
        params.put("condition", "and u.teamBOperation =1");
        params.put("currentPage", page);
        params.put("teamType", 0);
        params.put("pageSize", 12);
        if (type == 1) {  //历史赛事
            params.put("gameStatus", 10);
            params.put("orderby", "beginTime desc");
        } else {
            params.put("gameStatus", 5);
            params.put("orderby", "beginTime asc");
        }
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if (dataList.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
                if (dataList.size() < result.getTotalRecord()) {
                    recyView.reenableLoadmore();
                } else {
                    recyView.disableLoadmore();
                }
                if (dataList.size() == 0) {
                    emptyview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, GameBeanListResult.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        String data = event.data;
        if (TextUtils.equals(data, IntentKey.FootGame.KEY_NEWS_GAME)) {
            //跳转最新赛事
            if (null != adapter) {
//                adapter.setmSaveId(CommStatus.MNEWSGAMEID);
                adapter.notifyDataSetChanged();
            }
        } else if (TextUtils.equals(data, IntentKey.FootGame.KEY_HIS_GAME)) {
            //跳转历史赛事
            if (null != adapter) {
//                adapter.setmSaveId(CommStatus.MHISGAMEID);
                adapter.notifyDataSetChanged();
            }
        } else if (TextUtils.equals(data, IntentKey.FootGame.KEY_REFRESH_GAME)) {
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
