/**
 * ID: SmartRecord.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.database;

import android.content.Context;
import android.text.TextUtils;

import com.football.net.common.fileIo.FileIoUtil;
import com.football.net.common.fileIo.StorageUtil;
import com.football.net.common.util.LogUtils;
import com.football.net.common.util.SPUtils;
import com.football.net.manager.FootBallApplication;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * File Description
 *
 * @author xiangwx
 * @version 0.1
 * @.createdate 2015年8月19日 下午4:27:51
 * @.modifydate 2015年8月19日 下午4:27:51
 * <DT><B>修改历史记录</B>
 * <DD>
 * <p/>
 * </DD>
 * </DT>
 * @since 0.1
 */
public class SmartRecord {

    private DbUtils dbUtil;
    private static SmartRecord instance;

    private SmartRecord(Context context) {
        String dbDir = StorageUtil.getAvaRoot() + FootBallApplication.DB_PATH;
        FileIoUtil.makeFolder(dbDir);
        String dbName = SPUtils.getStringData("username", FootBallApplication.DB_FILE);
        LogUtils.d("数据库名称:" + dbName);
        if (TextUtils.isEmpty(dbName)) {
            dbName = "callxdb.db";
        } else {
            dbName = dbName + ".db";
        }
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
        //配置是否在sd卡生成数据库
        if (!TextUtils.isEmpty(FootBallApplication.DB_PATH)) {
            config.setDbDir(dbDir);
        }
        config.setDbName(dbName); //db名
        config.setDbVersion(FootBallApplication.DB_VERSION);  //db版本
        config.setDbUpgradeListener(new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
                try {
                    dbUtils.dropDb();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        dbUtil = DbUtils.create(config);

    }

    public synchronized static SmartRecord getInstance() {
        if (instance == null) {
            instance = new SmartRecord(FootBallApplication.getInstance());
        }
        return instance;
    }
    public synchronized static void reset( )
    {
        instance = null;
    }

    public DbUtils getDbUtil() {
        return dbUtil;
    }
}
