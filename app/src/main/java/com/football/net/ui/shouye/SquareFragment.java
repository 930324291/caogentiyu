package com.football.net.ui.shouye;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.ApplyBean;
import com.football.net.bean.GameBean;
import com.football.net.bean.RecruitBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.LogUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.ApplyBeanResult;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.RecruitBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.reponse.impl.SquareVideoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnButtonClickListener;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.RecruitPeopleDetailActivity;
import com.football.net.widget.SwipyRefreshLayout;
import com.football.net.widget.SwipyRefreshLayoutDirection;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广场
 */
public class SquareFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_order_refresh_ll)
    SwipyRefreshLayout mSwipyRefreshLayout; // 内容刷新视图

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
    @BindView(R.id.lin_pic)
    LinearLayout linPic;
    @BindView(R.id.lin_video)
    LinearLayout linVideo;
    @BindView(R.id.lin_team)
    LinearLayout linTeam;
    @BindView(R.id.lin_recruit)
    LinearLayout linRecruit;
    @BindView(R.id.lin_game)
    LinearLayout linGame;
    @BindView(R.id.ultimate_recycler_view)
    RecyclerView ultimateRecyclerView;

    @BindView(R.id.tv_pic)
    TextView tvPic;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.tv_team)
    TextView tvTeam;
    @BindView(R.id.tv_recruit)
    TextView tvRecruit;
    @BindView(R.id.tv_game)
    TextView tvGame;

    int currentfirstIndex = 0;
    int currentsecondIndex = 0;
    ArrayList<View> firstTv;
    ArrayList<TextView> secondTv;
    ArrayList<ImageView> tabPicture = new ArrayList<>();

    @BindView(R.id.tab1)
    ImageView tab1;
    @BindView(R.id.tab2)
    ImageView tab2;
    @BindView(R.id.tab3)
    ImageView tab3;
    @BindView(R.id.tab4)
    ImageView tab4;
    @BindView(R.id.tab5)
    ImageView tab5;

    LinearLayoutManager lineManager;
    GridLayoutManager gridmanager;

    int page = 1;

    SquarePhotoNewAdapter photoAdapter;
    SquareVideoNewAdapter videoAdapter;
    SquareRecuirAdapter recruitAdapter;
    SquareApplyAdapter applyAdapter;
    SquareAppointmentAdapter appointmentAdapeter;

    ArrayList<SquarePhotoBean> photoList = new ArrayList<SquarePhotoBean>();
    ArrayList<SquareVideoBean> videoList = new ArrayList<SquareVideoBean>();
    ArrayList<RecruitBean> recruitList = new ArrayList<RecruitBean>();
    ArrayList<ApplyBean> applyList = new ArrayList<ApplyBean>();
    ArrayList<GameBean> dataList5 = new ArrayList<GameBean>();

    public static SquareFragment newInstance() {
        SquareFragment fragment = new SquareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_square;
    }

    @Override
    protected void initView() {
        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
        mSwipyRefreshLayout.setColorSchemeColors(COLOR_SCHEMES);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);

        EventBus.getDefault().register(this);
        firstTv = new ArrayList<>();
        secondTv = new ArrayList<>();
        firstTv.add(line1);
        firstTv.add(line2);
        firstTv.add(line3);
        firstTv.add(line4);
        firstTv.add(line5);
        secondTv.add(tvGame);
        secondTv.add(tvRecruit);
        secondTv.add(tvPic);
        secondTv.add(tvVideo);
        secondTv.add(tvTeam);
        tabPicture.add(tab5);
        tabPicture.add(tab4);
        tabPicture.add(tab1);
        tabPicture.add(tab2);
        tabPicture.add(tab3);

        ultimateRecyclerView.setHasFixedSize(false);
        lineManager = new LinearLayoutManager(mContext);
        gridmanager = new GridLayoutManager(mContext, 3);
        ultimateRecyclerView.setLayoutManager(lineManager);

        photoAdapter = new SquarePhotoNewAdapter(mContext, photoList);
        videoAdapter = new SquareVideoNewAdapter(mContext, videoList);
        recruitAdapter = new SquareRecuirAdapter(mContext, recruitList);
        recruitAdapter.setmOnItemClickLitener(new SquareRecuirAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, RecruitPeopleDetailActivity.class);
                intent.putExtra("beandata", recruitList.get(position));
                mContext.startActivity(intent);
            }
        });
        applyAdapter = new SquareApplyAdapter(mContext, applyList);
        appointmentAdapeter = new SquareAppointmentAdapter(mContext, dataList5);
        appointmentAdapeter.setmOnButtonClickListener(new OnButtonClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GameBean bean = dataList5.get(position);
                commit(bean.getId(), FootBallApplication.userbean.getTeam().getId()); // 应战应该当前登录用户（队长）操作
            }
        });

//        ultimateRecyclerView.setAdapter(photoAdapter);
        ultimateRecyclerView.setAdapter(appointmentAdapeter);
        firstTv.get(currentfirstIndex).setVisibility(View.VISIBLE);
        secondTv.get(currentsecondIndex).setSelected(true);
        tabPicture.get(currentsecondIndex).setSelected(true);
//        ultimateRecyclerView.setLoadMoreView(R.layout.custom_bottom_progressbar);
//        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
//                page++;
//                loadMoreData();
//            }
//        });
//        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                dealMeathord(currentfirstIndex, currentsecondIndex, true);
//            }
//        });
        dealMeathord(currentfirstIndex, currentsecondIndex, true);
//        loadPhoto(1,0);
    }

    boolean isInited = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isInited) {
            isInited = true;
            mSwipyRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipyRefreshLayout.setRefreshing(true);
                }
            });
//            loadPhoto(1, 0);
            loadData5(1, 0);
        }
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5,
            R.id.lin_pic, R.id.lin_video, R.id.lin_team, R.id.lin_game, R.id.lin_recruit})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        int firstIndex = currentfirstIndex;
        int secondIndex = currentsecondIndex;
        switch (v.getId()) {
            case R.id.layout1:
                firstIndex = 0;
                break;
            case R.id.layout2:
                firstIndex = 1;
                break;
            case R.id.layout3:
                firstIndex = 2;
                break;
            case R.id.layout4:
                firstIndex = 3;
                break;
            case R.id.layout5:
                firstIndex = 4;
                break;
            case R.id.lin_pic:
                secondIndex = 2;
                break;
            case R.id.lin_video:
                secondIndex = 3;
                break;
            case R.id.lin_team:
                secondIndex = 4;
                break;
            case R.id.lin_recruit:
                secondIndex = 1;
                break;
            case R.id.lin_game:
                secondIndex = 0;
                break;
        }
        firstTv.get(currentfirstIndex).setVisibility(View.INVISIBLE);
        secondTv.get(currentsecondIndex).setSelected(false);
        tabPicture.get(currentsecondIndex).setSelected(false);
        firstTv.get(firstIndex).setVisibility(View.VISIBLE);
        secondTv.get(secondIndex).setSelected(true);
        tabPicture.get(secondIndex).setSelected(true);
        dealMeathord(firstIndex, secondIndex, false);
    }


    private void dealMeathord(int firstIndex, int secondIndex, boolean refresh) {
        if (currentfirstIndex != firstIndex || currentsecondIndex != secondIndex || refresh) {
            currentfirstIndex = firstIndex;
            currentsecondIndex = secondIndex;
            mSwipyRefreshLayout.setRefreshing(true);
            page = 1;
            if (currentsecondIndex == 0) {
                loadData5(page, getType(currentfirstIndex));
            } else if (currentsecondIndex == 1) {
                loadApply(page, getType(currentfirstIndex));
            } else if (currentsecondIndex == 2) {
                loadPhoto(page, getType(currentfirstIndex));
            } else if (currentsecondIndex == 3) {
                loadVideo(page, getType(currentfirstIndex));
            } else if (currentsecondIndex == 4) {
                loadRecruit(page, getType(currentfirstIndex));
            }
        }
    }

    private void loadMoreData() {
        if (currentsecondIndex == 0) {
            loadData5(page, getType(currentfirstIndex));
        } else if (currentsecondIndex == 1) {
            loadApply(page, getType(currentfirstIndex));
        } else if (currentsecondIndex == 2) {
            loadPhoto(page, getType(currentfirstIndex));
        } else if (currentsecondIndex == 3) {
            loadVideo(page, getType(currentfirstIndex));
        } else if (currentsecondIndex == 4) {
            loadRecruit(page, getType(currentfirstIndex));
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            page = 1;
            dealMeathord(currentfirstIndex, currentsecondIndex, true);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            page++;
            loadMoreData();
        }
    }


    private int getType(int currentfirstIndex) {
        int type = 0;
        switch (currentfirstIndex) {
            case 0:
                type = 0;
                break;
            case 1:
                type = 3;
                break;
            case 2:
                type = 5;
                break;
            case 3:
                type = 7;
                break;
            case 4:
                type = 10;
                break;
        }
        return type;
    }

    public void loadPhoto(final int page, int teamType) {
        if (page == 1) {
            ultimateRecyclerView.setLayoutManager(gridmanager);
            ultimateRecyclerView.setAdapter(photoAdapter);
        }
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("orderby", "createTime desc");
        params.put("status", 1);
        params.put("teamType", teamType);
        params.put("viewType", "1");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPhoto", params.toString(), new SmartCallback<SquarePhotoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    photoList.clear();
                }
                photoList.addAll(result.getList());
                LogUtils.d(photoList.toString() + "=-=-=-=--=-=");
                photoAdapter.setIsrFresh(true);
                photoAdapter.notifyDataSetChanged();
                if (photoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
                } else {
//                    ultimateRecyclerView.disableLoadmore();
                    ToastUtil.show(mContext, "暂无更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, SquarePhotoBeanResult.class);
    }

    public void loadVideo(final int page, int teamType) {
        if (page == 1) {
            ultimateRecyclerView.setLayoutManager(gridmanager);
            ultimateRecyclerView.setAdapter(videoAdapter);
        }
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 12);
        params.put("viewType", 1);
        params.put("orderby", "createTime desc");
        params.put("status", 1);
        params.put("teamType", teamType);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listVideo", params.toString(), new SmartCallback<SquareVideoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquareVideoBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    videoList.clear();
                }
                videoList.addAll(result.getList());
                LogUtils.d(videoList.toString() + "=-=-=-=--=-=");
                videoAdapter.setIsrFresh(true);
                videoAdapter.notifyDataSetChanged();
                if (videoList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
                } else {
//                    ultimateRecyclerView.disableLoadmore();
                    ToastUtil.show(mContext, "暂无更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, SquareVideoBeanResult.class);
    }

    public void loadRecruit(final int page, int teamType) {
        if (page == 1) {
            ultimateRecyclerView.setLayoutManager(lineManager);
            ultimateRecyclerView.setAdapter(recruitAdapter);
        }
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
//        params.put("pageSize", 12);
//        params.put("viewType", 1);
        params.put("orderby", "opTime desc");
        params.put("isPublic", 1);
        params.put("teamType", teamType);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listRecruit", params.toString(), new SmartCallback<RecruitBeanResult>() {

            @Override
            public void onSuccess(int statusCode, RecruitBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    recruitList.clear();
                }
                recruitList.addAll(result.getList());
                recruitAdapter.notifyDataSetChanged();
                if (recruitList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
                } else {
//                    ultimateRecyclerView.disableLoadmore();
                    ToastUtil.show(mContext, "暂无更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, RecruitBeanResult.class);
    }

    public void loadApply(final int page, int teamType) {
        if (page == 1) {
            ultimateRecyclerView.setLayoutManager(lineManager);
            ultimateRecyclerView.setAdapter(applyAdapter);
        }
        RequestParam params = new RequestParam();
        params.put("currentPage", page);
        params.put("isEnabled", 1);
        params.put("isOpen", 1);
        params.put("isPublic", 1);
        params.put("orderby", "applyTime desc");
        params.put("teamType", teamType);
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listApply", params.toString(), new SmartCallback<ApplyBeanResult>() {

            @Override
            public void onSuccess(int statusCode, ApplyBeanResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    applyList.clear();
                }
                applyList.addAll(result.getList());
                applyAdapter.notifyDataSetChanged();
                if (applyList.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
                } else {
//                    ultimateRecyclerView.disableLoadmore();
                    ToastUtil.show(mContext, "暂无更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, ApplyBeanResult.class);
    }

    //    public void loadGame(final int page,int teamType) {
//        ultimateRecyclerView.setAdapter(applyAdapter);
//        RequestParam params = new RequestParam();
//        params.put("currentPage", page);
//        params.put("isEnabled", 1);
//        params.put("isPublic", 1);
//        params.put("orderby", "beginTime desc");
//        params.put("teamType", teamType);
//        params.put("gameStatus", 1);
////        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
//        //3.请求数据
//        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<ApplyBeanResult>() {
//
//            @Override
//            public void onSuccess(int statusCode, ApplyBeanResult result) {
//                ultimateRecyclerView.setRefreshing(false);
//                if (page == 1) {
//                    applyList.clear();
//                }
//                applyList.addAll(result.getList());
//                if(applyList.size() > 0){
//                    applyAdapter.notifyDataSetChanged();
//                }
////                if (videoList.size() < result.getTotalRecord()) {
////                    ultimateRecyclerView.reenableLoadmore();
////                } else {
////                    ultimateRecyclerView.disableLoadmore();
////                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//                ultimateRecyclerView.setRefreshing(false);
//            }
//
//        }, ApplyBeanResult.class);
//    }
    public void loadData5(final int page, int teamType) {
        if (page == 1) {
            ultimateRecyclerView.setLayoutManager(lineManager);
            ultimateRecyclerView.setAdapter(appointmentAdapeter);
        }
        RequestParam params = new RequestParam();
        params.put("isEnabled", "1");
        params.put("isPublic", "1");
        params.put("currentPage", page);
        params.put("teamType", teamType);
        params.put("pageSize", 12);
        params.put("orderby", "beginTime desc");
        params.put("gameStatus", 1);
        params.put("condition", "and u.beginTime > now()");

//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                mSwipyRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    dataList5.clear();
                }
                dataList5.addAll(result.getList());
                appointmentAdapeter.notifyDataSetChanged();
                if (dataList5.size() < result.getTotalRecord()) {
//                    ultimateRecyclerView.reenableLoadmore();
                } else {
//                    ultimateRecyclerView.disableLoadmore();
                    ToastUtil.show(mContext, "暂无更多数据");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                mSwipyRefreshLayout.setRefreshing(false);
            }

        }, GameBeanListResult.class);
    }


    //GET http://47.89.46.215/app/respondDare?gameId=83&operation=2 HTTP/1.1
    public void commit(int gameId, int teamBId) {
        showProgress("提交中...");
        SmartParams params = new SmartParams();
        params.put("operation", 1);
        params.put("gameId", gameId);
        params.put("teamBId", teamBId);
        //3.请求数据
        new SmartClient(mContext).get(HttpUrlConstant.APP_SERVER_URL + "respondDare", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                showMsg("应战成功");
                loadData5(page, getType(currentfirstIndex));
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("应战失败");
            }

        }, Result.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            //&& resultCode == Activity.RESULT_OK
//            ultimateRecyclerView.setRefreshing(true);
//            page = 1;
//            ultimateRecyclerView.setRefreshing(true);
//            loadPhoto(1, getType(currentfirstIndex));
        } else if (requestCode == 11) {
            //&& resultCode == Activity.RESULT_OK
//            ultimateRecyclerView.setRefreshing(true);
//            page = 1;
//            ultimateRecyclerView.setRefreshing(true);
//            loadVideo(1, getType(currentfirstIndex));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(final BaseEvent event) {
        String data = event.data;
        if (TextUtils.equals(data, IntentKey.FootGame.KEY_REFRESH_GUANG)) {
            if (event.flag == 10) {
//                ultimateRecyclerView.setRefreshing(true);
//                page = 1;
//                ultimateRecyclerView.setRefreshing(true);
//                loadPhoto(1, getType(currentfirstIndex));
            } else if (event.flag == 11) {
//                ultimateRecyclerView.setRefreshing(true);
//                page = 1;
//                ultimateRecyclerView.setRefreshing(true);
//                loadVideo(1, getType(currentfirstIndex));
            } else if (event.flag == 12) {
                String zan = event.zan_Num;
                SquarePhotoBean bean = photoList.get(photoAdapter.getPostion());
                bean.setLikes(zan);
                photoAdapter.setIsrFresh(false);
                photoAdapter.notifyDataSetChanged();
            } else if (event.flag == 13) {
                String zan = event.zan_Num;
                SquareVideoBean bean = videoList.get(videoAdapter.getPostion());
                bean.setLikes(zan);
                videoAdapter.setIsrFresh(false);
                videoAdapter.notifyDataSetChanged();
            }
        }
    }
}
