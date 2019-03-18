package com.football.net.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.CommentAdapter;
import com.football.net.bean.CommentBean;
import com.football.net.bean.GameBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.CommentBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 赛事详情
 */
public class GameDetailAty extends BasicActivity {


    @BindView(R.id.imageA)
    ImageView imageA;
    @BindView(R.id.imageB)
    ImageView imageB;

    @BindView(R.id.nameA)
    TextView nameA;
    @BindView(R.id.nameB)
    TextView nameB;

    @BindView(R.id.teamType)
    ImageView mImgType;
    @BindView(R.id.gameName)
    TextView gameName;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tv_bifen1)
    TextView tvBiFen1;
    @BindView(R.id.tv_bifen2)
    TextView tvBiFen2;
    @BindView(R.id.bifenview)
    LinearLayout bifenview;

    //0最新赛事  1历史赛事
    private int mType;
    private GameBean gameBean;

    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();
    CommentAdapter adapter;
    int page =1;

    @Override
    public int getLayoutId() {
        return R.layout.aty_game_detail;
    }

    @Override
    public int getTitleRes() {
        return R.string.game_detail;
    }


    protected void initView() {
        mType = getIntent().getIntExtra(IntentKey.General.KEY_TYPE, -1);
        gameBean = (GameBean) getIntent().getSerializableExtra(IntentKey.General.KEY_MODEL);
        if (mType != -1 && gameBean != null) {
            bindView();
        }
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new CommentAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);
            }
        });
        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                page++;
                loaddata(page,gameBean.getId());
            }
        });
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loaddata(1,gameBean.getId());
            }
        });
        loaddata(1,gameBean.getId());
    }

    private void bindView() {

        if (gameBean.getTeamA() != null) {
            imageA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == gameBean.getTeamA()) {
                        ToastUtil.show(mContext, "没有球队");
                        return;
                    }
                    // 调出球队战术板
//                        Intent intent = new Intent(mContext, TeamDetialActivity.class);
                    Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                    intent.putExtra("teamBean", gameBean.getTeamA());
                    mContext.startActivity(intent);
                }
            });
        }

        if (gameBean.getTeamB() != null) {
            imageB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == gameBean.getTeamB()) {
                        ToastUtil.show(mContext, "没有球队");
                        return;
                    }
                    // 调出球队战术板
//                        Intent intent = new Intent(mContext, TeamDetialActivity.class);
                    Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                    intent.putExtra("teamBean", gameBean.getTeamB());
                    mContext.startActivity(intent);
                }
            });
        }

        nameA.setText(gameBean.getTeamA().getTeamTitle());
        nameB.setText(gameBean.getTeamB() == null ? "未知" : gameBean.getTeamB().getTeamTitle());
        mImgType.setImageResource(CommonUtils.getTeamTypeImage(gameBean.getTeamType()));
        gameName.setText(gameBean.getTitle());
        timeTv.setText(CommonUtils.getDateStr(gameBean.getBeginTime(), "yyyy-MM-dd HH:mm"));
        address.setText(gameBean.getAddress());
        if (mType == 0) {
            tvBiFen1.setText("?");
            tvBiFen2.setText("?");
//            lingdang.setVisibility(View.VISIBLE);
            bifenview.setVisibility(View.GONE);
        } else {
//            lingdang.setVisibility(View.GONE);
            bifenview.setVisibility(View.VISIBLE);
            tvBiFen1.setText(gameBean.getScoreA() == null ? "--" : gameBean.getScoreA() + "");
            tvBiFen2.setText(gameBean.getScoreB() == null ? "--" : gameBean.getScoreB() + "");
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamA().getIconUrl()), imageA, FootBallApplication.options);

        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(gameBean.getTeamB() == null ? "/web/img/icon_unknow.png" : gameBean.getTeamB().getIconUrl()), imageB, FootBallApplication.options);
    }

    @OnClick({R.id.commentBtn})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commentBtn:
                if(StringUtils.isEmpty(edittext.getText().toString())){
                    showMsg("请输入评论");
                    return;
                }else {
                    hideKeyboard();
                    commit(edittext.getText().toString());
                }
                break;
        }
    }

    public void loaddata(final int page,int id) {
        RequestParam params = new RequestParam();
        params.put("type", 4);
        params.put("currentPage", page);
        params.put("id", id);

        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listComment", params.toString(),  new SmartCallback<CommentBeanResult>() {

            @Override
            public void onSuccess(int statusCode, CommentBeanResult result) {
                recyclerView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if(dataList.size() > 0){
                    adapter.notifyDataSetChanged();
                }
                if (dataList.size() < result.getTotalRecord()) {
                    recyclerView.reenableLoadmore();
                } else {
                    recyclerView.disableLoadmore();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyclerView.setRefreshing(false);
            }

        }, CommentBeanResult.class);
    }
    public void commit(String comment) {
        showProgress("评论中....");
        RequestParam params = new RequestParam();
        params.put("type", 4);
        params.put("itemId", gameBean.getId());
        params.put("comment", comment);
        HashMap map = new HashMap();
        map.put("id",FootBallApplication.userbean.getId());
        params.put("player", map);
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "comment/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("评论成功");
                edittext.setText("");
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshing(true);
                    }
                });
                loaddata(1,gameBean.getId());
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("评论失败");
            }

        }, Result.class);
    }
}
