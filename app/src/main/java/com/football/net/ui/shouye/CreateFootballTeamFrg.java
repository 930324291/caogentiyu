package com.football.net.ui.shouye;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseFragment;
import com.football.net.manager.BasicActivity;
import com.football.net.manager.FootBallApplication;
import com.football.net.ui.DuihuiActivity;
import com.football.net.ui.StartActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hq on 2018/1/13.
 */
public class CreateFootballTeamFrg extends BaseFragment {

    @BindView(R.id.title)
    TextView textView;
    @BindView(R.id.right_txt)
    TextView textRight;
    @BindView(R.id.returnBtn)
    ImageView returnBtn;

    @BindView(R.id.sp_1)
    Spinner sp;
    @BindView(R.id.teamName)
    EditText teamName;
    @BindView(R.id.videoDescrip)
    EditText videoDescrip;
    @BindView(R.id.wordCount)
    TextView wordCount;
    @BindView(R.id.image1)
    ImageView image1;

    int teamType = 0;
    String url = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_football_team;
    }

    @Override
    protected void initView() {
        textView.setText("通知");
        textRight.setVisibility(View.GONE);
        returnBtn.setVisibility(View.GONE);
        initSpinner();
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
    }

    @OnClick({R.id.commitBtn,R.id.image1})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commitBtn:
                process();
                break;
            case R.id.image1:
                Intent intent = new Intent(getActivity(), DuihuiActivity.class);
                startActivityForResult(intent,10);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            url = data.getStringExtra("url");
            ImageLoader.getInstance().displayImage(HttpUrlConstant.SERVER_URL+CommonUtils.getRurl(url), image1, FootBallApplication.options);
        }
    }

    private void process() {

        String content = videoDescrip.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showMsg("内容不能为空！");
            return;
        }
        String name = teamName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showMsg("球队名称不能为空！");
            return;
        }

        if (TextUtils.isEmpty(url)) {
            showMsg("请选择球队队徽！");
            return;
        }

        if (teamType == 0) {
            showMsg("请选择赛制！");
        }
        showProgress("创建中...");
        dealHttp(name);
    }

//{"acRate":1,"teamTitle":"taafdfadfdd","teamType":5,"iconUrl":"{\"3\":\"/formalPic/logo/20161024011122245-304x350.jpg\",\"2\":\"/formalPic/logo/20161024011122245-304x350.jpg\",\"1\":\"/formalPic/logo/20161024011122245-55x64.jpg\"}","register":89}

    public void dealHttp(final String teamTitle) {

        {
            RequestParam params = new RequestParam();
            params.put("acRate", 1);
            params.put("teamTitle", teamTitle);
            params.put("teamType", teamType);
            params.put("iconUrl", url);
            params.put("register", FootBallApplication.userbean.getId());
            //3.请求数据
            new SmartClient(getActivity()).post(HttpUrlConstant.APP_SERVER_URL + "team/register", params.toString(), new SmartCallback<Result>() {

                @Override
                public void onSuccess(int statusCode, Result result) {
                    dismissProgress();
                    if (!result.isSuccess()) {
                        showMsg(Constant.interfaceInnorErr);
                        return;
                    }
                    showMsg("提交成功！");
                    Intent intent = new Intent(getActivity(),StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                    finish();
                }

                @Override
                public void onFailure(int statusCode, String message) {
                    dismissProgress();
                    showMsg(message);
                }

            }, Result.class);
        }
    }

    private void initSpinner(){
        String[] spTypes = new String[] {"请选择","3人制","5人制","7人制","11人制"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spTypes);
        typeAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sp.setAdapter(typeAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                tv.setTextColor(UIUtils.getResources().getColor(R.color.black));
                switch (position) {
                    case 0:
                        teamType = 0;
                        break;
                    case 1:
                        teamType = 3;
                        break;
                    case 2:
                        teamType = 5;
                        break;
                    case 3:
                        teamType = 7;
                        break;
                    case 4:
                        teamType = 11;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
