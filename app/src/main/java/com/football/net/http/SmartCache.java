/**
 * ID: SmartCache.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;

import com.football.net.database.SmartRecord;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;


/**
 * 接口缓存实现
 *
 */
public class SmartCache
{
	
	private SmartRecord m_dbhelper;
	
	public SmartCache(SmartRecord smartrecord)
	{
		m_dbhelper = smartrecord;
	}
	
    public CacheEntry get(String key)
    {

        List<CacheEntry> list = null;
        try {
            list = m_dbhelper.getDbUtil().findAll(Selector.from(CacheEntry.class).where(WhereBuilder.b("key", "=", key)));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return (list==null || list.size()==0)?null:list.get(0);
    }

    public void put(CacheEntry entry)
    {
        try {
            m_dbhelper.getDbUtil().saveOrUpdate(entry);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void remove(String key)
    {
        try {
            m_dbhelper.getDbUtil().delete(CacheEntry.class, WhereBuilder.b("key", "=", key));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void clear()
    {
        try {
            m_dbhelper.getDbUtil().delete(CacheEntry.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
