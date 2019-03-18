package com.football.net.ui;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.LogUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.NewsBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 用户须知
 */
public class UserKnowAty extends BasicActivity {

    @BindView(R.id.titletv)
    TextView titletv;
    @BindView(R.id.contentV)
    TextView contentV;

    private NewsBean news;

    @Override
    public int getLayoutId() {
        return R.layout.aty_user_know;
    }

    @Override
    public int getTitleRes() {
        return R.string.user_know;
    }


    protected void initView() {
        loadNews();
    }

    /**
     * 加载新闻
     */
    public void loadNews() {
        RequestParam params = new RequestParam();
        // params.put("isEnabled", 1);
        params.put("currentPage", 1);
        params.put("pageSize", 6);
        // params.put("orderby", "startTime desc");
        params.put("status", 2);
        params.put("condition", " and id=1");

        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listNews", params.toString(), new SmartCallback<NewsBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, NewsBeanListResult result) {
                List<NewsBean> lis = result.getList();
                if (null != lis && !lis.isEmpty()) {
                    for (NewsBean bean : lis) {
                        if (bean.getId() == 1) {
                            news = bean;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    titletv.setText(news.getTitle());
                                    contentV.setMovementMethod(ScrollingMovementMethod.getInstance());
                                    contentV.setText(Html.fromHtml(news.getContent()));
                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, NewsBeanListResult.class);
    }

    public void loaddata() {
        RequestParam params = new RequestParam();
        params.put("id", 49);
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "news", params.toString(), new SmartCallback<NewsBean>() {

            @Override
            public void onSuccess(int statusCode, final NewsBean result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        titletv.setText(result.getTitle());
                        contentV.setMovementMethod(ScrollingMovementMethod.getInstance());
                        contentV.setText(Html.fromHtml(result.getContent()));
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, String message) {
                LogUtils.d(message + "[][][]");
            }

        }, NewsBean.class);
    }
}
