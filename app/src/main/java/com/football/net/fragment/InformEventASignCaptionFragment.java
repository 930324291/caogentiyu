package com.football.net.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.SigninMessageAdapter;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.GameBean;
import com.football.net.bean.MessageBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.MessageBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.InformSignDetailActivity;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class InformEventASignCaptionFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    ListView mListV;
    private CommonAdapter<MessageBean> mOrderAdp;

    //    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView recyView;
//    SigninMessageAdapter adapter;
    ArrayList<MessageBean> dataList = new ArrayList<MessageBean>();
    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.frg_comm_refresh_listv;
    }

    @Override
    protected void initView() {
        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mOrderAdp = new CommonAdapter<MessageBean>(mContext, dataList, R.layout.item_signin_message) {
            @Override
            public void convert(final ViewHolder helper, final MessageBean bean) {
                if (helper.getPosition() % 2 == 0) {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg1);
                } else {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg2);
                }
                helper.setText(R.id.titletv, bean.getTitle());
                helper.setText(R.id.contentV, bean.getContent());
                helper.setText(R.id.timeview, CommonUtils.getDateStr(bean.getOpTime()));
            }
        };
        mListV.setAdapter(mOrderAdp);
        mListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, InformSignDetailActivity.class);
                intent.putExtra("beanId", dataList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
//        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loaddata(1);
//            }
//        });
//
//        recyView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loaddata(page);
//            }
//        });

        mSwipyRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipyRefreshLayout.setRefreshing(true);
            }
        });
        loaddata(1);
    }


    public void loaddata(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 10);
        params.put("type", 2);
        params.put("orderby", "opTime desc");
        if (FootBallApplication.userbean.getTeam() != null) {
            params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        }
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listMessage", params.toString(), new SmartCallback<MessageBeanResult>() {

            @Override
            public void onSuccess(int statusCode, MessageBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                mOrderAdp.notifyDataSetChanged();
                if (page > 1) {
                    if (result.getList() == null || result.getList().isEmpty())
                        ToastUtil.show(mContext, "没有更多数据");
                }
//                if (videoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, MessageBeanResult.class);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            loaddata(1);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loaddata(page);
        }
    }

}
