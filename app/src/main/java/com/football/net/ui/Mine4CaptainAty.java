package com.football.net.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.Mine4CaptainAdapter;
import com.football.net.bean.GameBean;
import com.football.net.bean.MessageBean;
import com.football.net.bean.MineBean;
import com.football.net.bean.RecruitBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.MessageBeanResult;
import com.football.net.http.reponse.impl.RecruitBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.reponse.impl.SquareVideoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.MyRatingBar;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/13.
 */
public class Mine4CaptainAty extends BasicActivity {

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


    @BindView(R.id.id_load_data_txt)
    TextView mTxtLoadData;
    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    Mine4CaptainAdapter adapter;

    int count = 0;
    ArrayList<MineBean> dataList = new ArrayList<MineBean>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine4captain;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_mine;
    }

    public void initView() {
//        textView.setText("我");
//        textRight.setVisibility(View.GONE);
//        returnBtn.setVisibility(View.GONE);
        UserBean bean = FootBallApplication.userbean;
        name.setText(bean.getName());
        bodyHeight.setText("身高：" + (bean.getHeight() == null ? "暂无" : bean.getHeight() + "CM"));
        tvposition.setText("擅长位置：" + CommonUtils.getPositionStr(bean.getPosition()));
        ratingbar.setRating(bean.getOfficial());
        levelV.setText("Lv" + bean.getOfficial());
        bodyWeight.setText("体重：" + (bean.getWeight() == null ? "暂无" : bean.getWeight() + "KG"));
        if (bean.getTeam() != null) {
            teamName.setText("所在球队：" + bean.getTeam().getTeamTitle());
        } else {
            teamName.setText("所在球队：暂无");
        }
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), header, FootBallApplication.circOptions);

        recyView.setHasFixedSize(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyView.setLayoutManager(manager);


        adapter = new Mine4CaptainAdapter(mContext, dataList);
        recyView.setAdapter(adapter);

        mTxtLoadData.setVisibility(View.VISIBLE);
        recyView.setVisibility(View.GONE);
        // 获取招人信息
        loadRecruit(1);
//        loadSignInOrInnerMessage(2, 1);
//        loadSignInOrInnerMessage(1, 1);
//        loadPhoto(1);
//        loadVideo(1);
//        loadAppointment(1);
//        loadScoreList(1);

    }

    /**
     * 获取球招人列表/邀我入队
     *
     * @param page
     */
    public void loadRecruit(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 2);
//        params.put("viewType", 1);
        params.put("orderby", "opTime desc");
        if (FootBallApplication.userbean.getTeam() != null) {
            params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        }
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");

        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listRecruit", params.toString(), new SmartCallback<RecruitBeanResult>() {

            @Override
            public void onSuccess(int statusCode, RecruitBeanResult result) {
                count++;
                ArrayList<RecruitBean> list = result.getList();
                if (list.size() > 0) {
                    for (RecruitBean bean : list) {
                        bean.setBeanType(MineBean.CapterAllList_Type_Recruit);
                    }
                    dataList.addAll(list);
                }
                loadSignInOrInnerMessage(2, 1);
//                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                loadSignInOrInnerMessage(2, 1);
            }

        }, RecruitBeanResult.class);
    }


    public void loadSignInOrInnerMessage(final int type, final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 2);
        params.put("type", type);
        params.put("orderby", "opTime desc");
        if (FootBallApplication.userbean.getTeam() != null) {
            params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        }
//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listMessage", params.toString(), new SmartCallback<MessageBeanResult>() {

            @Override
            public void onSuccess(int statusCode, MessageBeanResult result) {
                count++;
                ArrayList<MessageBean> list = result.getList();
                if (list.size() > 0) {
                    for (MessageBean bean : list) {
                        bean.setBeanType(MineBean.CapterAllList_Type_SigninMessage);
                    }
                    dataList.addAll(list);
                }
                if (type == 2) {
                    loadSignInOrInnerMessage(1, 1);
                } else if (type == 1) {
                    loadPhoto(1);
                }

//                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
//                handler.sendEmptyMessage(1000);
                if (type == 2) {
                    loadSignInOrInnerMessage(1, 1);
                } else if (type == 1) {
                    loadPhoto(1);
                }
            }

        }, MessageBeanResult.class);
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
                        bean.setBeanType(MineBean.CapterAllList_Type_Photo);
                    }
                    dataList.addAll(photoList);
                }
//                handler.sendEmptyMessage(1000);
                loadVideo(1);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
//                handler.sendEmptyMessage(1000);
                loadVideo(1);
            }

        }, SquarePhotoBeanResult.class);
    }

    public void loadVideo(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 2);
        params.put("playerId", FootBallApplication.userbean.getId());
        params.put("orderby", "createTime desc");
        params.put("status", 1);
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listVideo", params.toString(), new SmartCallback<SquareVideoBeanResult>() {

            @Override
            public void onSuccess(int statusCode, SquareVideoBeanResult result) {
                count++;
                ArrayList<SquareVideoBean> videoList = result.getList();
                if (videoList.size() > 0) {
                    for (SquareVideoBean bean : videoList) {
                        bean.setBeanType(MineBean.CapterAllList_Type_Video);
                    }
                    dataList.addAll(videoList);
                }
//                handler.sendEmptyMessage(1000);
                loadAppointment(1);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
//                handler.sendEmptyMessage(1000);
                loadAppointment(1);
            }

        }, SquareVideoBeanResult.class);
    }


    /**
     * 最新赛事
     *
     * @param page
     */
    public void loadAppointment(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("orderby", "beginTime asc");
        params.put("currentPage", page);
        params.put("teamId", FootBallApplication.userbean.getTeam().getId());
        params.put("gameStatus", 5); // 最新赛事
        params.put("pageSize", 2);

//        params.put("condition", "and (u.dismissed is null or u.dismissed !=1)");
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                count++;
                List<GameBean> list = result.getList();
                if (list.size() > 0) {
                    for (GameBean bean : list) {
                        bean.setBeanType(MineBean.CapterAllList_Type_Appointment);
                    }
                    dataList.addAll(list);
                }
//                handler.sendEmptyMessage(1000);
                loadScoreList(1);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
//                handler.sendEmptyMessage(1000);
                loadScoreList(1);
            }

        }, GameBeanListResult.class);
    }

    /**
     * 历史赛事
     *
     * @param page
     */
    public void loadScoreList(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 2);
        params.put("gameStatus", 10);
        params.put("orderby", "beginTime desc");
        params.put("teamId", FootBallApplication.userbean.getTeam() == null ? "" : FootBallApplication.userbean.getTeam().getId());
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listGame", params.toString(), new SmartCallback<GameBeanListResult>() {

            @Override
            public void onSuccess(int statusCode, GameBeanListResult result) {
                count++;
                List<GameBean> list = result.getList();
                if (list.size() > 0) {
                    for (GameBean bean : list) {
                        bean.setBeanType(MineBean.CapterAllList_Type_Score);
                    }
                    dataList.addAll(list);
                }
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(1000);
            }

        }, GameBeanListResult.class);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //adapter.notifyDataSetChanged();
            switch (msg.what) {
                case 1000:
                    if (count == 7) {
                        if (dataList.isEmpty()) {
                            mTxtLoadData.setText("加载数据失败");
                        } else {
                            mTxtLoadData.setVisibility(View.GONE);
                            recyView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.userCenter, R.id.header, R.id.myinfo, R.id.myteam, R.id.right_txt, R.id.user_foot})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.userCenter:
                if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                    mContext.startActivity(new Intent(mContext, MineCenter4MemberAtivity_.class));
                } else {
                    mContext.startActivity(new Intent(mContext, MineCenter4CaptainAtivity_.class));
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
            case R.id.myteam:
                if (FootBallApplication.userbean.getTeam() != null) {
                    Intent intent = new Intent(this, MineTeamAtivity_.class);
                    startActivity(intent);
                } else {
                    showMsg("您还没有加入任何球队！");
                }
                break;
            case R.id.right_txt:
            case R.id.user_foot:
                //进入战术板
                if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() == null) {
                    ToastUtil.show(mContext, "您还没有加入任何球队");
                    return;
                }
                Intent intent = new Intent(mContext, TeamDetialActivity2.class);
                intent.putExtra("teamBean", FootBallApplication.userbean.getTeam());
                mContext.startActivity(intent);
                break;
        }
    }
}
