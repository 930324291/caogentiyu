package com.football.net.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.CommentAdapter;
import com.football.net.bean.ApplyBean;
import com.football.net.bean.CommentBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
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
 * Created by Andy Rao on 2017/1/12.
 * 找队详情
 */
public class FindTeaamDetailActivity extends BasicActivity {

    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.teamType)
    ImageView teamType;
    @BindView(R.id.titletv)
    TextView titletv;
    @BindView(R.id.contentV)
    TextView contentV;
    @BindView(R.id.timeview)
    TextView timeview;
    ApplyBean bean;

    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.commentBtn)
    Button commentBtn;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;
    ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();
    CommentAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recruit_people;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_find_team_detail;
    }
    @Override
    protected void initView() {
        commentBtn.setText("发送");
        bean = (ApplyBean) getIntent().getSerializableExtra("beandata");
        String url = CommonUtils.getRurl(bean.getPlayer().getIconUrl());
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+url,imageview, FootBallApplication.options);
        titletv.setText(bean.getTitle());
        teamType.setImageResource(CommonUtils.getTeamTypeImage(bean.getDreamType()));
        contentV.setText(bean.getContent());
        timeview.setText(CommonUtils.getDateStr(bean.getApplyTime(), "yyyy-MM-dd HH:mm"));
        contentV.setText(bean.getContent());

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
        loaddata(1,bean.getId());

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
        params.put("type", 5);
        params.put("currentPage", page);
        params.put("pageSize", 12);
//        params.put("orderby", "createTime desc");
        params.put("id", id);
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listComment", params.toString(), new SmartCallback<CommentBeanResult>() {

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
        params.put("type", 5);
        params.put("itemId", bean.getId());
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
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshing(true);
                    }
                });
                loaddata(1,bean.getId());
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("评论失败");
            }

        }, Result.class);
    }
}
