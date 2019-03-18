package com.football.net.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.football.net.R;
import com.football.net.common.constant.HttpUrlConstant;
import com.football.net.http.SmartCallback;
import com.football.net.http.SmartClient;
import com.football.net.http.reponse.impl.LoginResult;
import com.football.net.http.request.RequestParam;
import com.football.net.manager.BaseActivity;
import com.football.net.manager.FootBallApplication;

/**
 * 定义一个Activity
 */
public class StartActivity extends BaseActivity {

    int tryTimes = 0;
    long stattime;

    //onCreate 方法
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        SharedPreferences shareprefer = getSharedPreferences("proference", Context.MODE_PRIVATE);
        String username = shareprefer.getString("username", "");
        String passwordStr = shareprefer.getString("password", "");
        boolean isautologin = shareprefer.getBoolean("isautologin",true);
        if(!isautologin){
            jumpToLoginActivity();
        }else{
            if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(passwordStr)){
                tryTimes ++;
                login(username,passwordStr);
            }else{
             /*   new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpToLoginActivity();
                    }
                },2000);*/
                jumpToLoginActivity();
            }
            stattime = System.currentTimeMillis();
        }
    }

    private void jumpToLoginActivity(){
        Intent intent0 = new Intent(StartActivity.this,LoginActivity.class);
        startActivity(intent0);
        finish();
    }

    private void loginRetry(final String preaccount, final String password){
        if(tryTimes < 2){
            tryTimes++;
            login(preaccount,password);
        }else{
            jumpToLoginActivity();
        }
    }

    /**
     * 加载数据，加载完成后在回调方法中刷新相应界面
     */
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
                        loginRetry(preaccount,password);
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

                    long gap = System.currentTimeMillis() - stattime;
                    if( gap >= 350){
                        Intent intent0 = new Intent(StartActivity.this,MainActivity.class);
                        startActivity(intent0);
                        finish();
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent0 = new Intent(StartActivity.this,MainActivity.class);
                                startActivity(intent0);
                                finish();
                            }
                        },350-gap);
                    }


                }

                @Override
                public void onFailure(int statusCode, String message) {
                    loginRetry(preaccount,password);
                }

            }, LoginResult.class);
        }
    }

}
