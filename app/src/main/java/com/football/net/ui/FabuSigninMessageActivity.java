package com.football.net.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.adapter.GalleryAdapter;
import com.football.net.bean.UserBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.ScoreListBeanResult;
import com.football.net.http.reponse.impl.UserBean2Result;
import com.football.net.http.reponse.impl.UserBean3Result;
import com.football.net.http.reponse.impl.UserBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.interFace.OnItemClickListener;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.widget.SpaceItemDecoration;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@EActivity(R.layout.activity_fabu_siginin_message)
public class FabuSigninMessageActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener  {
    //    @ViewById(R.id.account)
//    EditText account;
//    @ViewById(R.id.verification)
//    EditText verification;
//    @ViewById(R.id.verificationBtn)
//    TextView verificationBtn;
//    @ViewById(R.id.password)
//    EditText password;
//    @ViewById(R.id.commit)
//    TextView commit;
    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.titleView)
    EditText titleView;
    @ViewById(R.id.address)
    EditText address;
    @ViewById(R.id.videoDescrip)
    EditText videoDescrip;
    @ViewById(R.id.wordCount)
    TextView wordCount;
    @ViewById(R.id.timeV)
    TextView timeV;

    @ViewById(R.id.select1)
    ImageView select1;

    @ViewById(R.id.recyclerview)
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    ArrayList<UserBean> mDatas = new ArrayList<UserBean>();
    HashSet<Integer> selectedSet = new HashSet<>();

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    @AfterViews
    void initView() {
        title.setText("签到信");
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
                wordCount.setText(num+"/1200");
            }
        });
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dip14);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //设置适配器
        mAdapter = new GalleryAdapter(this, mDatas,selectedSet);
        mAdapter.setmOnitemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(selectedSet.contains(position)){
                    selectedSet.remove(position);
                }else {
                    selectedSet.add(position);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        loadData();
    }




    int mYear,mMonth,mDay,mHourDay,mMinute;
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
        calendar.set(Calendar.YEAR,mYear);
        calendar.set(Calendar.MONTH,mMonth);
        calendar.set(Calendar.DAY_OF_MONTH,mDay);
        calendar.set(Calendar.HOUR_OF_DAY,mHourDay);
        calendar.set(Calendar.MINUTE,mMinute);
        timeV.setText(CommonUtils.getDateStr(calendar.getTimeInMillis(),"yyyy-MM-dd HH:mm"));
    }

    @Click({R.id.commit,R.id.timeV,R.id.selectView})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commit:
                String titleStr = titleView.getText().toString();
                if(TextUtils.isEmpty(titleStr)){
                    showMsg("请输入标题");
                    return;
                }
                String addressStr = address.getText().toString();
                if(TextUtils.isEmpty(addressStr)){
                    showMsg("请输入活动地点");
                    return;
                }
                if( calendar == null){
                    showMsg("请选择活动时间");
                    return;
                }
                String content = videoDescrip.getText().toString();
                if(TextUtils.isEmpty(content)){
                    showMsg("请输入内容");
                    return;
                }
                if(selectedSet.size() ==0){
                    showMsg("请至少选择一个球员");
                    return;
                }
                commit(titleStr,content,addressStr,calendar.getTimeInMillis());
                break;
            case R.id.timeV:
                datePickerDialog.show(getSupportFragmentManager(), "datepicker");
                break;
            case R.id.selectView:
                if(select1.isSelected()){
                    select1.setSelected(false);
                    selectedSet.clear();
                    mAdapter.notifyDataSetChanged();
                }else{
                    select1.setSelected(true);
                    for(int i = 0; i < mDatas.size();i++){
                        selectedSet.add(i);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;

        }
    }

    //GET http://47.89.46.215:8181/app/findPlayersByTeam?isCaptain=false&teamId=17 HTTP/1.1
    public void loadData( ) {
        String teamId = "";
        if(FootBallApplication.userbean.getTeam() != null){
            teamId = FootBallApplication.userbean.getTeam().getId()+"";
        }

//        RequestParam params = new RequestParam();
        SmartParams params = new SmartParams();
        params.put("isCaptain", false);
        params.put("teamId", teamId);
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "findPlayersByTeam", params, new SmartCallback<UserBean3Result>() {

            @Override
            public void onSuccess(int statusCode, UserBean3Result result) {
                if(result.isSuccess()){
                    mDatas.addAll(result.getData());
                }
                if(mDatas.size() > 0){
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
            }

        }, UserBean3Result.class);
    }

    public void commit(final String title, final String content,String address,long beginTime) {

        {
            //{"messageType":1,"checkAll":true,"title":"aaaa","content":"aaaaaa","tanks":[{"player":{"id":89}}],"team":{"id":17},"isEnabled":1}
            //{"messageType":2,"checkAll":true,"title":"签到","content":"签到","address":"老地方","beginTime":1490192280000,"tanks":[{"player":{"id":89}},{"player":{"id":75}}],"team":{"id":17},"isEnabled":1}
           showProgress("提交中...");
            RequestParam params = new RequestParam();
            params.put("messageType", 2);
            params.put("title", title);
            params.put("content", content);
            params.put("address", address);
            params.put("beginTime", beginTime);
            params.put("checkAll", true);
            params.put("isEnabled", 1);
            HashMap team = new HashMap();
            team.put("id", FootBallApplication.userbean.getTeam().getId());
            params.put("team", team);
            List<HashMap> tanks = new ArrayList<>();
            for(Integer bean:selectedSet){
                if(bean >= 0){
                    HashMap<String,HashMap> player = new HashMap<>();
                    HashMap<String,String> idMap = new HashMap<>();
                    idMap.put("id",mDatas.get(bean).getId());
                    player.put("player",idMap);
                    tanks.add(player);
                }
            }
            params.put("tanks", tanks);
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "message/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

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
                    showMsg(message);
                }

            }, Result.class);
        }
    }
}