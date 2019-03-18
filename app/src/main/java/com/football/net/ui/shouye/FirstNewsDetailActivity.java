package com.football.net.ui.shouye;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.football.net.R;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.CommentBean;
import com.football.net.bean.NewsBean;
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
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.widget.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/12.
 * 头条详情
 */
public class FirstNewsDetailActivity extends BasicActivity {

    @BindView(R.id.titletv)
    TextView titletv;
    //    @BindView(R.id.contentV)
//    TextView contentV;
    @BindView(R.id.id_webview)
    WebView webview;

    NewsBean bean;

    @BindView(R.id.edittext)
    EditText edittext;
    //    @BindView(R.id.ultimate_recycler_view)
//    UltimateRecyclerView recyclerView;
    @BindView(R.id.id_listv)
    MyListView mListView;
    private CommonAdapter<CommentBean> mAdapter;
    ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();
    //    CommentAdapter adapter;
    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_firstnews_detial;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_firstnews_detial;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        // bm.recycle();
        return resizedBitmap;
    }

    class DetailImageGetter implements Html.ImageGetter {
        private Context context;
        private TextView textView;

        DetailImageGetter(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            final UrlDrawable drawable = new UrlDrawable();
            Glide.with(context)
                    .load(HttpUrlConstant.SERVER_URL + source)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                            drawable.setBitmap(resource);
                            textView.invalidate();
                            textView.setText(textView.getText());
                        }
                    });
            return drawable;
        }

        private class UrlDrawable extends BitmapDrawable {
            private Bitmap bitmap;

            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                if (bitmap != null) {
                    int i = canvas.getWidth() - bitmap.getWidth();
                    canvas.drawBitmap(bitmap, i / 2, 0, getPaint());
                }
            }

            void setBitmap(Bitmap bitmap) {
                this.bitmap = bitmap;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void initView() {

        bean = (NewsBean) getIntent().getSerializableExtra("beandata");
        titletv.setText(bean.getTitle());
//            contentV.setMovementMethod(ScrollingMovementMethod.getInstance());
//            contentV.setText(Html.fromHtml(bean.getContent(),new DetailImageGetter(context,contentV),null));

//        recyclerView.setHasFixedSize(false);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setSmoothScrollbarEnabled(true);
//        manager.setAutoMeasureEnabled(true);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(manager);

        mAdapter = new CommonAdapter<CommentBean>(mContext, dataList, R.layout.item_comment) {
            @Override
            public void convert(ViewHolder helper, final CommentBean item) {
                LinearLayout mLinear = (LinearLayout) helper.getViewById(R.id.lin_root);
                if (helper.getPosition() % 2 == 0) {
                    mLinear.setBackgroundResource(R.mipmap.team_infor_bg2);
                } else {
                    mLinear.setBackgroundResource(R.mipmap.team_infor_bg3);
                }
                helper.setText(R.id.timeV, CommonUtils.getDateStr(item.getOpTime(), "yyyy-MM-dd HH:mm"));
                helper.setText(R.id.username, item.getPlayer().getName());
                TextView mTxtContent = (TextView) helper.getViewById(R.id.comment);
                mTxtContent.setText(Html.fromHtml(item.getComment()));

                if (!StringUtils.isEmpty(item.getPlayer().getIconUrl())) {
                    ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(item.getPlayer().getIconUrl()), (ImageView) helper.getView(R.id.imageview), FootBallApplication.circOptions);
                } else {
                    // ImageLoader.getInstance().displayImage("http://football001.com/web/img/nopic.png", (ImageView) helper.getView(R.id.imageview), FootBallApplication.circOptions);
                    Glide.with(mContext).load(R.mipmap.nopic).crossFade().into((ImageView) helper.getView(R.id.imageview));
                }

                // helper.setRoundImageByUrl(R.id.imageview, HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(item.getPlayer().getIconUrl()), R.mipmap.video_icon, mContext);

                helper.getViewById(R.id.imageview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid", item.getPlayer().getId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid", item.getPlayer().getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        };
        mListView.setAdapter(mAdapter);
//        adapter = new CommentAdapter(this, dataList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                recyclerView.setRefreshing(true);
//            }
//        });

//        recyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
//        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loaddata(page, bean.getId());
//            }
//        });
//        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                loaddata(1, bean.getId());
//            }
//        });

        setWebView(bean.getContent());

        loaddata(1, bean.getId());
    }

    private void setWebView(String content) {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);

        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //注意安卓5.0以上的权限
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // webview.setBackgroundColor(getResources().getColor(R.color.transparent_4d));
       webview.setBackgroundColor(0);
        // webview.getBackground().setAlpha(0);
        webview.getSettings().setTextZoom(100);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setTextSize(WebSettings.TextSize.SMALLER);

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
//        webview.setVerticalScrollBarEnabled(false);
//        webview.setVerticalScrollbarOverlay(false);
//        webview.setHorizontalScrollBarEnabled(false);
//        webview.setHorizontalScrollbarOverlay(false);
//        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webview.setWebChromeClient(new WebChromeClient());

        webview.setWebViewClient(new WebViewClient());

        webview.loadDataWithBaseURL(null, getNewContent(content), "text/html", "UTF-8", null);
    }

    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            // element.attr("width", "100%").attr("height", "auto");
            String temp = element.attr("src");
            System.out.println(temp);
            element.attr("src", HttpUrlConstant.SERVER_URL + element.attr("src"));

            if (element.className() != null && element.className().length() > 0) {
            }
        }
        return doc.toString();
    }

    @OnClick({R.id.commentBtn})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
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
        params.put("type", 3);
        params.put("currentPage", page);
        params.put("pageSize", 100);
        params.put("id", id);

        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listComment", params.toString(), new SmartCallback<CommentBeanResult>() {

            @Override
            public void onSuccess(int statusCode, CommentBeanResult result) {
//                recyclerView.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();
                }
                dataList.addAll(result.getList());
                if (dataList.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                }
//                if (dataList.size() < result.getTotalRecord()) {
//                    recyclerView.reenableLoadmore();
//                } else {
//                    recyclerView.disableLoadmore();
//                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
//                recyclerView.setRefreshing(false);
            }

        }, CommentBeanResult.class);
    }

    public void commit(String comment) {
        showProgress("评论中....");
        RequestParam params = new RequestParam();
        params.put("type", 3);
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
//                recyclerView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        recyclerView.setRefreshing(true);
//                    }
//                });
                loaddata(1, bean.getId());
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("评论失败");
            }

        }, Result.class);
    }
}
