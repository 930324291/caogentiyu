package com.football.net.ui;

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
import android.widget.Toast;

import com.football.net.R;
import com.football.net.bean.ApplyBean;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.reponse.impl.ApplyBeanResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;

@EActivity(R.layout.activity_fabu_look_for_team)
public class FabuLookForTeamActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.wordCount)
    TextView wordCount;
    @ViewById(R.id.atitle)
    EditText atitle;
    @ViewById(R.id.acontent)
    EditText acontent;
    @ViewById(R.id.sp_1)
    Spinner sp;
//    @ViewById(R.id.select1)
//    ImageView select1;

    int gameStyle = 0;
    int isPublic = 2;

    ApplyBean bean;

    @AfterViews
    void initView() {
        title.setText("找队");
        gameStyleSelect();
        acontent.addTextChangedListener(new TextWatcher() {
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
        loadData();
    }

    private void gameStyleSelect(){
        String[] spTypes = new String[] {"赛制","3人制","5人制","7人制","11人制"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,spTypes);
        typeAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sp.setAdapter(typeAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                if(position == 0){
                    tv.setTextColor(UIUtils.getResources().getColor(R.color.txt_787878));
                }else{
                    tv.setTextColor(UIUtils.getResources().getColor(R.color.black));
                }
                switch (position) {
                    case 0:
                        gameStyle = 0;
                        break;
                    case 1:
                        gameStyle = 3;
                        break;
                    case 2:
                        gameStyle = 5;
                        break;
                    case 3:
                        gameStyle = 7;
                        break;
                    case 4:
                        gameStyle = 11;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Click({R.id.commit})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.commit:
                process();
                break;
//            case R.id.selectView:
//               if(select1.isSelected()){
//                   select1.setSelected(false);
//                   isPublic = 2;
//               }else{
//                   select1.setSelected(true);
//                   isPublic = 1;
//               }
//                break;
        }
    }

    private void process() {
        String titleStr = atitle.getText().toString();
        String contentStr = acontent.getText().toString();

        if (TextUtils.isEmpty(titleStr)) {
            showMsg("标题不能为空！");
            return;
        }
        if (TextUtils.isEmpty(contentStr)) {
            showMsg("内容不能为空！");
            return;
        }
        if(gameStyle == 0){
            showMsg("请选择赛制！");
            return;
        }

        Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT);
        finish();
        //发送登录请求事件
//        showProgress("提交中....");
//        dealHttp(titleStr, contentStr);
    }

    public void dealHttp(final String param1, final String param2) {

        {
            RequestParam params = new RequestParam();
            params.put("title", param1);
            params.put("content", param2);
            params.put("isPublic", isPublic);
            params.put("applyTime", System.currentTimeMillis());
            params.put("dreamType", gameStyle);
            if(bean != null){
                params.put("id",bean.getId());
            }
            params.put("player",FootBallApplication.userbean);
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "apply/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

                @Override
                public void onSuccess(int statusCode, Result result) {
                    if (!result.isSuccess()) {
                        dismissProgress();
                        showMsg(Constant.interfaceInnorErr);
                        return;
                    }
                    showMsg("提交成功！");
//                    Intent intent = new Intent(FabuLookForTeamActivity.this, FindTeaamDetailActivity.class);
//                    intent.putExtra("beandata",bean);
//                    startActivity(intent);
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

    public void loadData() {
        {
            showProgress("加载中....");
            RequestParam params = new RequestParam();
            params.put("isEnabled", 1);
            params.put("isOpen", 1);
            params.put("orderby", "applyTime desc");
            params.put("playerId=",FootBallApplication.userbean.getId());
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "listApply", params.toString(), new SmartCallback<ApplyBeanResult>() {

                @Override
                public void onSuccess(int statusCode, ApplyBeanResult result) {
                    dismissProgress();
                    ArrayList<ApplyBean> applyList = result.getList();
                    if(applyList != null && applyList.size() > 0){
                        bean = applyList.get(0);
                        atitle.setText(bean.getTitle());
                        acontent.setText(bean.getContent());
                        if(bean.getIsPublic() == 1){
                            isPublic = 1;
//                            select1.setSelected(true);
                        }
                        String gameType = bean.getDreamType();
                        if("3".equals(gameType)){
                            sp.setSelection(1,true);
                        }else  if("5".equals(gameType)){
                            sp.setSelection(2,true);
                        }else  if("7".equals(gameType)){
                            sp.setSelection(3,true);
                        }else if("11".equals(gameType)){
                            sp.setSelection(4,true);
                        }
                    }

                }

                @Override
                public void onFailure(int statusCode, String message) {
                    dismissProgress();
                    showMsg(message);
                }

            }, ApplyBeanResult.class);
        }
    }

}
