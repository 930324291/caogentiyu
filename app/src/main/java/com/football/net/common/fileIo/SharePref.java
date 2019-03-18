/**
 * ID: SharePref.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.fileIo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2015年1月3日 下午9:31:04
 * @.modifydate     2015年1月3日 下午9:31:04
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class SharePref
{
	
	private SharedPreferences m_preference;
	
	public SharePref(Context context, String filename)  //名字不带后缀
	{
		m_preference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	}
	
	public boolean contain(String name)
	{
		return m_preference.contains(name);
	}
	public String getString(String name, String value)
	{
		return m_preference.getString(name, value);
	}
	public int getInt(String name, int value)
	{
		return m_preference.getInt(name, value);
	}
	public float getFloat(String name, float value)
	{
		return m_preference.getFloat(name, value);
	}
	public long getLong(String name, long value)
	{
		return m_preference.getLong(name, value);
	}
	public boolean getBoolean(String name, boolean value)
	{
		return m_preference.getBoolean(name, value);
	}
	
	private Editor editor = null;
	@SuppressLint("CommitPrefEdits")
	public void trans_begin()
	{
		editor = m_preference.edit();
	}
	public void trans_commit()
	{
		if (editor != null)
		{
			editor.commit();
			editor = null;
		}
	}
	
	public void setString(String name, String value)
	{
		if(editor!=null)
		{
			editor.putString(name, value);
		}
	}
	public void setInt(String name, int value)
	{
		if(editor!=null)
		{
			editor.putInt(name, value);
		}
	}
	public void setFloat(String name, float value)
	{
		if(editor!=null)
		{
			editor.putFloat(name, value);
		}
	}
	public void setLong(String name, long value)
	{
		if(editor!=null)
		{
			editor.putLong(name, value);
		}
	}
	public void setBoolean(String name, Boolean value)
	{
		if(editor!=null)
		{
			editor.putBoolean(name, value);
		}
	}
	

	public static void setIsautoLoging(Context context,boolean isautologin){
		SharedPreferences.Editor editor = context.getSharedPreferences("proference", Context.MODE_PRIVATE).edit();
		editor.putBoolean("isautologin", isautologin);
		editor.commit();
	}

	public static void setLastReadTime(Context context,long time){
		SharedPreferences.Editor editor = context.getSharedPreferences("proference", Context.MODE_PRIVATE).edit();
		editor.putLong("lastReadTime", time);
		editor.commit();
	}
	public static long getLastReadTime(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("proference", Context.MODE_PRIVATE);
		return sharedPreferences.getLong("lastReadTime",0);
	}

}
