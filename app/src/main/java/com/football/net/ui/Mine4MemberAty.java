package com.football.net.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.MineAdapter;
import com.football.net.bean.GameBean;
import com.football.net.bean.MessageTankBean;
import com.football.net.bean.MineBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.MessageTankBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.reponse.impl.SquareVideoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MyRatingBar;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/13.
 */
public class Mine4MemberAty extends BasicActivity {

//    @BindView(R.id.title)
//    TextView textView;
//    @BindView(R.id.right_txt)
//    TextView textRight;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.bodyHeight)
    TextView bodyHeight;
    @BindView(R.id.tvposition)
    TextView tvposition;
    @BindView(R.id.ratingbar)
    MyRatingBar ratingbar;
    @BindView(R.id.levelV)
    TextView levelV;
    @BindView(R.id.bodyWeight)
    TextView bodyWeight;
    @BindView(R.id.teamName)
    TextView teamName;
    @BindView(R.id.header)
    ImageView header;
//    @BindView(R.id.returnBtn)
//    ImageView returnBtn;


    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    MineAdapter adapter;

    int count = 0;
    ArrayList<MineBean> dataList = new ArrayList<MineBean>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine;
    }

    protected void initView() {
//        textView.setText("我");
//        textRight.setVisibility(View.GONE);
//        returnBtn.setVisibility(View.GONE);
//        UserBean bean = FootBallApplication.userbean;
//        name.setText(bean.getName());
//        bodyHeight.setText("身高：" + (bean.getHeight() == null ? "暂无" : bean.getHeight() + "CM"));
//        tvposition.setText("擅长位置：" + CommonUtils.getPositionStr(bean.getPosition()));
//        ratingbar.setRating(bean.getOfficial());
//        levelV.setText("Lv" + bean.getOfficial());
//        bodyWeight.setText("体重：" + (bean.getWeight() == null ? "暂无" : bean.getWeight() + "KG"));
//        if (bean.getTeam() != null) {
//            teamName.setText("所在球队：" + bean.getTeam().getTeamTitle());
//        } else {
//            teamName.setText("所在球队：暂无");
//        }
//        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), header, FootBallApplication.circOptions);

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);


        adapter = new MineAdapter(mContext, dataList);
        recyView.setAdapter(adapter);

        loadPhoto(1);
        loadVideo(1);
        loadMessageTank(1);
        loadScoreList(1);

    }

    public void loadPhoto(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 2);
        params.put("orderby", "createTime desc");
        params.put("status", 1);
        params.put("playerId", FootBallApplication.userbean.getId());
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listPhoto", params.toString(), new SmartCallback<SquarePhotoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquarePhotoBeanResult result) {
                count++;
                ArrayList<SquarePhotoBean> photoList = result.getList();
                if (photoList.size() > 0) {
                    for (SquarePhotoBean bean : photoList) {
                        bean.setBeanType(1);
                    }
                    dataList.addAll(photoList);
                }
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(1000);
            }

        }, SquarePhotoBeanResult.class);
    }

    public void loadVideo(final int page) {
//        RequestParam params = new RequestParam();
//        params.put("isEnabled", 1);
//        params.put("currentPage", page);
//        params.put("pageSize", 2);
//        params.put("playerId", FootBallApplication.userbean.getId());
//        params.put("orderby", "createTime desc");
//        params.put("status", 1);
//        //3.请求数据
//        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listVideo", params.toString(), new SmartCallback<SquareVideoBeanResult>() {
//
//            @Override
//            public void onSuccess(int statusCode, SquareVideoBeanResult result) {
//                count++;
//                ArrayList<SquareVideoBean> videoList = result.getList();
//                if (videoList.size() > 0) {
//                    for (SquareVideoBean bean : videoList) {
//                        bean.setBeanType(2);
//                    }
//                    dataList.addAll(videoList);
//                }
//                handler.sendEmptyMessage(1000);
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//                count++;
//                handler.sendEmptyMessage(1000);
//            }
//
//        }, SquareVideoBeanResult.class);
    }

    public void loadMessageTank(final int page) {
//        RequestParam params = new RequestParam();
//        params.put("isEnabled", 1);
//        params.put("currentPage", page);
//        params.put("pageSize", 2);
//        params.put("type", 2);
//        params.put("playerId", FootBallApplication.userbean.getId());
//        //3.请求数据
//        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listMessageTank", params.toString(), new SmartCallback<MessageTankBeanResult>() {
//
//            @Override
//            public void onSuccess(int statusCode, MessageTankBeanResult result) {
//                count++;
//                ArrayList<MessageTankBean> list = result.getList();
//                if (list.size() > 0) {
//                    for (MessageTankBean bean : list) {
//                        bean.setBeanType(3);
//                    }
//                    dataList.addAll(list);
//                }
//                handler.sendEmptyMessage(1000);
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//                count++;
//                handler.sendEmptyMessage(1000);
//            }
//
//        }, MessageTankBeanResult.class);
    }

    public void loadScoreList(final int page) {
//        RequestParam params = new RequestParam();
//        params.put("isEnabled", 1);
//        params.put("currentPage", page);
//        params.put("pageSize", 2);
//        params.put("teamId", FootBallApplication.userbean.getTeam() == null ? "" : FootBallApplication.userbean.getTeam().getId());
//        //3.请求数据
//        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listScore", params.toString(), new SmartCallback<GameBeanListResult>() {
//
//            @Override
//            public void onSuccess(int statusCode, GameBeanListResult result) {
//                count++;
//                List<GameBean> list = result.getList();
//                if (list.size() > 0) {
//                    for (GameBean bean : list) {
//                        bean.setBeanType(4);
//                    }
//                    dataList.addAll(list);
//                }
//                handler.sendEmptyMessage(1000);
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//                count++;
//                handler.sendEmptyMessage(1000);
//            }
//
//        }, GameBeanListResult.class);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1000:
                    if (count == 4) {
                        if (dataList.size() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.userCenter, R.id.header, R.id.myinfo, R.id.right_txt, R.id.user_foot})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.userCenter:
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                    mContext.startActivity(new Intent(mContext, MineCenter4MemberAtivity_.class));
                }
                break;
            case R.id.header:
//                mContext.startActivity(new Intent(mContext, MineInfoActivity.class));
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                    Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                    intent.putExtra("beanid", FootBallApplication.userbean.getId());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                    intent.putExtra("beanid", FootBallApplication.userbean.getId());
                    mContext.startActivity(intent);
                }
                break;
            case R.id.myinfo:
                Intent intent0 = new Intent(mContext, MinePersonalInfoAtivity_.class);
                mContext.startActivity(intent0);
                break;
            case R.id.right_txt:
            case R.id.user_foot:
                //进入战术板
                if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() == null) {
                    ToastUtil.show(mContext, "您还没有任何球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", FootBallApplication.userbean.getTeam());
                mContext.startActivity(intent);
                break;
        }
    }


}
