package com.football.net.fragment;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.NewsBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.shouye.FirstNewsDetailActivity;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class InformFootBallFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {


    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    ListView mListV;
    private CommonAdapter<NewsBean> mOrderAdp;

//    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView ultimateRecyclerView;

    ArrayList<NewsBean> newsList = new ArrayList<NewsBean>();
//    InformEventASignAdapter adapter;

    int page = 1;

    @Override//fragment_football_new_and_his_game
    public int getLayoutId() {
        return R.layout.frg_comm_refresh_listv;
    }

    @Override
    protected void initView() {
        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mOrderAdp = new CommonAdapter<NewsBean>(mContext, newsList, R.layout.item_inform_news) {
            @Override
            public void convert(ViewHolder helper, NewsBean item) {
                if (helper.getPosition() % 2 == 0) {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg1);
                } else {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg2);
                }
                if (!StringUtils.isEmpty(item.getThumbnail())) {
                    // ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getThumbnail()), image1, FootBallApplication.options);
                    Glide.with(getContext()).load(HttpUrlConstant.SERVER_URL + item.getThumbnail()).crossFade().into((ImageView) helper.getViewById(R.id.image1));
                }
                helper.setText(R.id.titleV, item.getTitle());
                helper.setText(R.id.content, Html.fromHtml(item.getContent()) + "");
                helper.setText(R.id.timeV, CommonUtils.getDateStr(item.getStartTime(), "yyyy-MM-dd HH:mm"));
            }
        };
        mListV.setAdapter(mOrderAdp);

        mListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, FirstNewsDetailActivity.class);
                intent.putExtra("beandata", newsList.get(position));
                startActivity(intent);
            }
        });
        mSwipyRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipyRefreshLayout.setRefreshing(true);
            }
        });

//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadNews(page);
//            }
//        });
//
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadNews(1);
//            }
//        });
        loadNews(1);
    }


    public void loadNews(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 6);
        params.put("orderby", "startTime desc");
        params.put("status", 2);
        params.put("condition", " and id>1");

        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listNews", params.toString(), new SmartCallback<NewsBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, NewsBeanListResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    newsList.clear();
                }
                if (result.getList() != null) {
                    newsList.addAll(result.getList());
                }
                mOrderAdp.notifyDataSetChanged();
                if (page > 1) {
                    if (result.getList() == null || result.getList().isEmpty())
                        ToastUtil.show(mContext, "没有更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, NewsBeanListResult.class);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            loadNews(page);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loadNews(page);
        }
    }
}
