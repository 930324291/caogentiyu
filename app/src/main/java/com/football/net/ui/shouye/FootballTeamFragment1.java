package com.football.net.ui.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.football.net.R;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.TeamBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.TeamBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.ui.TeamDetialActivity2;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/10.
 * 球队
 */
public class FootballTeamFragment1 extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图
    @BindView(R.id.id_order_listv)
    GridView mGridV;
    private CommonAdapter<TeamBean> mOrderAdp;
    ArrayList<TeamBean> dataList = new ArrayList<TeamBean>();

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

    //    FootballTeamAdapter adapter;
    int teamType = 0;

    public static FootballTeamFragment1 newInstance() {
        Bundle args = new Bundle();
        FootballTeamFragment1 fragment = new FootballTeamFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_football_team1;
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

        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);

        mOrderAdp = new CommonAdapter<TeamBean>(mContext, dataList, R.layout.item_football_team) {
            @Override
            public void convert(ViewHolder helper, TeamBean item) {
                helper.setText(R.id.teamName, item.getTeamTitle());
                helper.setImageResource(R.id.teamType, CommonUtils.getTeamTypeImage(item.getTeamType()));
                if (!StringUtils.isEmpty(item.getIconUrl())) {
//                        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(item.getIconUrl()), img, FootBallApplication.options);
                    helper.setRoundImageByUrl(R.id.imageview, HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(item.getIconUrl()), R.mipmap.default_bg, mContext);
                } else {
                    Glide.with(mContext).load(R.mipmap.nopic2).crossFade().into((ImageView) helper.getView(R.id.imageview));
                }
            }
        };
        mGridV.setAdapter(mOrderAdp);
        mGridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 调出球队战术板
                if (null == dataList.get(position)) {
                    ToastUtil.show(mContext, "没有球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", dataList.get(position));
                startActivity(intent);
            }
        });
        loadData(page);
    }

    boolean isInited = false;

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(!isInited){
//            isInited = true;
//            mSwipyRefreshLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    mSwipyRefreshLayout.setRefreshing(true);
//                }
//            });
//            page = 1;
//            loadData(page);
//        }
//    }


    public void loadData(final int page) {
        RequestParam params = new RequestParam();
        params.put("teamType", teamType);
        params.put("currentPage", page);
        params.put("pageSize", 20);
        params.put("orderby", "u.point desc");
        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listTeam", params.toString(), new SmartCallback<TeamBeanResult>() {

            @Override
            public void onSuccess(int statusCode, TeamBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                mOrderAdp.notifyDataSetChanged();
//                if (dataList.size() > 0) {
//                }
                if (dataList.size() < result.getTotalRecord()) {
//                    recyView.reenableLoadmore();
                } else {
//                    recyView.disableLoadmore();
                    ToastUtil.show(mContext, "暂无更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
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
        if (currentIndex != index) {
            tvList.get(currentIndex).setVisibility(View.INVISIBLE);
            tvList.get(index).setVisibility(View.VISIBLE);
            currentIndex = index;
            page = 1;
            mSwipyRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipyRefreshLayout.setRefreshing(true);
                }
            });
            loadData(page);
        }
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
