package com.football.net.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.bean.MessageBean;
import com.football.net.bean.TankInMessageBean;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.MessageBeanDataResult;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.NoScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Andy Rao on 2017/1/11.
 */
public class InformSignDetailActivity extends BasicActivity {
    @BindView(R.id.rootLayout)
    View rootLayout;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.detail)
    TextView detail;

    @BindView(R.id.askSignNumtv)
    TextView askSignNumtv;

    @BindView(R.id.askLeaveNumtv)
    TextView askLeaveNumtv;

    @BindView(R.id.noAskNumtv)
    TextView noAskNumtv;

    @BindView(R.id.signin)
    View commitButton;

    @BindView(R.id.recyclerview1)
    NoScrollGridView mRecyclerView1; //已签

    @BindView(R.id.recyclerview2)
    NoScrollGridView mRecyclerView2; //未签

    @BindView(R.id.recyclerview3)
    NoScrollGridView mRecyclerView3; //请假

    MyAdapter mAskSignAdapter;
    MyAdapter mNoAskAdapter;
    MyAdapter mAckLeaveAdapter;

    List<TankInMessageBean> tanksAskSignPlayer = new ArrayList<>();
    List<TankInMessageBean> tanksNoAskPlayer = new ArrayList<>();
    List<TankInMessageBean> tanksAskLeaveAllPlayer = new ArrayList<>();

    MessageBean bean;
    boolean ifHideBottomButton;

    @Override
    public int getLayoutId() {
        return R.layout.activity_inform_sign_detial;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_inform_sign_detial;
    }

    @Override
    protected void initView() {
        rootLayout.setVisibility(View.INVISIBLE);
        int beanId = getIntent().getIntExtra("beanId", 0);
        ifHideBottomButton = getIntent().getBooleanExtra("ifHideBottomButton", false);
        //设置适配器
        mAskSignAdapter = new MyAdapter(this, tanksAskSignPlayer);
        mRecyclerView1.setAdapter(mAskSignAdapter);

        mNoAskAdapter = new MyAdapter(this, tanksNoAskPlayer);
        mRecyclerView2.setAdapter(mNoAskAdapter);

        mAckLeaveAdapter = new MyAdapter(this, tanksAskLeaveAllPlayer);
        mRecyclerView3.setAdapter(mAckLeaveAdapter);

        loaddata(beanId);

        if (ifHideBottomButton) {
            commitButton.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.signin})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.signin:
                Intent intent = new Intent(this, InformSignOnTheSpotActivity.class);
                intent.putExtra("itemBean", bean);
                startActivityForResult(intent,bean.getId());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == bean.getId())
        {
            if(resultCode == RESULT_CANCELED)
            {
                System.out.println("RESULT_CANCELED");
                loaddata(requestCode);
            }
        }
    }

    public void loaddata(int beanId) {

        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "message/" + beanId, null, new SmartCallback<MessageBeanDataResult>() {

            @Override
            public void onSuccess(int statusCode, MessageBeanDataResult result) {
                rootLayout.setVisibility(View.VISIBLE);
                bean = result.getData();
                timeTv.setText(CommonUtils.getFullTime(bean.getBeginTime()));
                address.setText(bean.getAddress());
                detail.setText(bean.getContent());

                // 在活动时间未到的情况下，是不可以点击现场签到按钮的
                if (bean.getBeginTime()>System.currentTimeMillis()) {
                    commitButton.setVisibility(View.GONE);
                } else {
                    commitButton.setVisibility(View.VISIBLE);
                }

                List<TankInMessageBean> tanks = bean.getTanks();

                if (tanks != null) {
                    tanksNoAskPlayer.clear();
                    tanksAskSignPlayer.clear();
                    tanksAskLeaveAllPlayer.clear();

                    for (TankInMessageBean tankbean : tanks) {

                        if (tankbean.getConfirmStatus()==null) {
                            tanksNoAskPlayer.add(tankbean);
                            mNoAskAdapter.notifyDataSetChanged();

                        } else {
                            if (tankbean.getConfirmStatus() == 1) {
                                tanksAskSignPlayer.add(tankbean);
                                mAskSignAdapter.notifyDataSetChanged();
                            }

                            if (tankbean.getConfirmStatus() == 2) {
                                tanksAskLeaveAllPlayer.add(tankbean);
                                mAckLeaveAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    bean.setSignTanks(tanksAskSignPlayer);

                    askSignNumtv.setText("签到：" + tanksAskSignPlayer.size() + "人");
                    noAskNumtv.setText("未签：" + tanksNoAskPlayer.size() + "人");
                    askLeaveNumtv.setText("请假：" + tanksAskLeaveAllPlayer.size() + "人");

                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, MessageBeanDataResult.class);
    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater inflater;
        List<TankInMessageBean> dataList;

        public MyAdapter(Context context, List<TankInMessageBean> dataList) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fabu_signin_message_recycler_item, null);
                vHolder = new ViewHolder();

                vHolder.mImg = (ImageView) convertView.findViewById(R.id.image);
                vHolder.mcView = (ImageView) convertView.findViewById(R.id.mcView);
                vHolder.mTxt = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }
            if (dataList.get(position).getPlayer() != null) {
                ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL + CommonUtils.getRurl(dataList.get(position).getPlayer().getIconUrl()), vHolder.mImg, FootBallApplication.circOptions);
                vHolder.mTxt.setText(dataList.get(position).getPlayer().getName());
                vHolder.mImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FootBallApplication.APPLacationRole == FootBallApplication.ROLE_TeamMember) {
                            Intent intent = new Intent(mContext, PlayerDetialActivity.class);
                            intent.putExtra("beanid", dataList.get(position).getPlayer().getId());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PlayerDetial4CaptainActivity.class);
                            intent.putExtra("beanid", dataList.get(position).getPlayer().getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            } else {
                ImageLoader.getInstance().displayImage("", vHolder.mImg, FootBallApplication.circOptions);
                vHolder.mTxt.setText("暂无");
            }
            return convertView;
        }


    }

    public static class ViewHolder {
        ImageView mImg, mcView;
        TextView mTxt;
    }


}
