package com.football.net.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnButtonClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.SquarePhotoAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

public class MinePicturecFragment extends BaseFragment {
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;

    SquarePhotoAdapter photoAdapter;
    ArrayList<SquarePhotoBean>  photoList = new ArrayList<SquarePhotoBean>();

    @Override
    public int getLayoutId() {
        return R.layout.recycleview;
    }


    @Override
    public void initView() {
        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);
        View footer = LayoutInflater.from(mContext).inflate(R.layout.refresh_listview_footer, null);
        photoAdapter = new SquarePhotoAdapter(mContext, photoList);
        photoAdapter.setIfSshowDelete(true);
        photoAdapter.setOnDeleteButtonClick(new OnButtonClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                delete(photoList.get(position).getId(),position);
            }
        });
        recyView.setAdapter(photoAdapter);
        recyView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPhoto(1);
            }
        });

        recyView.post(new Runnable() {
            @Override
            public void run() {
                recyView.setRefreshing(true);
            }
        });
        loadPhoto(1);
    }

    public void delete(final int itemId, final int position) {
        showProgress("删除中....");
        SmartParams params = new SmartParams();
        params.put("ids", itemId);
        //3.请求数据
        new SmartClient(mContext).get(HttpUrlConstant.APP_SERVER_URL + "delPhoto", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if(result.isSuccess()){
                    showMsg("删除成功");
                    photoList.remove(position);
                    photoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("删除失败");
            }

        }, Result.class);
    }
//{"isEnabled":1,"currentPage":1,"pageSize":10,"playerId":75,"status":1,"orderby":"createTime desc"}

    public void loadPhoto(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "createTime desc");
        params.put("status", 1);
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPhoto", params.toString(), new SmartCallback<SquarePhotoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBeanResult result) {
                recyView.setRefreshing(false);
                if (page == 1) {
                    photoList.clear();
                }
                photoList.addAll(result.getList());
                if(photoList.size() > 0){
                    photoAdapter.notifyDataSetChanged();
                }
//                if (photoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
//                } else {
//                    ultimateRecyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                recyView.setRefreshing(false);
            }

        }, SquarePhotoBeanResult.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            recyView.setRefreshing(true);
            recyView.setRefreshing(true);
            loadPhoto(1);
        }
    }

}
