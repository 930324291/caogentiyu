package com.football.net.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBean2Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.shouye.FirstNewsFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Andy Rao on 2017/1/12.
 */
public class PictureActivity extends BasicActivity {

    @BindView(R.id.iv_pic)
    ViewPager ivPic;
    @BindView(R.id.likeNumV)
    TextView likeNumV;
    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyclerView;

    ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();
    CommentAdapter adapter;

    SquarePhotoBean bean;

    boolean isPhotoChange;

    @Override
    public int getLayoutId() {
        return R.layout.activity_picture;
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
        ImageAdapter imageAdapter = new ImageAdapter(bean.getPics());
        ivPic.setAdapter(imageAdapter);
        likeNumV.setText(bean.getLikseNum() + "");

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);
            }
        });
        loaddata(1, bean.getId());
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
                    showMsg("点赞成功");
                    likeNumV.setText(newBean.getLikseNum() + "");
                    bean.setLikes(newBean.getLikes());
                    isPhotoChange = true;
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

    private class ImageAdapter extends PagerAdapter {
        ArrayList<SquarePicBean> vpImges;

        public ImageAdapter(ArrayList<SquarePicBean> vpImges) {
            this.vpImges = vpImges;
        }

        @Override
        public int getCount() {
            return vpImges.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
   /*         ImageView imageView = new ImageView(PictureActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final SquarePicBean bean = vpImges.get(position);
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getUrl()), imageView, FootBallApplication.options);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PictureShowActivity.class);
                    intent.putExtra("imgeurl", CommonUtils.getRurl(bean.getUrl()));
                    startActivity(intent);
                }
            });
            return imageView;*/
            final SquarePicBean bean = vpImges.get(position);
            View contentview = LayoutInflater.from(PictureActivity.this).inflate(R.layout.activity_picture_show, null);
            final ImageView iv = (ImageView) contentview.findViewById(R.id.imageview);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(iv);
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getUrl()), iv, FootBallApplication.options,//ImageLoader.getInstance().displayImage使用方法
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            displayImage(loadedImage,iv);
                            attacher.update();
                            //在加载完图片的时候更新一下attacher
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                        }
                    });
            container.addView(contentview);
            return contentview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void displayImage(Bitmap bitmap, ImageView iv) {
            //想让图片宽是屏幕的宽度
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.yangyang);
            //测量
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//只测量
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            //再拿到屏幕的宽
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            //计算如果让照片是屏幕的宽，选要乘以多少？
            double scale = screenWidth * 1.0 / width;
            //这个时候。只需让图片的宽是屏幕的宽，高乘以比例
            int displayHeight = (int) (height * scale);//要显示的高，这样避免失真V
            //最终让图片按照宽是屏幕 高是等比例缩放的大小
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, displayHeight);
            iv.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPhotoChange) {
            setResult(RESULT_OK);
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
