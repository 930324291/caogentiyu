package com.football.net.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.football.net.R;
import com.football.net.common.constant.Constant;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.common.fileIo.SharePref;
import com.football.net.common.util.CommonUtils;
import com.football.net.common.util.UIUtils;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.LoginResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 定义一个Activity，使用androidannotations注解
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewById(R.id.sp_1)
    Spinner sp;
    @ViewById(R.id.account)
    EditText account;
    @ViewById(R.id.password)
    EditText password;
    @ViewById(R.id.login)
    TextView login;
    @ViewById(R.id.register)
    TextView register;
    @ViewById(R.id.forgetPW)
    TextView forgetPW;
    int times =0;

    //androidannotations注解, 初始化view
    @AfterViews
    void initView(){
        roleSelect();
//        account.setText("15072413852");
//        account.setText("13585918777");
//        password.setText("111111");
        SharedPreferences shareprefer = getSharedPreferences("proference", Context.MODE_PRIVATE);
        String username = shareprefer.getString("username","");
        String passwordStr = shareprefer.getString("password","");
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(passwordStr)){
            account.setText(username);
            password.setText(passwordStr);
        }
    }

    //androidannotations注解，点击事件
    @Click({R.id.login, R.id.register, R.id.forgetPW})
    void onClick(View v) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.login:
                if(Constant.isDebug){
                    if(times == 0){
                        Intent intent0 = new Intent(this,MainActivity_.class);
                        this.startActivityForResult(intent0,11);
                        times ++;
                    }
                }else{
                    proc_userlogon();
                }

                break;
            case R.id.register:
                Intent intent = new Intent(this,RegisterActivity_.class);
                this.startActivity(intent);
                break;
            case R.id.forgetPW:
                Intent intent2 = new Intent(this,ForgetPWActivity_.class);
                this.startActivity(intent2);
                break;
        }
    }

    private void proc_userlogon() {
        String accountStr = account.getText().toString();
        String passwordStr = password.getText().toString();

        if (TextUtils.isEmpty(accountStr)) {
            Toast.makeText(getApplicationContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("proference", Context.MODE_PRIVATE);
        String oldAccoutn = sharedPreferences.getString("username", "");
        if(!accountStr.equals(oldAccoutn)){
            SharePref.setLastReadTime(this,0);
        }
        //发送登录请求事件
        showProgress("登录中....");
        login(accountStr, passwordStr);
    }

    //网络请求接口，调用公共类SmartClient
    public void login(final String preaccount, final String password) {

        {
            RequestParam params = new RequestParam();
            params.put("account", preaccount);
            params.put("password", password);
            //3.请求数据
            new SmartClient(this).post(HttpUrlConstant.APP_SERVER_URL + "login", params.toString(), new SmartCallback<LoginResult>() {

                @Override
                public void onSuccess(int statusCode, LoginResult result) {
                    if (!result.isSuccess()) {
                        dismissProgress();
                        showMsg("登录失败，请重试");
                        return;
                    }
                    FootBallApplication.access_token = result.getAccess_token();

                    // 获取用户的点赞集合
                    FootBallApplication.teamLikes = result.getTeamLikes();
                    FootBallApplication.playerLikes = result.getPlayerLikes();

                    // 获取系统参数
                    FootBallApplication.like_player_max = result.getLike_player_max();
                    FootBallApplication.like_team_max = result.getLike_team_max();
                    FootBallApplication.dare_max = result.getDare_max();
                    FootBallApplication.create_team_max = result.getCreate_team_max();


                    FootBallApplication.userbean = result.getUser();
                    FootBallApplication.APPLacationRole =  FootBallApplication.userbean.getIsCaptain();
                    SharedPreferences.Editor editor = getSharedPreferences("proference", Context.MODE_PRIVATE).edit();
                    editor.putString("username", preaccount);
                    editor.putString("password", password);
                    editor.commit();
                    SharePref.setIsautoLoging(LoginActivity.this, true);
                    Intent intent0 = new Intent(LoginActivity.this,MainActivity_.class);
//                    startActivityForResult(intent0,11);
                    startActivity(intent0);
                    finish();
//                    //存储用户id
//                    CommonUtils.cacheStringData(Constant.USERID, result.getData().getUserid());
//                    //存储用户账号和密码
//                    CommonUtils.cacheStringData("username", preaccount);
//                    try{
//                        String psw = AESEncryptor.encrypt(password);
//                        CommonUtils.cacheStringData("password", psw);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                    //存储用户信息
//                    try {
//                        SmartRecord.getInstance().getDbUtil().saveOrUpdate(result.getData());
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                    CallXApplication.getInstance().setUser(result.getData());

                }

                @Override
                public void onFailure(int statusCode, String message) {
                    dismissProgress();
                    showMsg(message);
                }

            }, LoginResult.class);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11 && resultCode ==12){
            finish();
        }
    }

    private void roleSelect(){
        String[] spTypes = new String[] {"队员","队长","初始状态"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spTypes);
        typeAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sp.setAdapter(typeAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                tv.setTextColor(UIUtils.getResources().getColor(R.color.black));
                FootBallApplication.APPLacationRole = 2;
                switch (position) {
                    case 0:
                        FootBallApplication.APPLacationRole = 2;
                        break;
                    case 1:
                        FootBallApplication.APPLacationRole = 1;
                        break;
                    case 2:
                        FootBallApplication.APPLacationRole = -1;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
