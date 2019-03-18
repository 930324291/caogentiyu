/**
 * ID: SmartFileLoader.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.fileIo;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2015年1月3日 下午8:34:44
 * @.modifydate     2015年1月3日 下午8:34:44
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class SmartFileLoader
{
	
	private static final Logger logger = LoggerFactory.getLogger(SmartFileLoader.class);
	
	//-------------------------------------------------------------------------------
	
	private Context mContext;
	
	public SmartFileLoader(Context context)
	{
		mContext = context;
	}
	
	public InputStream loadRaw(String name)
	{
		String pkgname = mContext.getPackageName();
		int id = mContext.getResources().getIdentifier(name, "raw", pkgname);
		logger.info("loadRaw : " + name + "/" + pkgname + ", " + id);
		return mContext.getResources().openRawResource(id);
	}
	
	public InputStream loadAssert(String name)
	{
		InputStream fis = null;
		try
		{
			fis = mContext.getAssets().open(name);
		}
		catch (IOException e)
		{
			logger.error("读取文件错误 : " + name, e);
		}
		return fis;
	}
	
	public boolean saveAssert(String name, String content)
	{
		FileOutputStream fos = null;
		try
		{
			fos = mContext.openFileOutput(name, Context.MODE_PRIVATE);
			fos.write(content.getBytes("UTF-8"));
			return true;
		}
		catch (IOException e)
		{
			logger.error("保存文件错误 : " + name, e);
		}
		finally
		{
			try
			{
				if(fos!=null) fos.close();
			}
			catch(IOException e) {}
		}
		return false;
	}

}
