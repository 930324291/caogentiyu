package com.football.net.http.reponse.impl;

import com.football.net.http.reponse.Result;

/**
 * Created by footman on 2017/2/21.
 */

public class UnreadNumResult extends Result {

    private int unReadCnt1;   //队长 球队招人   对应 app 用户中心招人
    private int unReadCnt2;   //队长 入队申请   对应  app通知新队员
    private int unReadCnt3;   //队长 球队约战   对应 app 通知里面的约战;  队员
    private int unReadCnt8;   //队长 球员变动                          ;  队员

    private int unReadCnt4;   //队员 签到   对应 app 签到
    private int unReadCnt5;   //队员 站内信   对应
    private int unReadCnt6;   //队员 我的申请   对应
    private int unReadCnt7;   //队员 邀我入队  对应 app 招人

    public int getUnReadCnt1() {
        return unReadCnt1;
    }

    public void setUnReadCnt1(int unReadCnt1) {
        this.unReadCnt1 = unReadCnt1;
    }

    public int getUnReadCnt2() {
        return unReadCnt2;
    }

    public void setUnReadCnt2(int unReadCnt2) {
        this.unReadCnt2 = unReadCnt2;
    }

    public int getUnReadCnt3() {
        return unReadCnt3;
    }

    public void setUnReadCnt3(int unReadCnt3) {
        this.unReadCnt3 = unReadCnt3;
    }

    public int getUnReadCnt8() {
        return unReadCnt8;
    }

    public void setUnReadCnt8(int unReadCnt8) {
        this.unReadCnt8 = unReadCnt8;
    }

    public int getUnReadCnt4() {
        return unReadCnt4;
    }

    public void setUnReadCnt4(int unReadCnt4) {
        this.unReadCnt4 = unReadCnt4;
    }

    public int getUnReadCnt5() {
        return unReadCnt5;
    }

    public void setUnReadCnt5(int unReadCnt5) {
        this.unReadCnt5 = unReadCnt5;
    }

    public int getUnReadCnt6() {
        return unReadCnt6;
    }

    public void setUnReadCnt6(int unReadCnt6) {
        this.unReadCnt6 = unReadCnt6;
    }

    public int getUnReadCnt7() {
        return unReadCnt7;
    }

    public void setUnReadCnt7(int unReadCnt7) {
        this.unReadCnt7 = unReadCnt7;
    }
}
