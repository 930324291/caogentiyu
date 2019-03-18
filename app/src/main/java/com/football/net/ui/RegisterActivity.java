package com.football.net.ui;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.football.net.R;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.util.CommonUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.SmartParams;
import com.football.net.http.reponse.Result;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewById(R.id.account)
    EditText account;
    @ViewById(R.id.verification)
    EditText verification;
    @ViewById(R.id.verificationBtn)
    TextView verificationBtn;
    @ViewById(R.id.userName)
    EditText userName;
    @ViewById(R.id.password)
    EditText password;
    @ViewById(R.id.register)
    TextView register;

    String accountNum,accountName,psw, verifyCode;
    private int time;

    @Click({R.id.verificationBtn, R.id.register})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.verificationBtn:
                accountNum = account.getText().toString();
                if (accountNum == null || accountNum.length() != 11) {
                    showMsg("请输入正确的手机号");
                    return;
                }
                time =80;
                handler.sendEmptyMessage(100);
                verificationBtn.setEnabled(false);
                checkUnique(accountNum);
                break;
            case R.id.register:
                checkInput();
                break;

        }
    }

    void checkInput() {

        accountNum = account.getText().toString();
        if (accountNum == null || accountNum.length() != 11) {
            showMsg("请输入正确的手机号");
            return;
        }

        verifyCode = verification.getText().toString().trim();
        if (verifyCode.length() != 6) {
            showMsg("验证码错误");
            return;
        }

        accountName = userName.getText().toString();
        if(TextUtils.isEmpty(accountName)){
            showMsg("请输入用户名");
            return;
        }
        psw = password.getText().toString();
        if (psw.length() >= 6 && psw.length() <= 18) {

        } else {
            showMsg("密码长度为6-18位");
            return;
        }
        showProgress("注册中....");
        checkVerifyCode(accountNum,verifyCode);

    }

    Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            Object data = msg.obj;
            switch (what) {
                case 100:
                    if (time <= 0) {
                        removeCallbacksAndMessages(null);
                        verificationBtn.setEnabled(true);
//                        verificationBtn.setTextColor(Color.parseColor("#2a929f"));
                        verificationBtn.setText("重新发送");
                    } else {
                        verificationBtn.setText(time-- + "s");
                        sendEmptyMessageDelayed(100, 1000);
                    }
                    break;
            }

        }
    };

    //POST http://47.89.46.215:8181/app/player/checkUnique?mobile=15072413852 HTTP/1.1
    public void checkUnique(final String phone) {
//        RequestParam params = new RequestParam();
//        SmartParams params = new SmartParams();
//        params.put("mobile", phone);
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "player/checkUnique?mobile="+phone, "", new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                if(result.isUnique()){
                    getVerifyCode(accountNum);
                }else{
                    showMsg("此号码已注册！");
                    handler.removeCallbacksAndMessages(null);
                    verificationBtn.setEnabled(true);
                    verificationBtn.setText("重新发送");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                handler.removeCallbacksAndMessages(null);
                verificationBtn.setEnabled(true);
                verificationBtn.setText("重新发送");
            }

        }, Result.class);
    }
    //获取验证码GET http://47.89.46.215:8181/app/player/sendSms?mobile=15072413852 HTTP/1.1
    public void getVerifyCode(final String phone) {
//        RequestParam params = new RequestParam();
        SmartParams params = new SmartParams();
        params.put("mobile", phone);
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "player/sendSms", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                if(result.isSuccess()){
                    handler.removeCallbacksAndMessages(null);
                    verificationBtn.setText("发送成功");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                handler.removeCallbacksAndMessages(null);
                verificationBtn.setEnabled(true);
                verificationBtn.setText("重新发送");
            }

        }, Result.class);
    }

    //判断验证码是否正确
    public void checkVerifyCode(final String phone,String verifyCode) {
//        RequestParam params = new RequestParam();
        SmartParams params = new SmartParams();
        params.put("mobile", phone);
        params.put("verifyCode", verifyCode);
        //3.请求数据
        new SmartClient(this).get(HttpUrlConstant.APP_SERVER_URL + "player/checkVerifyCode", params, new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                if(result.isSuccess()){
                    register(phone,accountName,psw);
                }else{
                    dismissProgress();
                    showMsg("验证码错误，请重试！");
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("注册失败，请重试！");
            }

        }, Result.class);
    }

    //判断验证码是否正确   GET http://47.89.46.215:8181/app/player/changePassword?mobile=15072413852&password=123456 HTTP/1.1
    public void register(final String phone,String name,String nwePw) {
        RequestParam params = new RequestParam();
//        SmartParams params = new SmartParams();
        params.put("mobile", phone);
        params.put("name", name);
        params.put("verifyCode", verifyCode);
        params.put("password", nwePw);
        //3.请求数据
        new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "player/register", params.toString(), new SmartCallback<Result>() {

            @Override
            public void onSuccess(int statusCode, Result result) {
                dismissProgress();
                if(result.isSuccess()){
                    showMsg("注册成功！");
                    finish(); // 注册成功后返回到登录界面
                }else{
                    if(!TextUtils.isEmpty(result.getError())){
                        showMsg(result.getError());
                    }else{
                        showMsg("注册失败！");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String message) {
                dismissProgress();
                showMsg("注册错误，请重试！");
            }

        }, Result.class);
    }
}
