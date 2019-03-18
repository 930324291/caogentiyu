package com.football.net.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.base.CommonAdapter;
import com.football.net.adapter.base.ViewHolder;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.BaseEvent;
import com.football.net.common.constant.CommonList;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.LogUtils;
import com.football.net.common.util.StringUtils;
import com.football.net.common.util.ToastUtil;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.CustomViews;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
public class ModifyPlayBookAty1 extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.id_modify_Custom)
    CustomViews mMoveCustv;
    private List<UserBean> mTopPlayer = new ArrayList<>();

    @BindView(R.id.layout2)
    View layout2; // 点赞视图

    //显示全部球员信息
    @BindView(R.id.id_modify_pos_gridv)
    GridView mGridView;
    private CommonAdapter<UserBean> mPlayerAdp;
    //显示下面展示的球员信息
    private List<UserBean> mBottomPlayers = new ArrayList<>();

    //存储全部球员的信息
    private List<UserBean> mAllPlayers = new ArrayList<>();

    private int mTeamId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_modify_book1);
        ButterKnife.bind(this);
        initV();
        initD1();
    }

    private void initV() {
        mTeamId = getIntent().getIntExtra("teamId", -1);
        layout2.setVisibility(View.GONE);
        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_CAPTAIN) {  // 若为队长显示成员列表视图
            if ((FootBallApplication.userbean != null) && FootBallApplication.userbean.getTeam() != null) {
                if (mTeamId == FootBallApplication.userbean.getTeam().getId()) {
                    layout2.setVisibility(View.VISIBLE);
                }
            }
        }
        mPlayerAdp = new CommonAdapter<UserBean>(this, mBottomPlayers, R.layout.item_player_pos) {
            @Override
            public void convert(ViewHolder helper, UserBean item) {
                TextView mTxtName = helper.getView(R.id.id_player_name_txt);
                TextView mTxtDel = helper.getView(R.id.id_remove_txt);
                ImageView mImgHead = helper.getView(R.id.id_player_head_img);
                ImageView mImgDel = (ImageView) helper.getViewById(R.id.id_del_player_img);

                String name = item.getName();
                String n = "";
                if (!StringUtils.isEmpty(name)) {
                    if (name.length() <= 3) {
                        n = name;
                    } else {
                        n = name.substring(0, 3);
                    }
                }
                mTxtName.setText(n);
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(item.getIconUrl()), mImgHead, FootBallApplication.circOptions);
                mImgDel.setVisibility(View.GONE);
                mTxtDel.setVisibility(View.GONE);
            }
        };
        mGridView.setAdapter(mPlayerAdp);
        mGridView.setOnItemClickListener(this);

        mMoveCustv.setOnMoveViewListener(new CustomViews.moveViewListener() {
            @Override
            public void moveViewLeft(View child, int left, int dx) {
                for (int i = 0; i < mTopPlayer.size(); i++) {
                    if (i == child.getId()) {
                        UserBean bean = mTopPlayer.get(i);
                        bean.setLocationx(left);

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                        params.setMargins(bean.getLocationx(), bean.getLocationy(), 0, 0);
                        child.setLayoutParams(params);
                        break;
                    }
                }
            }

            @Override
            public void moveViewTop(View child, int top, int dy) {
                for (int i = 0; i < mTopPlayer.size(); i++) {
                    if (i == child.getId()) {
                        UserBean bean = mTopPlayer.get(i);
                        bean.setLocationy(top);

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                        params.setMargins(bean.getLocationx(), bean.getLocationy(), 0, 0);
                        child.setLayoutParams(params);
                        break;
                    }
                }
            }
        });
    }

    private void initD1() {
        mAllPlayers.clear();
        mAllPlayers.addAll(CommonList.mSaveCache);
        Log.d("huang", mAllPlayers.toString() + "11111111111111");
        setMovePlayers();
    }

    /**
     * 获取球员
     */
    private void initD() {
        RequestParam params = new RequestParam();
        params.put("teamId", mTeamId + "");
        params.put("orderby", "isCaptain asc,u.createTime desc");
        params.put("currentPage", "1");
        params.put("pageSize", "1000");
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listPlayer", params.toString(), new SmartCallback<UserBeanResult>() {

            @Override
            public void onSuccess(int statusCode, UserBeanResult result) {
                LogUtils.d(result.getList().toString() + "-=-=-==");

                mAllPlayers.clear();
                mAllPlayers.addAll(result.getList());

                initD1();
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, UserBeanResult.class);
    }

    /**
     * 显示移动球员的位置信息
     */
    private void setMovePlayers() {
        mTopPlayer.clear();
        mBottomPlayers.clear();

        for (UserBean bean : mAllPlayers) {
            if (bean.getLocationx() >= 1 && bean.getLocationy() >= 1) {
                mTopPlayer.add(bean);
            } else {
                mBottomPlayers.add(bean);
            }
        }
        Log.d("huang", mTopPlayer.toString() + "2222222222222222");
        Log.d("huang", mBottomPlayers.toString() + "333333333333333333");
        mPlayerAdp.notifyDataSetChanged();

        setTopView();
    }

    /**
     * 设置头部移动view
     */
    private void setTopView() {
        mMoveCustv.removeAllViews();
        if (!mTopPlayer.isEmpty()) {
            for (int i = 0; i < mTopPlayer.size(); i++) {
                UserBean bean = mTopPlayer.get(i);
                setViewPos(bean, i);
            }
        }
    }

    /**
     * 画移动view布局
     *
     * @param bean
     * @param id
     */
    private void setViewPos(final UserBean bean, final int id) {
        View mView = getLayoutInflater().inflate(R.layout.item_player_pos, null);
        mView.setId(id);
        ImageView mHeadImg = (ImageView) mView.findViewById(R.id.id_player_head_img);
        TextView mTxtName = (TextView) mView.findViewById(R.id.id_player_name_txt);
        TextView mTxtDel = (TextView) mView.findViewById(R.id.id_remove_txt);
        ImageView mDelImg = (ImageView) mView.findViewById(R.id.id_del_player_img);

        String name = bean.getName();
        String n = "";
        if (!StringUtils.isEmpty(name)) {
            if (name.length() <= 5) {
                n = name;
            } else {
                n = name.substring(0, 5);
            }
        }
        mTxtName.setText(n);
        ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(bean.getIconUrl()), mHeadImg, FootBallApplication.circOptions);
        mMoveCustv.addView(mView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mView.getLayoutParams();
        params.setMargins(bean.getLocationx(), bean.getLocationy(), 0, 0);
        mView.setLayoutParams(params);

        mTxtDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomPlayers.add(bean);
                mPlayerAdp.notifyDataSetChanged();

                mTopPlayer.remove(id);
                setTopView();
            }
        });
        mDelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomPlayers.add(bean);
                mPlayerAdp.notifyDataSetChanged();

                mTopPlayer.remove(id);
                setTopView();
            }
        });
    }

    @OnClick({R.id.layout2, R.id.layout1})
    public void onClick(View mView) {
        switch (mView.getId()) {
            case R.id.layout1:
                finish();
                break;
            case R.id.layout2:
                //保存
                if (mTopPlayer.isEmpty()) {
                    ToastUtil.show(mContext, "请编辑球员后在提交");
                    return;
                }
                commit();
                Log.d("huang", mTopPlayer.toString() + "44444444444444444");
                break;
        }
    }

    public void commit() {
        showProgress("提交中...");
        List<RequestParam> mparams = new ArrayList<>();
        for (int i = 0; i < mTopPlayer.size(); i++) {
            UserBean bean = mTopPlayer.get(i);
            RequestParam params = new RequestParam();
            params.put("id", bean.getId());
            params.put("x", bean.getLocationx());
            params.put("y", bean.getLocationy());
            mparams.add(params);
        }
        LogUtils.d(mparams.toString() + "[][][]");

//        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "player/updateCoordinate", mparams.toString(), new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                LogUtils.d(result.toString() + "[][][][][]");
                dismissProgress();
                showMsg("提交成功");

                BaseEvent event = new BaseEvent();
                event.data = IntentKey.General.KEY_REFRESH;
                EventBus.getDefault().post(event);
                finish();
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("提交失败");
            }
        }, Result.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserBean bean = mBottomPlayers.get(position);
        mTopPlayer.add(bean);
        setTopView();

        mBottomPlayers.remove(position);
        mPlayerAdp.notifyDataSetChanged();
    }
}
