package com.football.net.ui.shouye;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.football.net.R;
import com.football.net.bean.AdsPhotoBean;
import com.football.net.bean.GameBean;
import com.football.net.bean.NewsBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.CommStatus;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.AdsPhotoBeanListResult;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.NewsBeanListResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.ActivityDetailActivity;
import com.football.net.ui.FabuMatchActivity;
import com.football.net.ui.NewsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 头条
 */
public class FirstNewsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.lin_points)
    LinearLayout linPoints;  // banner右下角的小球
    @BindView(R.id.firstnews_viewpager)
    ViewPager firstnewsViewpager; // banner
    @BindView(R.id.firstnews_new_game)
    ListView firstnewsNewGame; // 最新赛事
    @BindView(R.id.firstnews_his_game)
    ListView firstnewsHisGame; // 历史赛事
    @BindView(R.id.firstnews_news)
    ListView firstnewsNews; // 新闻列表
    /*    @BindView(R.id.firstnew_gridview)
        GridView firstnewGridview;*/
    @BindView(R.id.appointImage)
    ImageView appointImage; // 约战

    int currentPosition = 0;
    //    int[] vpImges = new int[]{R.mipmap.news1, R.mipmap.news2, R.mipmap.news3, R.mipmap.news4};
    ArrayList<AdsPhotoBean> vpImges = new ArrayList<AdsPhotoBean>(4);
    ArrayList<AdsPhotoBean> tmpvpImges = new ArrayList<AdsPhotoBean>(4);
    ImageAdapter imageAdapter;

    FirstGameAdapter newGameAdapter;
    ArrayList<GameBean> newGameList = new ArrayList<GameBean>();
    ArrayList<GameBean> tmpnewGameList = new ArrayList<GameBean>();
    FirstGameAdapter hisGameAdapter;
    ArrayList<GameBean> hisGameList = new ArrayList<GameBean>();
    ArrayList<GameBean> tmphisGameList = new ArrayList<GameBean>();

    FirstNewsAdapter firstNewsAdapter;
    ArrayList<NewsBean> newsList = new ArrayList<NewsBean>();
    ArrayList<NewsBean> tmpnewsList = new ArrayList<NewsBean>();

/*    FirstNewsGroundAdapter fiedAdapter;
    ArrayList<FieldBean> fieldList = new ArrayList<FieldBean>();
    ArrayList<FieldBean> tmpfieldList = new ArrayList<FieldBean>();*/

    int loadsize = 0;

    public static FirstNewsFragment newInstance() {
        Bundle args = new Bundle();
        FirstNewsFragment fragment = new FirstNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homepage_firstnew;
    }

    @Override
    protected void initView() {
        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) {
            appointImage.setVisibility(View.VISIBLE); // 只有是队长时，方可进行约战
        } else {
            appointImage.setVisibility(View.GONE); // 当为队员时，约战图标隐藏且不占据空间
        }
        initViewPager(); // 初始化Banner视图
        initGameListView();  // 初始化赛事视图
        initHeadline();  // 初始化新闻列表
//        initField();
        loadPhoto();  // 加载Banner图
        loadGame(0);  // 加载最新赛事
        loadGame(1);  // 加载历史赛事
        loadNews();  // 加载新闻
//        loadField();
    }

    @OnClick({R.id.appointImage, R.id.three_football, R.id.id_news_game_linear, R.id.id_his_game_linear, R.id.id_click_news_linear})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.appointImage:
                Intent intent = new Intent(mContext, FabuMatchActivity.class);
                mContext.startActivity(intent);
                hideKeyboard();
                break;
            case R.id.three_football:
            case R.id.id_click_news_linear:
                Intent intent2 = new Intent(mContext, NewsActivity.class);
                mContext.startActivity(intent2);
                hideKeyboard();
                break;
            case R.id.id_news_game_linear:
                CommStatus.MNEWSGAMEID = -1;
                CommStatus.JUMP_GAME = 0;
                BaseEvent news = new BaseEvent();
                news.data = IntentKey.FootGame.KEY_NEWS_GAME;
                EventBus.getDefault().post(news);
                break;
            case R.id.id_his_game_linear:
                CommStatus.MHISGAMEID = -1;
                CommStatus.JUMP_GAME = 1;
                BaseEvent his = new BaseEvent();
                his.data = IntentKey.FootGame.KEY_HIS_GAME;
                EventBus.getDefault().post(his);
                break;
        }
    }

    /**
     * 初始化赛事数据
     */
    private void initGameListView() {
        newGameAdapter = new FirstGameAdapter(getActivity(), 0, newGameList);
        firstnewsNewGame.setAdapter(newGameAdapter);
        hisGameAdapter = new FirstGameAdapter(getActivity(), 1, hisGameList);
        firstnewsHisGame.setAdapter(hisGameAdapter);
        firstnewsNewGame.setOnItemClickListener(this);
        firstnewsHisGame.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.firstnews_new_game:
                //最新赛事
                if (newGameList.isEmpty()) {
                    return;
                }
//                CommStatus.MNEWSGAMEID = newGameList.get(position).getId();
//                CommStatus.JUMP_GAME = 0;
//                BaseEvent news = new BaseEvent();
//                news.data = IntentKey.FootGame.KEY_NEWS_GAME;
//                EventBus.getDefault().post(news);
                break;
            case R.id.firstnews_his_game:
                //历史赛事
                if (hisGameList.isEmpty()) {
                    return;
                }
//                CommStatus.MHISGAMEID = hisGameList.get(position).getId();
//                CommStatus.JUMP_GAME = 1;
//                BaseEvent his = new BaseEvent();
//                his.data = IntentKey.FootGame.KEY_HIS_GAME;
//                EventBus.getDefault().post(his);
                break;
        }
    }

    /**
     * 初始化新闻数据
     */
    private void initHeadline() {
        firstNewsAdapter = new FirstNewsAdapter(getActivity(), newsList);
        firstnewsNews.setAdapter(firstNewsAdapter);
        firstnewsNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {  // 点击新闻列表项进入新闻详情页
                Intent intent = new Intent(mContext, FirstNewsDetailActivity.class);
                intent.putExtra("beandata", newsList.get(position));
                startActivity(intent);
            }
        });
    }

/*    private void initField(){
        fiedAdapter = new FirstNewsGroundAdapter(getActivity(),fieldList);
*//*        firstnewGridview.setAdapter(fiedAdapter);
        firstnewGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, FootBallGroundDetailActivity.class);
                intent.putExtra("beandata",fieldList.get(position));
                startActivity(intent);
            }
        });*//*
    }*/

    /**
     * 初始化Banner
     */
    private void initViewPager() {

        imageAdapter = new ImageAdapter();
        firstnewsViewpager.setAdapter(imageAdapter);
        firstnewsViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != currentPosition) {
                    linPoints.getChildAt(currentPosition).setSelected(false);
                    currentPosition = position;
                    linPoints.getChildAt(currentPosition).setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 加载Banner图片
     */
    public void loadPhoto() {

        //3.请求数据
        new SmartClient(mContext).get(HttpUrlConstant.APP_SERVER_URL + "findAds?position=1", null, new SmartCallback<AdsPhotoBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, AdsPhotoBeanListResult result) {
                tmpvpImges.clear();
                if (result.getData() != null) {
                    tmpvpImges.addAll(result.getData());
                }
//                dealPhoto();
                loadsize++;
//                handler.removeMessages(IF_LOAD_FINISH);
//                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH, 2000);
                dealPhoto();
            }

            @Override
            public void onFailure(int statusCode, String message) {
                loadsize++;
//                handler.removeMessages(IF_LOAD_FINISH);
//                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH, 2000);
            }

        }, AdsPhotoBeanListResult.class);
    }


    /*public void loadPhoto() {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", 1);
        params.put("pageSize", 4);
        params.put("orderby", "createTime desc");
        params.put("status", 1);
        params.put("teamType", 0);
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPhoto", params.toString(), new SmartCallback<SquarePhotoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBeanResult result) {

                tmpvpImges.clear();
                if(result.getList() != null && result.getList().size()<5){
                    tmpvpImges.addAll(result.getList());
                }
//                dealPhoto();
                loadsize++;
                handler.removeMessages(IF_LOAD_FINISH);
                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH,2000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                loadsize++;
                handler.removeMessages(IF_LOAD_FINISH);
                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH,2000);
            }

        }, SquarePhotoBeanResult.class);
    }*/

    /**
     * 删除图片
     */
    private void dealPhoto() {
        vpImges.addAll(tmpvpImges);
        tmpvpImges.clear();
        if (vpImges.size() > 0) {
            imageAdapter.notifyDataSetChanged();
            View view;
            LinearLayout.LayoutParams lp;
            linPoints.removeAllViews();
            int length = vpImges.size() >= 4 ? 4 : vpImges.size();
            for (int i = 0; i < length; i++) {
                view = new View(getActivity());
                view.setBackgroundResource(R.drawable.selector_maintab_tab1);
                view.setEnabled(false);
                lp = new LinearLayout.LayoutParams(UIUtils.dip2px(10), UIUtils.dip2px(10));
                if (i != 0) {
                    lp.leftMargin = UIUtils.dip2px(10);
                }
                view.setLayoutParams(lp);
                linPoints.addView(view);

            }
            // 设置默认选中第一个点
            linPoints.getChildAt(currentPosition).setSelected(true);
        }
    }

    /**
     * 加载赛事数据
     *
     * @param type
     */
    public void loadGame(final int type) {
        RequestParam params = new RequestParam();
        params.put("condition", "and u.teamBOperation =1"); // teamBOperation =1 是指 已约战成功的(包含未开赛 与已结束的比赛)
        params.put("currentPage", 1);
        params.put("teamType", 0); // 赛制 3\5\7\11
        params.put("pageSize", 3);
        params.put("orderby", "beginTime asc"); // APP首页最新赛事的逻辑排列应该以距离现在时间最近的一场球来显示
        if (type == 1) {  //历史赛事
            params.put("gameStatus", 10);  // 已结束
        } else {  //最新赛事
            params.put("gameStatus", 5);  // 未开赛
        }
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                if (type == 1) { //历史赛事
                    firstnewsHisGame.setVisibility(View.VISIBLE);

                    tmphisGameList.clear();
                    tmphisGameList.addAll(result.getList());
                    hisGameList.clear();
                    hisGameList.addAll(tmphisGameList);
                    hisGameAdapter.notifyDataSetChanged();
                } else {
                    firstnewsNewGame.setVisibility(View.VISIBLE);

                    tmpnewGameList.clear();
                    tmpnewGameList.addAll(result.getList());
                    newGameList.clear();
                    newGameList.addAll(tmpnewGameList);
                    newGameAdapter.notifyDataSetChanged();
                }
                loadsize++;
//                handler.removeMessages(IF_LOAD_FINISH);
//                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH, 2000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                loadsize++;
//                handler.removeMessages(IF_LOAD_FINISH);
//                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH, 2000);
            }

        }, GameBeanListResult.class);
    }

    /**
     * 加载新闻
     */
    public void loadNews() {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", 1);
        params.put("pageSize", 6);
        params.put("orderby", "startTime desc");
        params.put("status", 2);
        params.put("condition", " and id>1");

        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listNews", params.toString(), new SmartCallback<NewsBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, NewsBeanListResult result) {
                tmpnewsList.clear();
                if (result.getList() != null) {
                    tmpnewsList.addAll(result.getList());
                }
                loadsize++;
                dealNews();
//                handler.removeMessages(IF_LOAD_FINISH);
//                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH, 2000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                loadsize++;
//                handler.removeMessages(IF_LOAD_FINISH);
//                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH, 2000);
            }

        }, NewsBeanListResult.class);
    }

/*    public void loadField() {
        RequestParam params = new RequestParam();
        params.put("currentPage", 1);
        params.put("pageSize", 6);
        params.put("orderby", "opTime desc");
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listField", params.toString(), new SmartCallback<FieldBeanResult>() {

            @Override
            public void onSuccess(int statusCode, FieldBeanResult result) {
                tmpfieldList.clear();;
                tmpfieldList.addAll(result.getList());
                loadsize++;
//                dealField();
                handler.removeMessages(IF_LOAD_FINISH);
                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH,2000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                loadsize++;
                handler.removeMessages(IF_LOAD_FINISH);
                handler.sendEmptyMessageDelayed(IF_LOAD_FINISH,2000);
            }

        }, FieldBeanResult.class);
    }

    private void dealField(){
        fieldList.addAll(tmpfieldList);
        tmpfieldList.clear();
        if(fieldList.size() > 0){
            firstnewGridview.getLayoutParams();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) firstnewGridview.getLayoutParams();
            params.height = (int) (UIUtils.dip2px(170)*(Math.ceil( fieldList.size()/2.0)));
            firstnewGridview.setLayoutParams(params);
            fiedAdapter.notifyDataSetChanged();
        }
    }*/

    private void dealNews() {
        newsList.addAll(tmpnewsList);
        tmpnewsList.clear();
        if (newsList.size() > 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) firstnewsNews.getLayoutParams();
            params.height = UIUtils.dip2px(75) * newsList.size();
            firstnewsNews.setLayoutParams(params);
            firstNewsAdapter.notifyDataSetChanged();
        }
    }

    public static final int IF_LOAD_FINISH = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IF_LOAD_FINISH:
                    if (loadsize == 4) {
                        dealPhoto();

                        firstnewsHisGame.setVisibility(View.VISIBLE);
                        hisGameList.clear();
                        hisGameList.addAll(tmphisGameList);
                        tmphisGameList.clear();
                        hisGameAdapter.notifyDataSetChanged();

                        firstnewsNewGame.setVisibility(View.VISIBLE);
                        newGameList.clear();
                        newGameList.addAll(tmpnewGameList);
                        tmpnewGameList.clear();
                        newGameAdapter.notifyDataSetChanged();

                        dealNews();
//                        dealField();

                    }
                    break;
            }
        }
    };

    private class ImageAdapter extends PagerAdapter {

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
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            AdsPhotoBean bean = vpImges.get(position);
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getUrl()), imageView, FootBallApplication.options);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(mContext, ActivityDetailActivity.class));
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
