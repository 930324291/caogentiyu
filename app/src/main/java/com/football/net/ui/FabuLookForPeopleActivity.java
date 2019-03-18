package com.football.net.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_fabu_look_for_people)
public class FabuLookForPeopleActivity extends BaseActivity {
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
    @ViewById(R.id.wordCount)
    TextView wordCount;
    @ViewById(R.id.atitle)
    EditText atitle;
    @ViewById(R.id.acontent)
    EditText acontent;

    @AfterViews
    void initView() {
        title.setText("招人");
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

        }
    }

    private void process() {
        String accountStr = atitle.getText().toString();
        String passwordStr = acontent.getText().toString();

        if (TextUtils.isEmpty(accountStr)) {
            showMsg("标题不能为空！");
            return;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            showMsg("内容不能为空！");
            return;
        }

        //发送登录请求事件
        showProgress("提交中....");
        dealHttp(accountStr, passwordStr);
    }

    public void dealHttp(final String param1, final String param2) {

        {
            RequestParam params = new RequestParam();
            params.put("title", param1);
//            params.put("isPublic", 1);
            params.put("password", param2);
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "recruit/saveOrUpdate", params.toString(), new SmartCallback<Result>() {

                @Override
                public void onSuccess(int statusCode, Result result) {
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
