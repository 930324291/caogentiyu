package com.football.net.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.GalleryAdapter;
import com.football.net.bean.SimpleTeamBean;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.constant.IntentKey;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.UserBean3Result;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.SpaceItemDecoration;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;


import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.OnClick;

public class FabuMatchActivity extends BasicActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //    @BindView(R.id.account)
//    EditText account;
//    @BindView(R.id.verification)
//    EditText verification;
//    @BindView(R.id.verificationBtn)
//    TextView verificationBtn;
//    @BindView(R.id.password)
//    EditText password;
//    @BindView(R.id.commit)
//    TextView commit;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.titleView)
    EditText titleView;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.videoDescrip)
    EditText videoDescrip;
    @BindView(R.id.wordCount)
    TextView wordCount;
    @BindView(R.id.timeV)
    TextView timeV;

    @BindView(R.id.select1)
    ImageView select1;
    @BindView(R.id.tvselect1)
    TextView mTxtSelect1;
    @BindView(R.id.select2)
    ImageView select2;
    @BindView(R.id.teamBName)
    TextView teamBName;


    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private int isPublic = 0;
    SimpleTeamBean teamB;

    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fabu_match;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_appoint_game;
    }

    public void initView() {
        type = getIntent().getIntExtra(IntentKey.General.KEY_TYPE, -1);
        if (type != -1) {
            select1.setVisibility(View.INVISIBLE);
            mTxtSelect1.setVisibility(View.INVISIBLE);
        }
        isPublic = 1;
        select1.setSelected(true);
        videoDescrip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int num = editable.length();
                wordCount.setText(num + "/1200");
            }
        });
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
//        loadData();
    }


    int mYear, mMonth, mDay, mHourDay, mMinute;
    GregorianCalendar calendar;

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//        timeV.setText(year + "/" + (month + 1) + "/" + day);
        timePickerDialog.show(getSupportFragmentManager(), "timepicker");
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHourDay = hourOfDay;
        mMinute = minute;
        calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHourDay);
        calendar.set(Calendar.MINUTE, mMinute);
        timeV.setText(CommonUtils.getDateStr(calendar.getTimeInMillis(), "yyyy-MM-dd HH:mm"));
    }

    @OnClick({R.id.commit, R.id.timeV, R.id.select1, R.id.tvselect1, R.id.select2, R.id.tvselect2, R.id.teamBName})
    public void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commit:
                String titleStr = titleView.getText().toString();
                if (TextUtils.isEmpty(titleStr)) {
                    showMsg("请输入赛事名称");
                    return;
                }
                String addressStr = address.getText().toString();
                if (TextUtils.isEmpty(addressStr)) {
                    showMsg("请输入比赛地点");
                    return;
                }
                if (calendar == null) {
                    showMsg("请选择比赛时间");
                    return;
                }
                String content = videoDescrip.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showMsg("请输入备注信息");
                    return;
                }
                if (isPublic == 0) {
                    showMsg("请选择权限");
                    return;
                }
                if (isPublic == 2) {
                    if (teamB == null) {
                        showMsg("请选择球队");
                        return;
                    }
                }
                commit(titleStr, content, addressStr, calendar.getTimeInMillis());
                break;
            case R.id.timeV:
                datePickerDialog.show(getSupportFragmentManager(), "datepicker");
                break;
            case R.id.select1:
            case R.id.tvselect1:
                if (!select1.isSelected()) {
                    select1.setSelected(true);
                    select2.setSelected(false);
                    teamBName.setVisibility(View.GONE);
                    isPublic = 1;
                }
                break;
            case R.id.select2:
            case R.id.tvselect2:
                if (!select2.isSelected()) {
                    select2.setSelected(true);
                    select1.setSelected(false);
                    teamBName.setVisibility(View.VISIBLE);
                    isPublic = 2;
                }
                break;
            case R.id.teamBName:
                Intent intent = new Intent(this, SelectTeamActivity.class);
                startActivityForResult(intent, 10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (null == data) {
                return;
            }
            teamB = (SimpleTeamBean) data.getSerializableExtra("SimpleTeamBean");
            teamBName.setText(teamB.getTeamTitle());
        }
    }

    //GET http://47.89.46.215/app/field/search?name=&pageSize=10 HTTP/1.1
//    public void loadData( ) {
//        String teamId = "";
//        if(FootBallApplication.userbean.getTeam() != null){
//            teamId = FootBallApplication.userbean.getTeam().getId()+"";
//        }
//
////        RequestParam params = new RequestParam();
//        SmartParams params = new SmartParams();
//        params.put("name", "");
//        params.put("pageSize", 20);
//        //3.请求数据
//        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "field/search", params, new SmartCallback<UserBean3Result>() {
//
//            @Override
//            public void onSuccess(int statusCode, UserBean3Result result) {
//                if(result.isSuccess()){
//                    mDatas.addAll(result.getData());
//                }
//                if(mDatas.size() > 0){
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, String message) {
//            }
//
//        }, UserBean3Result.class);
//    }

    public void commit(final String title, final String content, String address, long beginTime) {

        {
            //{"messageType":1,"checkAll":true,"title":"aaaa","content":"aaaaaa","tanks":[{"player":{"id":89}}],"team":{"id":17},"isEnabled":1}
            //{"messageType":2,"checkAll":true,"title":"签到","content":"签到","address":"老地方","beginTime":1490192280000,"tanks":[{"player":{"id":89}},{"player":{"id":75}}],"team":{"id":17},"isEnabled":1}
            showProgress("提交中...");
            RequestParam params = new RequestParam();
            params.put("title", title);
            params.put("address", address);
            params.put("beginTime", beginTime);
            params.put("isPublic", isPublic);
            params.put("content", content);
            if (FootBallApplication.userbean.getTeam() != null) {
                params.put("teamType", FootBallApplication.userbean.getTeam().getTeamType());
            }
            params.put("isEnabled", 1);

            params.put("checkAll", true);
            params.put("teamA", FootBallApplication.userbean.getTeam());
            if (isPublic == 2 && teamB != null) {
                params.put("teamB", teamB);
            }
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "game/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

                @Override
                public void onSuccess(int statusCode, Result result) {
                    dismissProgress();
                    if (!result.isSuccess()) {
                        dismissProgress();
                        showMsg(Constant.interfaceInnorErr);
                        return;
                    }
                    showMsg("提交成功！");
                    finish();


                }

                @Override
                public void onFailure(int statusCode, String message) {
                    dismissProgress();
                    if (!TextUtils.isEmpty(message)) {
                        showMsg(message);
                    } else {
                        showMsg("提交失败！");
                    }
                }

            }, Result.class);
        }
    }
}
