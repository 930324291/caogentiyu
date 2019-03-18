package com.football.net.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.Mine4CaptainAdapter;
import com.football.net.adapter.MineAdapter;
import com.football.net.bean.GameBean;
import com.football.net.bean.MessageBean;
import com.football.net.bean.MessageTankBean;
import com.football.net.bean.MineBean;
import com.football.net.bean.RecruitBean;
import com.football.net.bean.ScoreListBean;
import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.impl.GameBeanListResult;
import com.football.net.http.reponse.impl.MessageBeanResult;
import com.football.net.http.reponse.impl.MessageTankBeanResult;
import com.football.net.http.reponse.impl.RecruitBeanResult;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.http.reponse.impl.SquarePhotoBeanResult;
import com.football.net.http.reponse.impl.SquareVideoBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.MineCenter4CaptainAtivity;
import com.football.net.ui.MineCenter4CaptainAtivity_;
import com.football.net.ui.MineCenter4MemberAtivity;
import com.football.net.ui.MineCenter4MemberAtivity_;
import com.football.net.ui.MineInfoActivity;
import com.football.net.ui.MinePersonalInfoAtivity;
import com.football.net.ui.MinePersonalInfoAtivity_;
import com.football.net.ui.MineTeamAtivity;
import com.football.net.ui.MineTeamAtivity_;
import com.football.net.ui.PlayerDetial4CaptainActivity;
import com.football.net.ui.PlayerDetialActivity;
import com.football.net.ui.TeamDetialActivity1;
import com.football.net.ui.TeamDetialActivity2;
import com.football.net.widget.MyRatingBar;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：Raoqw on 2016/9/12 15:41
 * Email：lhholylight@163.com
 */

public class Mine4CaptainFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView textView;
    @BindView(R.id.right_txt)
    TextView textRight;
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
    @BindView(R.id.returnBtn)
    ImageView returnBtn;


    @BindView(R.id.ultimate_recycler_view)
    UltimateRecyclerView recyView;
    Mine4CaptainAdapter adapter;

    int count = 0;
    ArrayList<MineBean> dataList = new ArrayList<MineBean>();

    @Override
    protected void initView() {
        textView.setText("我");
        textRight.setVisibility(View.GONE);
        returnBtn.setVisibility(View.GONE);
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

        loadRecruit(1);
        loadSignInOrInnerMessage(2, 1);
        loadSignInOrInnerMessage(1, 1);
        loadPhoto(1);
        loadVideo(1);
        loadAppointment(1);
        loadScoreList(1);

    }

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
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
            }

        }, RecruitBeanResult.class);
    }


    public void loadSignInOrInnerMessage(int type, final int page) {
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
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(1000);
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
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(1000);
            }

        }, SquareVideoBeanResult.class);
    }


    public void loadAppointment(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("orderby", "beginTime desc");
        params.put("currentPage", page);
        params.put("teamId", FootBallApplication.userbean.getTeam().getId());
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
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onFailure(int statusCode, String message) {
                count++;
                handler.sendEmptyMessage(1000);
            }

        }, GameBeanListResult.class);
    }

    public void loadScoreList(final int page) {
        RequestParam params = new RequestParam();
        params.put("isEnabled", 1);
        params.put("currentPage", page);
        params.put("pageSize", 2);
        params.put("teamId", FootBallApplication.userbean.getTeam() == null ? "" : FootBallApplication.userbean.getTeam().getId());
        //3.请求数据
        new SmartClient(mContext).post(HttpUrlConstant.APP_SERVER_URL + "listScore", params.toString(), new SmartCallback<GameBeanListResult>() {

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
            switch (msg.what) {

                case 10000:
                    if (count == 7) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.userCenter, R.id.header, R.id.myinfo, R.id.myteam, R.id.right_txt,R.id.user_foot})
    void onClick(View v) {
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
                    Intent intent = new Intent(getActivity(), MineTeamAtivity_.class);
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine4captain;
    }
}
