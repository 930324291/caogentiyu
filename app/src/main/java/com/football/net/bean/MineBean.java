package com.football.net.bean;

/**
 * Created by footman on 2017/1/11.
 */

public class MineBean {
    private int beanType = 1; //  作为队员 1是图片 2是视频  3是
                            //
public static final  int CapterAllList_Type_Recruit =1;
public static final  int CapterAllList_Type_SigninMessage =2;
public static final  int CapterAllList_Type_innerMessage =3;
public static final  int CapterAllList_Type_Photo =4;
public static final  int CapterAllList_Type_Video =5;
public static final  int CapterAllList_Type_Appointment =6;
public static final  int CapterAllList_Type_Score =7;

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }
}
