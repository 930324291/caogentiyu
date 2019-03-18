package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.football.net.R;
import com.football.net.adapter.NewsAdapter;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.NewsBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.ui.shouye.FirstNewsDetailActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 新闻详情
 */
public class NewsActivity extends BasicActivity {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    NewsAdapter adapter;
    ArrayList<NewsBean> dataList = new ArrayList<NewsBean>();

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_news;
    }

    public void initView() {
        setTitle(getResources().getString(R.string.title_news)); // 设置标题为头条新闻

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(this).inflate(R.layout.refresh_listview_footer, null);
        adapter = new NewsAdapter(this, dataList);

        adapter.setmOnItemClickLitener(new NewsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(NewsActivity.this, InformSignDetailActivity.class);
//                intent.putExtra("beanId", dataList.get(position).getId());
//                intent.putExtra("ifHideBottomButton", true);
//                startActivity(intent);
                Intent intent = new Intent(mContext, FirstNewsDetailActivity.class);
                intent.putExtra("beandata", dataList.get(position));
                startActivity(intent);
            }
        });

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
        params.put("orderby", "startTime desc");
        params.put("status", 2);
        params.put("condition", " and id>1");

        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listNews", params.toString(), new SmartCallback<NewsBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, NewsBeanListResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if (dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        NewsBean bean = dataList.get(i);
                        if (bean.getTitle().contains("用户须知")) {
                            if (dataList.size() == 1) {
                                dataList.clear();
                            } else {
                                dataList.remove(i);
                            }
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, NewsBeanListResult.class);
    }
}
