package com.football.net.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.CommentAdapter;
import com.football.net.bean.CommentBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquarePicBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.CommentBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBean2Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MultiImageView;
import com.football.net.widget.StaticObjectUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class PictureActivity1 extends BasicActivity {

    @BindView(R.id.id_circle_pic_list)
    MultiImageView mMultiImg;
    @BindView(R.id.likeNumV)
    TextView likeNumV;
    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;

    @BindView(R.id.view1)
    View view1; // 点赞视图

    ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();
    CommentAdapter adapter;

    SquarePhotoBean bean;

    boolean isPhotoChange;

    @Override
    public int getLayoutId() {
        return R.layout.activity_picture1;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_pic;
    }

    @Override
    protected void initView() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new CommentAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        bean = (SquarePhotoBean) getIntent().getSerializableExtra("SquarePhotoBean");
        mMultiImg.setList(bean.getPics(), 1);
        mMultiImg.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int fileType, String urlVideo) {
                ArrayList<String> mPics = new ArrayList<String>();
                for (SquarePicBean p : bean.getPics()) {
                    String url_pic = HttpUrlConstant.SERVER_URL + CommonUtils.getRurl3(p.getUrl());
                    mPics.add(url_pic);
                }
                StaticObjectUtils.setImageUrls(mPics);
                toDetailPreAct(position);
            }
        });

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);
            }
        });

        likeNumV.setText(bean.getLikseNum() + "");
        // 如果图片的点赞集合里包含登录用户，则置灰，且不可点击
        String likes = bean.getLikes();
        if(!StringUtils.isEmpty(likes)){
            String[] list = likes.split(",");
            if(Arrays.asList(list).contains(FootBallApplication.userbean.getId()+"")){
                view1.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                view1.setClickable(false);
            }
        }

        loaddata(1, bean.getId());

        // 由于PictureActivity1仅显示图片集合，浏览次数应该在这里进行调用
        viewPhotos(bean.getId());
    }

    /**
     * 浏览图片
     */
    public void viewPhotos(int id) {
        SmartParams params = new SmartParams();
        params.put("playerId", FootBallApplication.userbean.getId());
        params.put("photoGroupId", id);
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "viewPhotos", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
            }

        }, Result.class);
    }

    /**
     * 前往图片预览页
     */
    private void toDetailPreAct(int Pos) {
        Intent intent = new Intent(mContext, ImageDetailsActivity.class);
        intent.putExtra("image_position", Pos);
        intent.putExtra(IntentKey.General.KEY_TYPE, 1);
        startActivityForResult(intent, 10);
    }

    @OnClick({R.id.view1, R.id.commentBtn})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.view1:
                doApprove();
                break;
            case R.id.commentBtn:
                if (StringUtils.isEmpty(edittext.getText().toString())) {
                    showMsg("请输入评论");
                    return;
                } else {
                    hideKeyboard();
                    commit(edittext.getText().toString());
                }
                break;
        }
    }

    public void loaddata(final int page, int id) {
        RequestParam params = new RequestParam();
        params.put("type", 1);
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
                if (dataList.size() > 0) {
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

    private void doApprove() {
//        showProgress("点赞中....");
        SmartParams params = new SmartParams();
        params.put("photoGroupId", bean.getId());
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "likePhoto", params, new SmartCallback<SquarePhotoBean2Result>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBean2Result result) {
                dismissProgress();
                SquarePhotoBean newBean = result.getData();
                if (newBean.getLikseNum() > bean.getLikseNum()) {
                    BaseEvent event = new BaseEvent();
                    event.data = IntentKey.FootGame.KEY_REFRESH_GUANG;
                    event.flag = 12;
                    event.zan_Num = newBean.getLikes() + "";
                    EventBus.getDefault().post(event);

                    showMsg("点赞成功");
                    likeNumV.setText(newBean.getLikseNum() + "");
                    bean.setLikes(newBean.getLikes());
                    isPhotoChange = true;

                    view1.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                    view1.setClickable(false);

                } else {
//                    showMsg("已点赞，不能重复点赞");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("点赞失败");
            }

        }, SquarePhotoBean2Result.class);
    }

    public void commit(String comment) {
        showProgress("评论中....");
        RequestParam params = new RequestParam();
        params.put("type", 1);
        params.put("itemId", bean.getId());
        params.put("comment", comment);
        HashMap map = new HashMap();
        map.put("id", FootBallApplication.userbean.getId());
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
                loaddata(1, bean.getId());
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("评论失败");
            }

        }, Result.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        if (isPhotoChange) {
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseEvent event = new BaseEvent();
        event.data = IntentKey.FootGame.KEY_REFRESH_GUANG;
        event.flag = 10;
        EventBus.getDefault().post(event);
    }

}
