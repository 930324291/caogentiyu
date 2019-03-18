package com.football.net.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.CommentAdapter;
import com.football.net.bean.CommentBean;
import com.football.net.bean.SquareVideoBean;
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
import com.football.net.http.reponse.impl.SquareVideo2BeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youku.cloud.module.PlayerErrorInfo;
import com.youku.cloud.player.PlayerListener;
import com.youku.cloud.player.VideoDefinition;
import com.youku.cloud.player.YoukuPlayerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Andy Rao on 2017/1/12.
 */
public class SquareVideoActivity extends BasicActivity {


//    int itemId;
//    int likesNum;

    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.video_play)
    ImageView video_play;
    @BindView(R.id.baseview)
    YoukuPlayerView youkuPlayerView;
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
    SquareVideoBean bean;
    boolean isPhotoChange;
    String vid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_squre_video;
    }

    @Override
    protected void initView() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new CommentAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        bean = (SquareVideoBean) getIntent().getSerializableExtra("SquareVideoBean");
//        String url =  getIntent().getStringExtra("Url");
//        itemId = getIntent().getIntExtra("id",0);


        ImageLoader.getInstance().displayImage(bean.getScreenshot(), ivPic, FootBallApplication.options);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);
            }
        });
        loaddata(1, bean.getId());

        youkuPlayerView.attachActivity(this);
        youkuPlayerView.setPreferVideoDefinition(VideoDefinition.VIDEO_HD);
        youkuPlayerView.setPlayerListener(new MyPlayerListener());
        vid = bean.getVideoDiv();
        if (vid != null) {
            Pattern pattern = Pattern.compile("sid/(.*?)/v.swf");
            Matcher matcher = pattern.matcher(vid);
            if (matcher.find()) {
                vid = matcher.group(1);
            }
        }

        likeNumV.setText(bean.getLikseNum() + "");
        // 如果视频的点赞集合里包含登录用户，则置灰，且不可点击
        String likes = bean.getLikes();
        if(!StringUtils.isEmpty(likes)){
            String[] list = likes.split(",");
            if(Arrays.asList(list).contains(FootBallApplication.userbean.getId()+"")){
                view1.setBackgroundResource(R.drawable.shape_praise_grey_bg);
                view1.setClickable(false);
            }
        }

        // 增加对视频的浏览次数记录
        viewVideo(bean.getId());
    }

    /**
     * 浏览视频
     */
    public void viewVideo(int id) {
        SmartParams params = new SmartParams();
        params.put("playerId", FootBallApplication.userbean.getId());
        params.put("videoId", id);
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "viewVideo", params, new SmartCallback<Result>() {

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

    @OnClick({R.id.view1, R.id.commentBtn, R.id.video_play})
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
            case R.id.video_play:
                video_play.setVisibility(View.GONE);
                youkuPlayerView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams parmms = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
                parmms.addRule(RelativeLayout.BELOW, youkuPlayerView.getId());
                recyclerView.setLayoutParams(parmms);
                youkuPlayerView.playYoukuVideo(vid);
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

    //GET http://47.89.46.215/app/likeVideo?playerId=75&videoId=20 HTTP/1.1
    private void doApprove() {
//        showProgress("点赞中....");
        SmartParams params = new SmartParams();
        params.put("videoId", bean.getId());
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "likeVideo", params, new SmartCallback<SquareVideo2BeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquareVideo2BeanResult result) {
                dismissProgress();
                SquareVideoBean newBean = result.getData();
                if (newBean.getLikseNum() > bean.getLikseNum()) {

                    BaseEvent event = new BaseEvent();
                    event.data = IntentKey.FootGame.KEY_REFRESH_GUANG;
                    event.flag = 13;
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

        }, SquareVideo2BeanResult.class);
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
                edittext.setText("");
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
        if (isPhotoChange) {
        }
        setResult(RESULT_OK);
        finish();
    }

    // 添加播放器的监听器
    private class MyPlayerListener extends PlayerListener {
        @Override
        public void onComplete() {
            // TODO Auto-generated method stub
            super.onComplete();
        }

        @Override
        public void onError(int code, PlayerErrorInfo info) {
            // TODO Auto-generated method stub
//            txt1.setText(info.getDesc());
        }

        @Override
        public void OnCurrentPositionChanged(int msec) {
            // TODO Auto-generated method stub
            super.OnCurrentPositionChanged(msec);
        }

        @Override
        public void onVideoNeedPassword(int code) {
            // TODO Auto-generated method stub
            super.onVideoNeedPassword(code);
        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
            // TODO Auto-generated method stub
            super.onVideoSizeChanged(width, height);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 必须重写的onPause()
        youkuPlayerView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 必须重写的onResume()
        youkuPlayerView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须重写的onDestroy()
        youkuPlayerView.onDestroy();
        BaseEvent event = new BaseEvent();
        event.data = IntentKey.FootGame.KEY_REFRESH_GUANG;
        event.flag = 11;
        EventBus.getDefault().post(event);
    }

}
