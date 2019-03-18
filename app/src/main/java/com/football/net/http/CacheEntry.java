package com.football.net.http;

import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

/**
 * 接口缓存对象
 *
 */
@Table(name="tbl_cacheentry")
public class CacheEntry
{

	private int id;
	@Unique
	private String key;
	private String data;
	private long serverDate;
	private long updateDate;
	
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	
	public long getServerDate()
	{
		return serverDate;
	}
	public void setServerDate(long serverDate)
	{
		this.serverDate = serverDate;
	}
	
	public long getUpdateDate()
	{
		return updateDate;
	}
	public void setUpdateDate(long updateDate)
	{
		this.updateDate = updateDate;
	}

}
