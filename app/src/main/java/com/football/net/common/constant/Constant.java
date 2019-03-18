package com.football.net.common.constant;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/8/25.
 * 常量类
 */
public class
Constant {
    /** sp名 */
    public static final String SP_NAME = "nake_sp";
    /** 记住密码 */
    public static final String REMEMBER_PASSWORD = "remember_password";
    /** 记住密码 */
    public static final String AUTO_LOGIN = "auto_login";
    /** 存储用户ID键*/
    public static final String USERID = "userId";
    /** 存储用户名*/
    public static final String USERNAME = "username";
    /** 存储公司名*/
    public static final String COMP_NAME = "comp_name";
    /** 存储用户密码*/
    public static final String PASSWORD = "password";
    /** 存储登录信息*/
    public static final String LOGIN_INFO = "login_info";
    /** 单个权限申请 */
    public static final int REQUEST_PERMISSIONS = 10010;
    /** 权限申请 */
    public static final int REQUEST_M_PERMISSIONS = 10011;
    //调试模式
    public static final boolean isDebug = true;

    //调试模式
    public static final String interfaceInnorErr = "接口内部错误";

/*    public static HashMap<String,String> positionMap = new HashMap<>();
    static {
        positionMap.put("1","前场");
        positionMap.put("2","中场");
        positionMap.put("3","后场");
        positionMap.put("4","门将");
    }*/
}
