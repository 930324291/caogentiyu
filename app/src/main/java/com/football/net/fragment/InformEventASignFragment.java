package com.football.net.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.InformMemberMessageAdapter;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.MessageOutBean;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.MessageOutBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.MemberSignDetailActivity;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Andy Rao on 2017/1/10.
 */
public class InformEventASignFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    ListView mListV;
    private CommonAdapter<MessageOutBean> mOrderAdp;

    //    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView ultimateRecyclerView;
    ArrayList<MessageOutBean> dataList = new ArrayList<MessageOutBean>();
    //    InformMemberMessageAdapter adapter;
    String type = "signMessage";

    int page = 1;

    public static InformEventASignFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        InformEventASignFragment fragment = new InformEventASignFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frg_comm_refresh_listv;
    }

    @Override
    protected void initView() {
        type = getArguments().getString("type");
        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mOrderAdp = new CommonAdapter<MessageOutBean>(mContext, dataList, R.layout.item_inform_event_sign) {
            @Override
            public void convert(ViewHolder helper, MessageOutBean item) {
                if (helper.getPosition() % 2 == 0) {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg1);
                } else {
                    helper.getViewById(R.id.layout1).setBackgroundResource(R.mipmap.item_bg2);
                }
                helper.setText(R.id.titleV, item.getMessage().getTitle());
                helper.setText(R.id.content, item.getMessage().getContent());
                helper.setText(R.id.timeV, CommonUtils.getDateStr(item.getMessage().getBeginTime(), "yyyy-MM-dd HH:mm"));
            }
        };
        mListV.setAdapter(mOrderAdp);
        mListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("signMessage".equals(type)) {
                    Intent intent = new Intent(mContext, MemberSignDetailActivity.class);
                    intent.putExtra("MessageOutBean", dataList.get(position));
                    startActivityForResult(intent, 1);
                }
            }
        });
        mSwipyRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipyRefreshLayout.setRefreshing(true);
            }
        });
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData(1);
//            }
//        });
//
//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadData(page);
//            }
//        });

        loadData(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 刷新页面
        if (requestCode == 1 && resultCode == MemberSignDetailActivity.RESULT_CODE) {
            loadData(1);
        }
    }

    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("currentPage", page);
        if ("signMessage".equals(type)) {
            params.put("type", 2);
        } else {
            params.put("type", 1);
        }
        params.put("pageSize", 12);
        params.put("isEnabled", 1);
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listMessageTank", params.toString(), new SmartCallback<MessageOutBeanResult>() {

            @Override
            public void onSuccess(int statusCode, MessageOutBeanResult result) {
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

        }, MessageOutBeanResult.class);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            loadData(page);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loadData(page);
        }
    }

}
